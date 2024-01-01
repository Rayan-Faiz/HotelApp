import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ViewCookieServlet")
public class ViewCookieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve all cookies from the request
        Cookie[] cookies = request.getCookies();

        // Check if cookies exist
        if (cookies != null) {
            response.getWriter().println("<h2>Viewing Cookies:</h2>");

            // Iterate through each cookie and display its name and value
            for (Cookie cookie : cookies) {
                String cookieInfo = "Name: " + cookie.getName() + ", Value: " + cookie.getValue();
                response.getWriter().println(cookieInfo + "<br>");
            }
        } else {
            response.getWriter().println("<h2>No cookies found</h2>");
        }
    }
}
