package stepper.users;

import stepper.role.Role;
import stepper.role.RoleImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private boolean isManager;
    protected String userName;
    private Map<String, RoleImpl> associatedRole;
    private boolean isAllFlowExistsFromManager;

    public User(String name, boolean isManager)
    {
        this.userName = name;
        this.isManager = isManager;
        this.associatedRole = new HashMap<>();
        this.isAllFlowExistsFromManager = false;
    }

    public boolean isManager() {
        return isManager;
    }
    public String getUserName() {
        return userName;
    }

    public Map<String, RoleImpl> getAssociatedRole() {
        return associatedRole;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setIsManager(boolean manager) {
        isManager = manager;
    }

    public Map<String, RoleImpl> getRoles() {
        return associatedRole;
    }

    public boolean getIsManager() {
        return isManager;
    }
    public void setAssociatedRole(Map<String, RoleImpl> roles) {
        for (Map.Entry<String, RoleImpl> role : roles.entrySet()) {
            if (!this.associatedRole.containsKey(role.getKey()))
                this.associatedRole.put(role.getKey(), role.getValue());
        }
    }
    public boolean isAllFlowExistsFromManager() {
        return isAllFlowExistsFromManager;
    }
    public void setAllFlowExistsFromManager(boolean allFlowExistsFromManager) {
        isAllFlowExistsFromManager = allFlowExistsFromManager;
    }
}
