package stepper.role;
import utilWebApp.DTORole;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RolesManager {
    private final Map<String, Role> roleMap;
    private final Set<String> rolesNames;

    public RolesManager() {
        this.roleMap = new HashMap<String, Role>() {{
            put("Read Only Flows",new AllFlowsRole());
            put("All Flows",new FlowsReadOnlyRole());
        }};
        this.rolesNames = new HashSet<String>() {{
            add("Read Only Flows");
            add("All Flows");
        }};
    }

    public void addRole(Role role) {
        this.roleMap.put(role.getName(), role);
        this.rolesNames.add(role.getName());
    }

    public void updateRole(Role role) {
        this.roleMap.remove(role.getName());
        this.rolesNames.remove(role.getName());
        addRole(role);
    }

    public Role getRole(String roleName) {
        return this.roleMap.get(roleName);
    }

    public Set<String> getRolesNames() {
        return this.rolesNames;
    }

    public Map<String, Role> getRoleMap() {
        return this.roleMap;
    }

    public Map<String, DTORole> getRoles() {
        Map<String, DTORole> roles = new HashMap<>();
        for (Map.Entry<String, Role> roleEntry : roleMap.entrySet()) {
            roles.put(roleEntry.getKey(), new DTORole(roleEntry.getValue().getName(), roleEntry.getValue().getRoleDescription(),
                    roleEntry.getValue().getFlowsAllowed()));
        }
        return roles;
    }

}
