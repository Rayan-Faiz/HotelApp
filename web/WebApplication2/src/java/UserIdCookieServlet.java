import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UserIdCookieServlet")
public class UserIdCookieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve the user ID cookie from the request
        Cookie[] cookies = request.getCookies();
        int userId = -1;

        // Check if cookies exist
        if (cookies != null) {
            // Iterate through cookies to find the "userId" cookie
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    userId = Integer.parseInt(cookie.getValue());
                    break;
                }
            }
        }

        // Display the user ID
        response.getWriter().println("User ID: " + userId);
    }
}
