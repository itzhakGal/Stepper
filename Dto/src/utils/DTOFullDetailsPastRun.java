package utils;

import stepper.flow.definition.api.DataInFlow;
import stepper.flow.definition.api.DataKind;
import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.execution.FlowExecution;
import stepper.flow.execution.FlowExecutionImpl;
import stepper.flow.execution.FlowExecutionResult;
import stepper.flow.execution.StepExecutionDataImpl;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOStepFlowPastWeb;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DTOFullDetailsPastRun
{
    private String flowName;
    private UUID uniqueId;
    private FlowExecutionResult finalResult;
    private Duration totalTime;
    private LocalDateTime activationDate;
    private List<DTOInputFlowPast> inputs;
    private List<DTOOutPutFlowPast> outputs;
    private List<DTOStepFlowPast> steps;

    public DTOFullDetailsPastRun(FlowExecution flowExecution) {
        this.flowName = flowExecution.getFlowDefinition().getName();
        this.uniqueId = flowExecution.getUniqueId();
        this.finalResult=flowExecution.getFlowExecutionResult();
        this.totalTime = flowExecution.getTotalTime();
        this.activationDate = flowExecution.getActivationDate();
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.steps = new ArrayList<>();
        initialListData(flowExecution);
    }

    public void initialListData(FlowExecution flowExecution)
    {
        List <String> outputsNameList = new ArrayList<>();
        for (StepExecutionDataImpl step : flowExecution.getStepsDataJavaFX()){
            if((!flowExecution.getStepUsageDeclartionByStepName(step.getFinalName()).skipIfFail()) && (step.getResult() == StepResult.FAILURE)) {
                steps.add(new DTOStepFlowPast(step));
                break;
            }else
                steps.add(new DTOStepFlowPast(step));
        }

        for (Map.Entry<String, DataInFlowExecution> entry : flowExecution.getFreeInputsExist().entrySet()) {
            if (entry.getValue().getDataDefinitionInFlow().getNecessity() == DataNecessity.MANDATORY)
                inputs.add(new DTOInputFlowPast(entry.getValue()));
        }
        for (Map.Entry<String, DataInFlowExecution> entry : flowExecution.getFreeInputsExist().entrySet()) {
            if (entry.getValue().getDataDefinitionInFlow().getNecessity() == DataNecessity.OPTIONAL)
                inputs.add(new DTOInputFlowPast(entry.getValue()));
        }
        for (Map.Entry<String, DataInFlowExecution> entry : flowExecution.getDataValues().entrySet()) {
            if (entry.getValue().getDataDefinitionInFlow().getDataKind() == DataKind.OUTPUT) {
                outputs.add(new DTOOutPutFlowPast(entry.getValue()));
                outputsNameList.add(entry.getValue().getDataDefinitionInFlow().getFinalName());
            }
       }
        for (StepUsageDeclaration step : flowExecution.getFlowDefinition().getSteps()) {
            for (Map.Entry<String, DataInFlow> entry : flowExecution.getFlowDefinition().getStepOutput().get(step.getFinalStepName()).entrySet()) {
                StepResult stepResult = flowExecution.getStepExecutionDataByStepName(step.getFinalStepName());
                 if((flowExecution.getDataValues().get(entry.getValue().getFinalName()) == null || stepResult== StepResult.FAILURE ) && (!outputsNameList.contains(entry.getValue().getFinalName())) )
                 {
                    DataInFlowExecution dataOutput = new DataInFlowExecutionImp(entry.getValue());
                    dataOutput.setItem("Not created due to failure in flow");
                    outputs.add(new DTOOutPutFlowPast(dataOutput));
                 }
            }
        }
    }

    // Getter methods
    public String getFlowName() {
        return flowName;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getActivationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return activationDate.format(formatter);
    }

    public FlowExecutionResult getFinalResult() {
        return finalResult;
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public List<DTOInputFlowPast> getInputs() {
        return inputs;
    }

    public List<DTOOutPutFlowPast> getOutputs() {
        return outputs;
    }

    public List<DTOStepFlowPast> getSteps() {
        return steps;
    }
}
