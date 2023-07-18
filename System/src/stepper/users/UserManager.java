package stepper.users;
import stepper.role.Role;
import stepper.role.RoleImpl;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private String adminName;
    private final Map<String, User> usersMap = new HashMap<>();

    public synchronized void addUser(String username, boolean isManager) {
        usersMap.put(username, new User(username, isManager));
    }

    public synchronized void removeUser(String username) {
        usersMap.remove(username);
    }

    public synchronized User getUser(String username) {
        return this.usersMap.get(username);
    }

    public synchronized Map<String, User> getUsers() {
        return usersMap;
    }

    public Map<String, RoleImpl> getUserRoles(String userName) {
        return this.usersMap.get(userName).getRoles();
    }

    public boolean isUserExists(String username) {
        return usersMap.containsKey(username);
    }

    public synchronized void setAdminName(String name) {
        adminName = name;
    }

    public synchronized String getAdminName() {
        return this.adminName;
    }

}
