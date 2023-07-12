package stepper.role;
import utilWebApp.DTORole;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RolesManager {
    private final Map<String, RoleImpl> roleMap;
    private final Set<String> rolesNames;

    public RolesManager() {
        this.roleMap = new HashMap<String, RoleImpl>() {{
            put("Read Only Flows",new FlowsReadOnlyRole());
            put("All Flows",new AllFlowsRole());

        }};
        this.rolesNames = new HashSet<String>() {{
            add("Read Only Flows");
            add("All Flows");
        }};
    }

    public void addRole(RoleImpl role) {
        this.roleMap.put(role.getName(), role);
        this.rolesNames.add(role.getName());
    }

    public void updateRole(RoleImpl role) {
        this.roleMap.remove(role.getName());
        this.rolesNames.remove(role.getName());
        addRole(role);
    }

    public RoleImpl getRole(String roleName) {
        return this.roleMap.get(roleName);
    }

    public Set<String> getRolesNames() {
        return this.rolesNames;
    }

    public Map<String, RoleImpl> getRoleMap() {
        return this.roleMap;
    }

    public Map<String, DTORole> getRoles() {
        Map<String, DTORole> roles = new HashMap<>();
        for (Map.Entry<String, RoleImpl> roleEntry : roleMap.entrySet()) {
            roles.put(roleEntry.getKey(), new DTORole(roleEntry.getValue().getName(), roleEntry.getValue().getRoleDescription(),
                    roleEntry.getValue().getFlowsAllowed()));
        }
        return roles;
    }

}
