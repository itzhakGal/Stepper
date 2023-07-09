package stepper.flows.execution;

import stepper.flow.execution.FlowExecutionImpl;

import java.util.ArrayList;
import java.util.List;

public class FlowsExecutionImp implements FlowsExecution{
    private List<FlowExecutionImpl> flowsExecutionList;


    public FlowsExecutionImp()
    {
        this.flowsExecutionList= new ArrayList<>();
    }

    @Override
    public List<FlowExecutionImpl> getFlowsExecutionList() {
        return flowsExecutionList;
    }
}
