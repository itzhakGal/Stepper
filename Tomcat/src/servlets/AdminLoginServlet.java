package servlets;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.IOException;

@WebServlet(name = "AdminLoginServlet", urlPatterns = "/adminLogin")
public class AdminLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        response.setContentType("text/plain;charset=UTF-8");

        if (usernameFromSession == null) {
            String usernameFromParameter = "Admin";
            if (ServletUtils.isAdminExists()) {
                String errorMessage = "Admin already logged in.";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print(errorMessage);
            }
            else {
                ServletUtils.setAdminExists(true);
                ServletUtils.setCountAdmin(1);
                request.getSession(true).setAttribute("username", usernameFromParameter);
                String countAdmin = String.valueOf(ServletUtils.getCountAdmin());
                response.getOutputStream().print(countAdmin);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}

