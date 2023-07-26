package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.role.RolesManager;
import stepper.systemEngine.SystemEngineInterface;
import stepper.users.User;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ClientLogOutServlet", urlPatterns = "/logOutClient")
public class ClientLogOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String usernameFromParameter = request.getParameter("username");
        if (usernameFromParameter == null) {
            return;
        }

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        if(!systemEngine.getUserManager().isUserExists(usernameFromParameter))
            return;

        systemEngine.removeUserFromTheUserManager(usernameFromParameter);

    }

    public User getUserByName(Map<String, User> usersMap, String name) {
        for (Map.Entry<String, User> entry : usersMap.entrySet()) {
            String key = entry.getKey();
            if (key.equals(name)) {
                return entry.getValue();
            }
        }
        return null; // User not found
    }
}

