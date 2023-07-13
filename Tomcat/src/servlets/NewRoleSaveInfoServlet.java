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
import utilWebApp.DTORole;
import utilWebApp.DTOSavaNewInfoForRole;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "NewRoleSaveInfoServlet", urlPatterns = "/newRoleDataToSave")
public class NewRoleSaveInfoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        SystemEngineInterface systemEngine = ServletUtils.getSystemManager(getServletContext());
        //UserManager userManager = systemEngine.getUserManager();
        RolesManager rolesManager = systemEngine.getRolesManager();

        String errorMessage;
        String infoDataSaved = req.getReader().lines().collect(Collectors.joining(" "));
        DTORole dtoRole = new Gson().fromJson(infoDataSaved, DTORole.class);

        boolean isRoleNameExists = CheckIfRoleExistsInRoleManager(rolesManager, dtoRole.getRoleName());

        if(isRoleNameExists)
            errorMessage = "This role already exists in the system";
        else // הרול לא קיים במערכת ולכן צריך להוסיף אותו
        {
            rolesManager.getRoleMap().put(dtoRole.getRoleName() , new RoleImpl(dtoRole));
            errorMessage = "This role added to the system";
        }
        res.getWriter().write(errorMessage);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    private boolean CheckIfRoleExistsInRoleManager(RolesManager rolesManager, String roleName) {

       if(rolesManager.getRoleMap().containsKey(roleName))
           return true;
       else
           return false;
    }

}
