package stepper.users;
import stepper.role.Role;
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

    public Map<String, Role> getUserRoles(String userName) {
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

    /*public void updateUser(UserDTO user) {
        Map<String, Role> roles = new HashMap<>();
        for (Map.Entry<String, RoleDTO> role : user.getRoles().entrySet()) {
            roles.put(role.getKey(), new RoleImpl(role.getValue().getName(), role.getValue().getDescription(), role.getValue().getAllowedFlows()));
        }

        User userToUpdate = this.getUser(user.getName());
        userToUpdate.setIsManager(user.getIsManager());
        userToUpdate.setRoles(roles);
    }*/

}
