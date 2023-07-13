package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.role.RoleImpl;
import stepper.role.RolesManager;
import stepper.systemEngine.SystemEngineInterface;
import stepper.users.User;
import stepper.users.UserManager;
import utilWebApp.DTOSavaNewInfoForRole;
import utilWebApp.DTOSavaNewInfoForUser;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "RoleNewInfoSavedServlet", urlPatterns = "/savaNewDataRole")
public class RoleNewInfoSavedServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        UserManager userManager = systemEngine.getUserManager();
        RolesManager rolesManager = systemEngine.getRolesManager();

        String infoDataSaved = req.getReader().lines().collect(Collectors.joining(" "));
        DTOSavaNewInfoForRole dtoSavaNewInfoForRole = new Gson().fromJson(infoDataSaved, DTOSavaNewInfoForRole.class);

        RoleImpl roleData = getRoleByName(rolesManager.getRoleMap(), dtoSavaNewInfoForRole.getRoleName());
        insertNewDataToRoleInMap(roleData, userManager.getUsers(), dtoSavaNewInfoForRole);
        updateRoleFlowList(roleData, dtoSavaNewInfoForRole);


        res.setStatus(HttpServletResponse.SC_OK);
    }


    public void insertNewDataToRoleInMap(RoleImpl roleData, Map<String, User> usersMap, DTOSavaNewInfoForRole dtoSavaNewInfoForRole) {

        /*for (Map.Entry<String, User> entry : usersMap.entrySet()) {
            if(!entry.getValue().getAssociatedRole().containsKey(dtoSavaNewInfoForRole.getRoleName()))
            {
                entry.getValue().getAssociatedRole().put(dtoSavaNewInfoForRole.getRoleName(), roleData);
            }
        }*/

        for (Map.Entry<String, User> entry : usersMap.entrySet()) {
            if(dtoSavaNewInfoForRole.getListUserToAddToTheRole().contains(entry.getKey()))
            {
                entry.getValue().getAssociatedRole().put(dtoSavaNewInfoForRole.getRoleName(), roleData);
            }
        }
    }
    public void updateRoleFlowList(RoleImpl roleData, DTOSavaNewInfoForRole dtoSavaNewInfoForRole)
    {
        for(String flow : dtoSavaNewInfoForRole.getListFlowsToAddToTheRole()) {
            if (!roleData.getFlowsAllowed().contains(flow)) {
                roleData.getFlowsAllowed().add(flow);
            }
        }
    }
    public RoleImpl getRoleByName(Map<String, RoleImpl> roleMap, String name) {
        for (Map.Entry<String, RoleImpl> entry : roleMap.entrySet()) {
            String key = entry.getKey();
            if (key.equals(name)) {
                return entry.getValue();
            }
        }
        return null; // User not found
    }

}
