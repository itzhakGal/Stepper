package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            UserManager userManager = ServletUtils.getUserManager(getServletContext());

            Map<String, User> usersMap = userManager.getUsers();
            Set<String> usersList = usersMap.keySet();
            String json = gson.toJson(usersList);
            out.println(json);
            out.flush();
        }
    }

    /*@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        String username = SessionUtils.getUsername(req);
        if (username == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Set<DTOUser> usersName = systemEngine.getUsers();
        String usersNameStr = new Gson().toJson(usersName, new TypeToken<Set<DTOUser>>(){}.getType());
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(usersNameStr);
    }*/

}
