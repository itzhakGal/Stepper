package utilWebApp;

import java.util.List;

public class DTOSavaNewInfoForRole {

    private String roleName;
    private List<String> listFlowsToAddToTheRole;
    private List<String> listFlowsToRemoveFromTheRole;
    private List<String> listUserToAddToTheRole;
    private List<String> listUsersToRemoveFromTheRole;

    public DTOSavaNewInfoForRole(String roleName, List<String> listFlowsToAddToTheRole, List<String> listFlowsToRemoveFromTheRole, List<String> listUserToAddToTheRole, List<String> listUsersToRemoveFromTheRole)
    {
        this.roleName = roleName;
        this.listFlowsToAddToTheRole = listFlowsToAddToTheRole;
        this.listFlowsToRemoveFromTheRole = listFlowsToRemoveFromTheRole;
        this.listUserToAddToTheRole = listUserToAddToTheRole;
        this.listUsersToRemoveFromTheRole = listUsersToRemoveFromTheRole;
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

    public List<String> getListFlowsToRemoveFromTheRole() {
        return listFlowsToRemoveFromTheRole;
    }

    public List<String> getListUsersToRemoveFromTheRole() {
        return listUsersToRemoveFromTheRole;
    }

    public void setListFlowsToAddToTheRole(List<String> listFlowsToAddToTheRole) {
        this.listFlowsToAddToTheRole = listFlowsToAddToTheRole;
    }

    public void setListUserToAddToTheRole(List<String> listUserToAddToTheRole) {
        this.listUserToAddToTheRole = listUserToAddToTheRole;
    }
}
