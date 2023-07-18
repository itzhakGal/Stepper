package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.role.RolesManager;
import stepper.systemEngine.SystemEngineInterface;
import stepper.users.User;
import stepper.users.UserManager;
import utilWebApp.DTOUser;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;


@WebServlet(name = "UsersListRefresherServlet", urlPatterns = "/usersListRefresher")
public class UsersListRefresherServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();

            SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
            UserManager userManager = systemEngine.getUserManager();

            Map<String, User> usersMap = userManager.getUsers();
            Set<String> usersList = usersMap.keySet();
            String json = gson.toJson(usersList);
            out.println(json);
            out.flush();
        }
    }

}
