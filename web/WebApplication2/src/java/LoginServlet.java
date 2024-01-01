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
        // Retrieve email and password from the request
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Authenticate user and retrieve user ID
        int userId = getUserId(email, password);

        // If login is successful, set a cookie with the user ID and redirect to the dashboard
        // Otherwise, redirect to an error page
        if (userId != -1) {
            // Set a cookie for the user's ID
            setUserIdCookie(response, userId);

            // For simplicity, assuming there's a dashboard.jsp page
            response.sendRedirect("dashboard.jsp");
        } else {
            // For simplicity, assuming there's an error.jsp page
            response.sendRedirect("error.jsp");
        }
    }

    private int getUserId(String email, String password) {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Connect to the MySQL database (replace with your database details)
            String jdbcUrl = "jdbc:mysql://localhost:3306/loginapp";
            String dbUsername = "root";
            String dbPassword = "";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
                // Prepare the SQL query
                String sql = "SELECT id FROM users WHERE email = ? AND password = ?";

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, email);
                    statement.setString(2, password);

                    // Execute the query
                    try (ResultSet resultSet = statement.executeQuery()) {
                        // Check if the result set has any rows
                        if (resultSet.next()) {
                            return resultSet.getInt("id");
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Handle exceptions (printStackTrace or log them)
            e.printStackTrace();
        }

        return -1; // Return -1 if user ID is not found
    }

    private void setUserIdCookie(HttpServletResponse response, int userId) {
        Cookie userIdCookie = new Cookie("userId", String.valueOf(userId));

        // Set the cookie's maximum age (in seconds)
        int maxAge = 60 * 60 * 24 * 7; // Example: 1 week
        userIdCookie.setMaxAge(maxAge);

        // Set the cookie's path
        // (This is the URL path for which the cookie is valid, "/" means the whole application)
        userIdCookie.setPath("/");

        // Add the cookie to the response
        response.addCookie(userIdCookie);
    }
}
