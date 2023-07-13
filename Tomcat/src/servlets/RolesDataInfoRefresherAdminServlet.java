package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.role.RoleImpl;
import stepper.role.RolesManager;
import stepper.systemEngine.SystemEngineInterface;
import stepper.users.User;
import stepper.users.UserManager;
import utilWebApp.DTORole;
import utilWebApp.DTORoleDataFullInfo;
import utilWebApp.DTOUser;
import utils.ServletUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "RolesDataInfoRefresherAdminServlet", urlPatterns = "/roleDataInfoAdminRefresher")
public class RolesDataInfoRefresherAdminServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String roleNameFromParameter = request.getParameter("roleName");

        if (roleNameFromParameter == null) {
            return;
        }

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        UserManager userManager = systemEngine.getUserManager();
        RolesManager rolesManager = systemEngine.getRolesManager();

        Map<String, DTORole> rolesMap = rolesManager.getRoles();
        Map<String, User> usersMap = userManager.getUsers();
        DTORole specificRole = getRoleByName(rolesMap, roleNameFromParameter);

        List<String> listOfUsersConnected = createListOfUserConnectedToSpecificRole(userManager.getUsers(), roleNameFromParameter);
        List<String> listOfAssignedFlows = systemEngine.getListOfFlowsAvailable();
        List<String> listOfAssignedUsers = new ArrayList<>(usersMap.keySet());
        DTORoleDataFullInfo dtoRoleDataFullInfo = new DTORoleDataFullInfo(specificRole,listOfUsersConnected,listOfAssignedFlows,listOfAssignedUsers);
        String dtoRoleDataFullInfoToJSON = new Gson().toJson(dtoRoleDataFullInfo, new TypeToken<DTORoleDataFullInfo>(){}.getType());
        response.getWriter().write(dtoRoleDataFullInfoToJSON);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private List<String> createListOfUserConnectedToSpecificRole(Map<String, User> userMap, String roleName) {

        List<String> listOfUsersConnected = new ArrayList<>();
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            for(Map.Entry<String, RoleImpl> associatedRoleUser : entry.getValue().getAssociatedRole().entrySet())
            {
                if(associatedRoleUser.getKey().equals(roleName))
                {
                    listOfUsersConnected.add(entry.getValue().getUserName());
                }
            }
        }
        return listOfUsersConnected;
    }

    public DTORole getRoleByName(Map<String, DTORole> rolesMap, String name) {
        for (Map.Entry<String, DTORole> entry : rolesMap.entrySet()) {
            String key = entry.getKey();
            if (key.equals(name)) {
                return entry.getValue();
            }
        }
        return null; // Role not found
    }

}
