package utilWebApp;

import stepper.role.RoleImpl;
import stepper.users.User;

import java.util.ArrayList;
import java.util.List;

public class DTORoleDataFullInfo {

    DTORole roleData;  //שם+תיאור+רשימת פלואים משוייכים

    List<String> allUserConnectedToRole;

    List<String> allFlowsInTheSystem;

    List<String> allUsersInTheSystem;

    public DTORoleDataFullInfo(DTORole roleData, List<String> allUserConnectedToRole, List<String> allFlowsInTheSystem, List<String> allUsersInTheSystem)
    {
        this.roleData = roleData;
        this.allUserConnectedToRole = allUserConnectedToRole;
        this.allFlowsInTheSystem = allFlowsInTheSystem;
        this.allUsersInTheSystem = allUsersInTheSystem;
    }


    public DTORole getRoleData() {
        return roleData;
    }

    public List<String> getAllFlowsInTheSystem() {
        return allFlowsInTheSystem;
    }

    public List<String> getAllUserConnectedToRole() {
        return allUserConnectedToRole;
    }

    public List<String> getAllUsersInTheSystem() {
        return allUsersInTheSystem;
    }

}
