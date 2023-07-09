package stepper.users;

import stepper.role.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    boolean isManager;
    protected String userName;
    private Map<String, Role> associatedRole;

    public User(String name, boolean isManager)
    {
        this.userName = name;
        this.isManager = isManager;
        this.associatedRole = new HashMap<>();
    }

    public boolean isManager() {
        return isManager;
    }
    public String getUserName() {
        return userName;
    }

    public Map<String, Role> getAssociatedRole() {
        return associatedRole;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public Map<String, Role> getRoles() {
        return associatedRole;
    }

    public boolean getIsManager() {
        return isManager;
    }
    public void setAssociatedRole(Map<String, Role> roles) {
        for (Map.Entry<String, Role> role : roles.entrySet()) {
            if (!this.associatedRole.containsKey(role.getKey()))
                this.associatedRole.put(role.getKey(), role.getValue());
        }
    }

}
