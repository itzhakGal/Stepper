package stepper.flows.definition;

import stepper.flow.definition.api.FlowDefinition;
import java.io.Serializable;
import java.util.List;

public interface FlowsDefinition extends Serializable {

    boolean checkNameExistsInList(String str);
    List<FlowDefinition> getListFlowsDefinition();
    void setListFlowsDefinition(List<FlowDefinition> listFlowsDefinition);

    void setThreadPoolSize(int threadPoolSize);
    int getThreadPoolSize();
}
