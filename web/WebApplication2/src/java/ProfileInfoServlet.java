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
        // Retrieve user ID from the cookie
        int userId = getUserIdFromCookie(request);

        // If user ID is found in the cookie, retrieve user information
        if (userId != -1) {
            // Retrieve user information from the database
            UserData userData = getUserData(userId);

            // Pass user data to the JSP page
            request.setAttribute("userData", userData);

            // Forward to the profile info JSP page
            request.getRequestDispatcher("profileInfo.jsp").forward(request, response);
        } else {
            // User ID not found in the cookie, redirect to an error page
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
        // Load the MySQL JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Connect to the MySQL database (replace with your database details)
        String jdbcUrl = "jdbc:mysql://localhost:3306/loginapp";
        String dbUsername = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            // Prepare the SQL query
            String sql = "SELECT id, firstname, last_name, email, address FROM users WHERE id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);

                // Execute the query
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Check if the result set has any rows
                    if (resultSet.next()) {
                        // Retrieve user data from the result set
                        int id = resultSet.getInt("id");
                        String firstName = resultSet.getString("firstname");
                        String lastName = resultSet.getString("last_name");  // Fix column name
                        String email = resultSet.getString("email");
                        String address = resultSet.getString("address");

                        // Create and return a UserData object
                        return new UserData(id, firstName, lastName, email, address);
                    }
                }
            }
        }
    } catch (ClassNotFoundException | SQLException e) {
        // Handle exceptions (printStackTrace or log them)
        e.printStackTrace();
    }

    return null; // Return null if user data is not found
}

}
