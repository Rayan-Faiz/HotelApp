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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        
        int userId = getUserId(email, password);

        if (userId != -1) {
           
            setUserIdCookie(response, userId);

            response.sendRedirect("dashboard.jsp");
        } else {
            response.sendRedirect("error.jsp");
        }
    }

    private int getUserId(String email, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/hotel";
            String dbUsername = "root";
            String dbPassword = "";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
                String sql = "SELECT c_id FROM clients WHERE c_email = ? AND c_password = ?";

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, email);
                    statement.setString(2, password);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            return resultSet.getInt("c_id");
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private void setUserIdCookie(HttpServletResponse response, int userId) {
        Cookie userIdCookie = new Cookie("userId", String.valueOf(userId));

        int maxAge = 60 * 60 * 24 * 7;
        userIdCookie.setMaxAge(maxAge);
        userIdCookie.setPath("/");
        response.addCookie(userIdCookie);
    }
}
