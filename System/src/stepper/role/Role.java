package stepper.role;

import java.util.List;
import java.util.Set;

public interface Role {

    String getName();
    String getRoleDescription();
    Set<String> getFlowsAllowed();
    void addAllowedFlow(String flowName);

}
