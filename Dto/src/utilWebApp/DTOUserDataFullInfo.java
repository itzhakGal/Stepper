package utilWebApp;

import stepper.users.User;

import java.util.ArrayList;
import java.util.List;

public class DTOUserDataFullInfo {

    User userData;  //שם+תיאור+רשימת רולים משוייכים שכולל את רשימת הפלואים פר רולים

    List<String> totalFlowPreformedByUser;

    List<String> allRoleInSystem;


    public DTOUserDataFullInfo(User userData,  List<String> allRoleInSystem,List<String> totalFlowPreformedByUser)
    {
        this.totalFlowPreformedByUser = totalFlowPreformedByUser;
        this.allRoleInSystem = allRoleInSystem;
        this.userData = userData;
    }

    public User getUser() {
        return userData;
    }

    public void setUser(DTOUser userDTO) {
        this.userData = userData;
    }

    public List<String> getTotalFlowPreformedByUser() {
        return totalFlowPreformedByUser;
    }

    public void setTotalFlowPreformedByUser(List<String> totalFlowPreformedByUser) {
        this.totalFlowPreformedByUser = totalFlowPreformedByUser;
    }

    public List<String> getAllRoleInSystem() {
        return allRoleInSystem;
    }

    public void setAllRoleInSystem(List<String> allRoleInSystem) {
        this.allRoleInSystem = allRoleInSystem;
    }

}
