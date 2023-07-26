package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.ha.session.DeltaSession;
import stepper.role.Role;
import stepper.role.RoleImpl;
import stepper.role.RolesManager;
import stepper.systemEngine.SystemEngineInterface;
import stepper.users.User;
import stepper.users.UserManager;
import utilWebApp.DTOSavaNewInfoForUser;
import utils.ServletUtils;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        dtoSavaNewInfoForUser.getListRolesToAddToTheUser().remove("Assign Roles To User");
        insertNewDataToUserInMap(systemEngine, rolesManager, userManager.getUsers(), dtoSavaNewInfoForUser);
        systemEngine.initialUserMapFlowsDefinition(dtoSavaNewInfoForUser);

        res.setStatus(HttpServletResponse.SC_OK);
    }


    public void insertNewDataToUserInMap(SystemEngineInterface systemEngine, RolesManager rolesManager, Map<String, User> usersMap, DTOSavaNewInfoForUser dtoSavaNewInfoForUser) {

        RoleImpl allFlowsRole = rolesManager.getRole("All Flows");
        for (Map.Entry<String, User> entry : usersMap.entrySet()) {
            if(entry.getKey().equals(dtoSavaNewInfoForUser.getUserName()))
            {
                updateRolesInMapUser(rolesManager, entry.getValue(), dtoSavaNewInfoForUser);
                entry.getValue().setIsManager(dtoSavaNewInfoForUser.isManager());
                // יוזר מנהל
                if(entry.getValue().getIsManager())
                {
                    boolean isExists = checkIfAllFlowExists(entry.getValue());
                    if(isExists)
                    {
                        entry.getValue().setAllFlowExistsFromManager(false);
                    }
                    else {
                        entry.getValue().setAllFlowExistsFromManager(true);
                    }

                    entry.getValue().getAssociatedRole().put("All Flows", allFlowsRole);
                }
                else
                {
                   if(entry.getValue().isAllFlowExistsFromManager())
                   {
                       entry.getValue().getAssociatedRole().remove("All Flows");
                   }
                }
            }
        }
    }

    private boolean checkIfAllFlowExists(User user) {

        if(user.getAssociatedRole().containsKey("All Flows"))
            return true;
        return false;
    }

    public void updateRolesInMapUser(RolesManager rolesManager, User user, DTOSavaNewInfoForUser dtoSavaNewInfoForUser)
    {
        for(String roleName : dtoSavaNewInfoForUser.getListRolesToAddToTheUser()) {
            if (!user.getAssociatedRole().containsKey(roleName)) {
                RoleImpl role = rolesManager.getRole(roleName);
                user.getAssociatedRole().put(roleName, role);
            }
            if(roleName.equals("All Flows"))
                user.setAllFlowExistsFromManager(false);
        }

        for(String roleName : dtoSavaNewInfoForUser.getListRolesToRemoveFromTheUser()) {
            if (user.getAssociatedRole().containsKey(roleName)) {
                RoleImpl role = rolesManager.getRole(roleName);
                user.getAssociatedRole().remove(roleName, role);
            }
            if(roleName.equals("All Flows"))
                user.setAllFlowExistsFromManager(false);
        }
    }

}
