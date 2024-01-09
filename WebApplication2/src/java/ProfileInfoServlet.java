import java.io.IOException;
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

@WebServlet("/ProfileInfoServlet")
public class ProfileInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = getUserIdFromCookie(request);
        if (userId != -1) {
            UserData userData = getUserData(userId);
            request.setAttribute("userData", userData);
            request.getRequestDispatcher("profileInfo.jsp").forward(request, response);
        } else {
            response.sendRedirect("error.jsp");
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

    private UserData getUserData(int userId) {
    try {
        Class.forName("com.mysql.jdbc.Driver");
        String jdbcUrl = "jdbc:mysql://localhost:3306/hotel";
        String dbUsername = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            String sql = "SELECT c_id, c_firstname, c_lastname, c_email, c_addr FROM clients WHERE c_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("c_id");
                        String firstName = resultSet.getString("c_firstname");
                        String lastName = resultSet.getString("c_lastname");  
                        String email = resultSet.getString("c_email");
                        String address = resultSet.getString("c_addr");
                        return new UserData(id, email, firstName, lastName, address);
                    }
                }
            }
        }
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }

    return null;
}

}
