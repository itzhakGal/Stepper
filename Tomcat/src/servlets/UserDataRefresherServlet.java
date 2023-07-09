package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.users.User;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "UserDataRefresherServlet", urlPatterns = "/userDataRefresher")
public class UserDataRefresherServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String usernameFromParameter = request.getParameter("username");

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());

            Map<String, User> usersMap = userManager.getUsers();
            User specificUser = getUserByName(usersMap, usernameFromParameter);

            String json = gson.toJson(specificUser);
            out.println(json);
            out.flush();
        }


        //UserDTO user = engineManager.getUser(username);
        //String userStr = new Gson().toJson(user, UserDTO.class);
        //res.setStatus(HttpServletResponse.SC_OK);
        //res.getWriter().write(userStr);
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
