package stepper.flows.execution;

import stepper.flow.execution.FlowExecutionImpl;

import java.io.Serializable;
import java.util.List;

public interface FlowsExecution extends Serializable {
    List<FlowExecutionImpl> getFlowsExecutionList();

}
