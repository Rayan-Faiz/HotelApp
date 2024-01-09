import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ReservationServlet")
public class ReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String roomType = request.getParameter("roomType");
            String bedType = request.getParameter("bedType");
            String checkInDate = request.getParameter("checkInDate");
            String checkOutDate = request.getParameter("checkOutDate");

            try {
                System.out.println("Before executing isRoomAvailable");
                if (isRoomAvailable(roomType, bedType, checkInDate, checkOutDate)) {
                    System.out.println("Room is available. Proceeding with reservation.");
                    if (insertReservation(roomType, bedType, checkInDate, checkOutDate, request)) {
                        out.println("<h3>Reservation Successful!</h3>");
                    } else {
                        out.println("<h3>Failed to make reservation. Please try again later.</h3>");
                    }
                } else {
                    System.out.println("Room is not available. Reservation canceled.");
                    out.println("<h3>Selected room is not available for the specified dates.</h3>");
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.println("<h3>An error occurred. Please check the server logs for more details.</h3>");
            }
        }
    }

    private boolean isRoomAvailable(String roomType, String bedType, String checkInDate, String checkOutDate)
            throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "")) {
            String query = "SELECT COUNT(*) FROM rooms WHERE rm_type = ? AND rm_bed = ? AND rm_status = 1";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, roomType);
                pstmt.setString(2, bedType);

                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (resultSet.next()) {
                        int availableRooms = resultSet.getInt(1);
                        return availableRooms > 0;
                    }
                }
            }
        }
        return false;
    }

   private boolean insertReservation(String roomType, String bedType, String checkInDate, String checkOutDate,
        HttpServletRequest request) throws SQLException {
    HttpSession session = request.getSession(false);
    if (session != null) {
        int userId = getUserIdFromCookie(request);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "")) {
         
            int roomNumber = getRoomNumber(roomType, bedType, conn);

            if (roomNumber != -1) {
                String insertQuery = "INSERT INTO resa (c_id, rm_number, rs_start, rs_end) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setInt(1, userId);
                    pstmt.setInt(2, roomNumber);
                    pstmt.setString(3, checkInDate);
                    pstmt.setString(4, checkOutDate);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        
                        updateRoomStatus(roomNumber, conn);
                    }

                    return rowsAffected > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return false;
}

private void updateRoomStatus(int roomNumber, Connection conn) throws SQLException {
    String updateQuery = "UPDATE rooms SET rm_status = 0 WHERE rm_number = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
        pstmt.setInt(1, roomNumber);
        pstmt.executeUpdate();
    }
}


    private int getUserIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        int userId = -1;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    userId = Integer.parseInt(cookie.getValue());
                    break;
                }
            }
        }

        return userId;
    }

    private int getRoomNumber(String roomType, String bedType, Connection conn) throws SQLException {
        String query = "SELECT rm_number FROM rooms WHERE rm_type = ? AND rm_bed = ? AND rm_status = 1 LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, roomType);
            pstmt.setString(2, bedType);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("rm_number");
                }
            }
        }
        return -1;
    }
}
