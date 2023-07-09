package utilWebApp;

import java.util.Set;

public class DTORole {
    private String roleName;
    private String description;
    private Set<String> allowedFlows;

    public DTORole(String name, String description, Set<String> allowedFlows) {
        this.roleName = name;
        this.description = description;
        this.allowedFlows = allowedFlows;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getDescription() {
        return this.description;
    }

    public Set<String> getAllowedFlows() {
        return this.allowedFlows;
    }
}
