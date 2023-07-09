package utils;

import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.FlowExecutionImpl;
import stepper.flow.execution.FlowExecutionResult;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.step.api.StepResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DTOEndOFlowExecution
{
    private UUID uniqueId;
    private String name;
    private FlowExecutionResult result;
    private Map<String, Data> outputs;

    public DTOEndOFlowExecution(FlowExecutionImpl flowExecution) {
        this.uniqueId = flowExecution.getUniqueId();
        this.name = flowExecution.getFlowDefinition().getName();
        this.result =  flowExecution.getFlowExecutionResult();
        outputs=new HashMap<>();
        initialFormalOutputs(flowExecution);
    }

    public void initialFormalOutputs(FlowExecutionImpl flowExecution)
    {
        for (String outputName: flowExecution.getFlowDefinition().getFlowFormalOutputs())
        {
            DataInFlowExecution outputExecution = flowExecution.getDataValues().get(outputName);
            String stepName =  flowExecution.getFlowDefinition().getStepByFinalOutputName(outputName);
            StepResult stepResult = flowExecution.getStepExecutionDataByStepName(stepName);
            if(outputExecution == null || stepResult == StepResult.FAILURE ) {
                String contentOutputNotCreated = "Not created due to failure in flow";
                DataInFlow output = flowExecution.getFlowDefinition().getDataByFinalOutputName(outputName);
                Data data = new Data(contentOutputNotCreated.getClass(), contentOutputNotCreated, output.getUserString());
                outputs.put(outputName, data);
            }
            else {
                Data data = new Data(outputExecution.getItem().getClass(), outputExecution.getItem(), outputExecution.getDataDefinitionInFlow().getUserString());
                outputs.put(outputName, data);
            }
        }
    }
    public UUID getIdentity() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public FlowExecutionResult getResult() {
        return result;
    }
    public Map<String, Data> getOutputs() {
        return outputs;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }
}