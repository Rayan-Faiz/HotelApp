import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve user ID from the cookie
        int userId = getUserIdFromCookie(request);

        // If user ID is found in the cookie, retrieve updated user data from the request
        if (userId != -1) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String address = request.getParameter("address");

            // Retrieve password-related fields
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmNewPassword = request.getParameter("confirmNewPassword");

            // Update user profile in the database, including password change
            boolean isUpdateSuccessful = updateProfile(userId, firstName, lastName, email, address, currentPassword, newPassword);

            // Redirect to the appropriate page based on the update result
            if (isUpdateSuccessful) {
                // For simplicity, assuming there's a success.jsp page
                response.sendRedirect("Update-success.jsp");
            } else {
                // For simplicity, assuming there's an error.jsp page
                response.sendRedirect("error.jsp");
            }
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

    private boolean updateProfile(int userId, String firstName, String lastName, String email, String address, String currentPassword, String newPassword) {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Connect to the MySQL database (replace with your database details)
            String jdbcUrl = "jdbc:mysql://localhost:3306/loginapp";
            String dbUsername = "root";
            String dbPassword = "";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
                // Prepare the SQL query to update user profile and password
                StringBuilder sqlBuilder = new StringBuilder("UPDATE users SET ");
                boolean updatePassword = false;

                if (isNotEmpty(firstName)) {
                    sqlBuilder.append("firstname = ?, ");
                }
                if (isNotEmpty(lastName)) {
                    sqlBuilder.append("last_name = ?, ");
                }
                if (isNotEmpty(email)) {
                    sqlBuilder.append("email = ?, ");
                }
                if (isNotEmpty(address)) {
                    sqlBuilder.append("address = ?, ");
                }
                if (isNotEmpty(newPassword)) {
                    // If newPassword is provided, update password
                    sqlBuilder.append("password = ?, ");
                    updatePassword = true;
                }

                // Remove the trailing comma and space
                sqlBuilder.setLength(sqlBuilder.length() - 2);

                sqlBuilder.append(" WHERE id = ?");
                String sql = sqlBuilder.toString();

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    int parameterIndex = 1;

                    if (isNotEmpty(firstName)) {
                        statement.setString(parameterIndex++, firstName);
                    }
                    if (isNotEmpty(lastName)) {
                        statement.setString(parameterIndex++, lastName);
                    }
                    if (isNotEmpty(email)) {
                        statement.setString(parameterIndex++, email);
                    }
                    if (isNotEmpty(address)) {
                        statement.setString(parameterIndex++, address);
                    }
                    if (updatePassword) {
                        statement.setString(parameterIndex++, newPassword);
                    }

                    statement.setInt(parameterIndex, userId);

                    // Execute the update query
                    int rowsUpdated = statement.executeUpdate();

                    // Check if the update was successful
                    return rowsUpdated > 0;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Handle exceptions (printStackTrace or log them)
            e.printStackTrace();
        }

        return false; // Return false if the update fails
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
