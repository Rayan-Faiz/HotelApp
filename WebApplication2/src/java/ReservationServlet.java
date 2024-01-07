import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ReservationServlet")
public class ReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int userId = getUserIdFromCookie(request);
        String roomType = request.getParameter("roomType");
        String bedType = request.getParameter("bedType");
        String checkInDate = request.getParameter("checkInDate");
        String checkOutDate = request.getParameter("checkOutDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date checkIn = null;
        Date checkOut = null;

        try {
            checkIn = sdf.parse(checkInDate);
            checkOut = sdf.parse(checkOutDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ReservationService reservationService = new ReservationService();
        if (reservationService.isRoomAvailable(roomType, bedType)
                && !reservationService.isRoomReserved(roomType, checkInDate, checkOutDate)
                && reservationService.reserveRoom(userId, roomType, checkInDate, checkOutDate)) {
            PrintWriter out = response.getWriter();
            out.println("<html><body><h2>Reservation Successful!</h2></body></html>");
        } else {
            PrintWriter out = response.getWriter();
            if (!reservationService.isRoomAvailable(roomType, bedType)) {
                out.println("<html><body><h2>Selected room type is not available!</h2></body></html>");
            } else if (reservationService.isRoomReserved(roomType, checkInDate, checkOutDate)) {
                out.println("<html><body><h2>Room is already reserved for the selected dates!</h2></body></html>");
            } else {
                out.println("<html><body><h2>Reservation failed. Please try again.</h2></body></html>");
            }
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
}
