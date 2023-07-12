package utilWebApp;

import java.util.List;

public class DTOSavaNewInfoForRole {

    private String roleName;
    private List<String> listFlowsToAddToTheRole;
    private List<String> listUserToAddToTheRole;


    public DTOSavaNewInfoForRole(String roleName, List<String> listFlowsToAddToTheRole, List<String> listUserToAddToTheRole)
    {
        this.roleName = roleName;
        this.listFlowsToAddToTheRole = listFlowsToAddToTheRole;
        this.listUserToAddToTheRole = listUserToAddToTheRole;
    }

    public List<String> getListFlowsToAddToTheRole() {
        return listFlowsToAddToTheRole;
    }

    public List<String> getListUserToAddToTheRole() {
        return listUserToAddToTheRole;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setListFlowsToAddToTheRole(List<String> listFlowsToAddToTheRole) {
        this.listFlowsToAddToTheRole = listFlowsToAddToTheRole;
    }

    public void setListUserToAddToTheRole(List<String> listUserToAddToTheRole) {
        this.listUserToAddToTheRole = listUserToAddToTheRole;
    }
}
