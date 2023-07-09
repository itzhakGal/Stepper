package utils;

import stepper.flow.execution.FlowExecutionImpl;
import stepper.flows.execution.FlowsExecution;

import java.util.ArrayList;
import java.util.List;

public class DTOFlowsPastInformation
{
    private List<DTOFlowPastRun> flows = new ArrayList<>();

    public DTOFlowsPastInformation(FlowsExecution listFlowsExecution)
    {
        int length = listFlowsExecution.getFlowsExecutionList().size();
        for(int i =length-1; i>=0; i--)
        {
            FlowExecutionImpl flowExecution = listFlowsExecution.getFlowsExecutionList().get(i);
            flows.add(new DTOFlowPastRun(flowExecution));
        }
    }
    public List<DTOFlowPastRun> getFlows() {
        return flows;
    }
}
