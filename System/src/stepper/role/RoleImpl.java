package stepper.role;

import utilWebApp.DTORole;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleImpl implements Role {
    private String roleName;
    private String roleDescription;
    private Set<String> allowedFlows;


    public RoleImpl(String name, String description) {
        this.roleName = name;
        this.roleDescription = description;
        this.allowedFlows = new HashSet<>();
    }
    public RoleImpl(DTORole dtoRole) {
        this.roleName = dtoRole.getRoleName();
        this.roleDescription = dtoRole.getDescription();
        this.allowedFlows = dtoRole.getAllowedFlows();
    }
    @Override
    public String getName() {
        return roleName;
    }

    @Override
    public String getRoleDescription() {
        return roleDescription;
    }

    @Override
    public Set<String> getFlowsAllowed() {
        return allowedFlows;
    }

    @Override
    public void addAllowedFlow(String flowName) {
        allowedFlows.add(flowName);
    }

    public void setAllowedFlows(Set<String> allowedFlows) {
        this.allowedFlows = allowedFlows;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
