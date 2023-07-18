package utilWebApp;

import java.util.List;

public class DTOSavaNewInfoForUser {
    private String userName;
    private boolean isManager;
    private List<String> listRolesToAddToTheUser;
    private List<String> listRolesToRemoveFromTheUser;


    public DTOSavaNewInfoForUser(String userName, List<String> listRolesToAddToTheUser,List<String> listRolesToRemoveFromTheUser, boolean isManager)
    {
        this.userName = userName;
        this.listRolesToAddToTheUser = listRolesToAddToTheUser;
        this.listRolesToRemoveFromTheUser = listRolesToRemoveFromTheUser;
        this.isManager = isManager;
    }

    public boolean isManager() {
        return isManager;
    }
    public void setManager(boolean manager) {
        isManager = manager;
    }
    public void setListRolesToAddToTheUser(List<String> listRolesToAddToTheUser) {
        this.listRolesToAddToTheUser = listRolesToAddToTheUser;
    }
    public List<String> getListRolesToAddToTheUser() {
        return listRolesToAddToTheUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getListRolesToRemoveFromTheUser() {
        return listRolesToRemoveFromTheUser;
    }
}
