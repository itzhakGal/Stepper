package utilWebApp;

import stepper.flow.definition.api.DataInFlow;
import stepper.flow.definition.api.DataKind;
import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.execution.FlowExecution;
import stepper.flow.execution.FlowExecutionResult;
import stepper.flow.execution.StepExecutionDataImpl;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;
import utils.DTOInputFlowPast;
import utils.DTOOutPutFlowPast;
import utils.DTOStepFlowPast;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DTOFullDetailsPastRunWeb
{
    private String flowName;
    private UUID uniqueId;
    private FlowExecutionResult finalResult;
    private Duration totalTime;
    private LocalDateTime activationDate;
    private List<DTOInputFlowPastWeb> inputs;
    private List<DTOOutPutFlowPastWeb> outputs;
    private List<DTOStepFlowPastWeb> steps;
    private String userName;

    public DTOFullDetailsPastRunWeb(FlowExecution flowExecution) {
        this.flowName = flowExecution.getFlowDefinition().getName();
        this.uniqueId = flowExecution.getUniqueId();
        this.finalResult=flowExecution.getFlowExecutionResult();
        this.totalTime = flowExecution.getTotalTime();
        this.activationDate = flowExecution.getActivationDate();
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.steps = new ArrayList<>();
        this.userName = flowExecution.getUserExecute().getUserName();
        initialListData(flowExecution);
    }

    public void initialListData(FlowExecution flowExecution)
    {
        List <String> outputsNameList = new ArrayList<>();
        for (StepExecutionDataImpl step : flowExecution.getStepsDataJavaFX()){
            if((!flowExecution.getStepUsageDeclartionByStepName(step.getFinalName()).skipIfFail()) && (step.getResult() == StepResult.FAILURE)) {
                steps.add(new DTOStepFlowPastWeb(step));
                break;
            }else
                steps.add(new DTOStepFlowPastWeb(step));
        }

        for (Map.Entry<String, DataInFlowExecution> entry : flowExecution.getFreeInputsExist().entrySet()) {
            if (entry.getValue().getDataDefinitionInFlow().getNecessity() == DataNecessity.MANDATORY)
                inputs.add(new DTOInputFlowPastWeb(entry.getValue()));
        }
        for (Map.Entry<String, DataInFlowExecution> entry : flowExecution.getFreeInputsExist().entrySet()) {
            if (entry.getValue().getDataDefinitionInFlow().getNecessity() == DataNecessity.OPTIONAL)
                inputs.add(new DTOInputFlowPastWeb(entry.getValue()));
        }
        for (Map.Entry<String, DataInFlowExecution> entry : flowExecution.getDataValues().entrySet()) {
            if (entry.getValue().getDataDefinitionInFlow().getDataKind() == DataKind.OUTPUT) {
                outputs.add(new DTOOutPutFlowPastWeb(entry.getValue()));
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
                    outputs.add(new DTOOutPutFlowPastWeb(dataOutput));
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

    public List<DTOInputFlowPastWeb> getInputs() {
        return inputs;
    }

    public List<DTOOutPutFlowPastWeb> getOutputs() {
        return outputs;
    }

    public List<DTOStepFlowPastWeb> getSteps() {
        return steps;
    }

    public void setFinalResult(FlowExecutionResult finalResult) {
        this.finalResult = finalResult;
    }

    public String getUserName() {
        return userName;
    }
}
