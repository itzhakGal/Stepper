package utilWebApp;

import stepper.users.User;

import java.util.Map;

public class DTOUser {
    private String userName;
    private boolean isManager;
    private Map<String, DTORole> roles;

    public DTOUser(String name, boolean isManager) {
        this.userName = name;
        this.isManager = isManager;
    }

    public DTOUser(String name, boolean isManager, Map<String, DTORole> roles) {
        this.userName = name;
        this.isManager = isManager;
        this.roles = roles;
    }

    public String getName() {
        return this.userName;
    }

    public boolean getIsManager() {
        return this.isManager;
    }

    public Map<String, DTORole> getRoles() {
        return this.roles;
    }

}
