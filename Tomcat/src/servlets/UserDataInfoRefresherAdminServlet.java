package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.role.RolesManager;
import stepper.systemEngine.SystemEngineInterface;
import stepper.users.User;
import stepper.users.UserManager;
import utilWebApp.DTORole;
import utilWebApp.DTOUserDataFullInfo;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UserDataInfoRefresherAdminServlet", urlPatterns = "/userDataInfoAdminRefresher")
public class UserDataInfoRefresherAdminServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String usernameFromParameter = request.getParameter("username");

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            RolesManager rolesManager = ServletUtils.getRolesManager(getServletContext());
            SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());

            Map<String, User> usersMap = userManager.getUsers();
            User specificUser = getUserByName(usersMap, usernameFromParameter);
            Map<String, DTORole> roles = rolesManager.getRoles();
            List<String> assignedRoles = new ArrayList<>(roles.keySet());
            //List<String> totalFlowsPreformedBySpecificUser - צריך להוסיף את הרשימה של הפלואו שהורצו ע"י יוזר ספציפי
            DTOUserDataFullInfo dtoUserDataFullInfo = new DTOUserDataFullInfo(specificUser, assignedRoles);

            String json = gson.toJson(dtoUserDataFullInfo);
            out.println(json);
            out.flush();
        }

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
