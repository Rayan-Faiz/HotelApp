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
        int userId = getUserIdFromCookie(request);
        if (userId != -1) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmNewPassword = request.getParameter("confirmNewPassword");
            boolean isUpdateSuccessful = updateProfile(userId, firstName, lastName, email, address, currentPassword, newPassword);
            if (isUpdateSuccessful) {
                response.sendRedirect("Update-success.jsp");
            } else {
                response.sendRedirect("error.jsp");
            }
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

    private boolean updateProfile(int userId, String firstName, String lastName, String email, String address, String currentPassword, String newPassword) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/hotel";
            String dbUsername = "root";
            String dbPassword = "";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
                StringBuilder sqlBuilder = new StringBuilder("UPDATE clients SET ");
                boolean updatePassword = false;

                if (isNotEmpty(firstName)) {
                    sqlBuilder.append("c_firstname = ?, ");
                }
                if (isNotEmpty(lastName)) {
                    sqlBuilder.append("c_lastname = ?, ");
                }
                if (isNotEmpty(email)) {
                    sqlBuilder.append("c_email = ?, ");
                }
                if (isNotEmpty(address)) {
                    sqlBuilder.append("c_address = ?, ");
                }
                if (isNotEmpty(newPassword)) {
                    sqlBuilder.append("c_password = ?, ");
                    updatePassword = true;
                }
                sqlBuilder.setLength(sqlBuilder.length() - 2);

                sqlBuilder.append(" WHERE c_id = ?");
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
                    int rowsUpdated = statement.executeUpdate();
                    return rowsUpdated > 0;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
