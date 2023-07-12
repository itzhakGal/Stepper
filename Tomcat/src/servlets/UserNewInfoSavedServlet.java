package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.role.Role;
import stepper.role.RoleImpl;
import stepper.role.RolesManager;
import stepper.systemEngine.SystemEngineInterface;
import stepper.users.User;
import stepper.users.UserManager;
import utilWebApp.DTOSavaNewInfoForUser;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "UserNewInfoSavedServlet", urlPatterns = "/savaNewDataUser")
public class UserNewInfoSavedServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        UserManager userManager = systemEngine.getUserManager();
        RolesManager rolesManager = systemEngine.getRolesManager();

        String infoDataSaved = req.getReader().lines().collect(Collectors.joining(" "));
        DTOSavaNewInfoForUser dtoSavaNewInfoForUser = new Gson().fromJson(infoDataSaved, DTOSavaNewInfoForUser.class);

        insertNewDataToUserInMap(rolesManager, userManager.getUsers(), dtoSavaNewInfoForUser);
        res.setStatus(HttpServletResponse.SC_OK);
    }


    public void insertNewDataToUserInMap(RolesManager rolesManager, Map<String, User> usersMap, DTOSavaNewInfoForUser dtoSavaNewInfoForUser) {

        for (Map.Entry<String, User> entry : usersMap.entrySet()) {
            if(entry.getKey().equals(dtoSavaNewInfoForUser.getUserName()))
            {
                entry.getValue().setIsManager(dtoSavaNewInfoForUser.isManager());
                updateRolesInMapUser(rolesManager, entry.getValue().getAssociatedRole() ,dtoSavaNewInfoForUser);

            }
        }
    }

    public void updateRolesInMapUser(RolesManager rolesManager, Map<String, RoleImpl> userRoleMap, DTOSavaNewInfoForUser dtoSavaNewInfoForUser)
    {
        for(String roleName : dtoSavaNewInfoForUser.getListRolesToAddToTheUser()) {
            if (!userRoleMap.containsKey(roleName)) {
                RoleImpl role = rolesManager.getRole(roleName);
                userRoleMap.put(roleName, role);
            }
        }
    }

}
