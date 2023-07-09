package stepper.flows.definition;

import stepper.flow.definition.api.FlowDefinition;

import java.util.ArrayList;
import java.util.List;

public class FlowsDefinitionImpl implements FlowsDefinition {
    private List<FlowDefinition> listFlowsDefinition;

    private int threadPoolSize;

    public FlowsDefinitionImpl(){
        listFlowsDefinition = new ArrayList<>();
        threadPoolSize = 0;
    }
    @Override
    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
    @Override
    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    @Override
    public boolean checkNameExistsInList(String str)
    {
        boolean flag = true;
        String outputToTheUser = "";
        for(FlowDefinition flowName : listFlowsDefinition)
        {
            if(flowName.getName().equals(str)) {
                outputToTheUser = "The flow with this name already exists in this file\n";
                flag = false;
            }
        }
        return flag;
    }
    @Override
    public List<FlowDefinition> getListFlowsDefinition() {
        return listFlowsDefinition;
    }

    @Override
    public void setListFlowsDefinition(List<FlowDefinition> listFlowsDefinition) {
        this.listFlowsDefinition = listFlowsDefinition;
    }
}
