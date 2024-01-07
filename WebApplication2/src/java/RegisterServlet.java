import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        if (password.equals(confirmPassword)) {
            boolean isRegistered = registerUser(firstname,lastname, password, email,address);
            if (isRegistered) {
                response.sendRedirect("registration-success.jsp");
            } else {
                response.sendRedirect("registration-failure.jsp");
            }
        } else {
            response.sendRedirect("registration-error.jsp");
        }
    }

    private boolean registerUser(String firstname,String lastname, String password, String email, String address) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/hotel";
            String dbUsername = "root";
            String dbPassword = "";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
                String sql = "INSERT INTO clients (c_firstname,c_last_name, c_password, c_email,c_address) VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, firstname);
                    statement.setString(2, lastname);
                    statement.setString(3, password);
                    statement.setString(4, email);
                    statement.setString(5, address);
                    int rowsAffected = statement.executeUpdate();
                    return rowsAffected > 0;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
