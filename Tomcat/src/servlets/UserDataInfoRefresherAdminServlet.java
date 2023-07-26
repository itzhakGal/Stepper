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
import utilWebApp.DTORole;
import utilWebApp.DTOUser;
import utilWebApp.DTOUserDataFullInfo;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "UserDataInfoRefresherAdminServlet", urlPatterns = "/userDataInfoAdminRefresher")
public class UserDataInfoRefresherAdminServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String usernameFromParameter = request.getParameter("username");

        if (usernameFromParameter == null) {
            return;
        }

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        UserManager userManager = systemEngine.getUserManager();
        RolesManager rolesManager = systemEngine.getRolesManager();

        Map<String, User> usersMap = userManager.getUsers();
        User specificUser = getUserByName(usersMap, usernameFromParameter);

        Map<String, DTORole> roles = rolesManager.getRoles();
        List<String> assignedRoles = new ArrayList<>(roles.keySet());

        List<String> totalFlowsPreformedBySpecificUser  = systemEngine.getFlowsExecutedNameByUserName(specificUser);

        DTOUserDataFullInfo dtoUserDataFullInfo = new DTOUserDataFullInfo(specificUser, assignedRoles, totalFlowsPreformedBySpecificUser);
        String dtoUserDataFullInfoToJSON = new Gson().toJson(dtoUserDataFullInfo, new TypeToken<DTOUserDataFullInfo>(){}.getType());
        response.getWriter().write(dtoUserDataFullInfoToJSON);
        response.setStatus(HttpServletResponse.SC_OK);
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
