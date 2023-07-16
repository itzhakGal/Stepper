package stepper.flow.execution;

import stepper.dataDefinition.impl.Enumerator.EnumeratorData;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.InitialInputValue;
import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.step.api.StepResult;
import stepper.users.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FlowExecutionImpl  implements FlowExecution{
    private final UUID uniqueId;
    private User userExecute;
    private final FlowDefinition flowDefinition;
    private Duration totalTime;
    private long startTime;
    private long endTime;
    private FlowExecutionResult flowExecutionResult;
    private LocalDateTime activationDate;
    private Map<String, DataInFlowExecution> dataValues;
    private Map<String, DataInFlowExecution>  freeInputsExist;
    private List<StepExecutionDataImpl> stepsData;
    private List<StepExecutionDataImpl> stepsDataJavaFX;


    public FlowExecutionImpl(FlowDefinition flowDefinition, User user) {
        this.uniqueId = UUID.randomUUID();
        this.flowDefinition = flowDefinition;
        this.startTime = 0;
        this.endTime=0;
        this.dataValues = new HashMap<>();
        this.stepsData = new ArrayList();
        this.freeInputsExist = new HashMap<>();
        this.flowExecutionResult = null;
        this.stepsDataJavaFX = new ArrayList<>();
        this.userExecute = user;
        initialStepsData(flowDefinition);
        initialMapFreeInputsExists(flowDefinition.getFreeInputs());
        updateInitialValues();
    }

    public FlowExecutionImpl(FlowDefinition flowDefinition) {
        this.uniqueId = UUID.randomUUID();
        this.flowDefinition = flowDefinition;
        this.startTime = 0;
        this.endTime=0;
        this.dataValues = new HashMap<>();
        this.stepsData = new ArrayList();
        this.freeInputsExist = new HashMap<>();
        this.flowExecutionResult = null;
        this.stepsDataJavaFX = new ArrayList<>();
        this.userExecute = null;
        initialStepsData(flowDefinition);
        initialMapFreeInputsExists(flowDefinition.getFreeInputs());
        updateInitialValues();
    }

    @Override
    public void updateInitialValues() {
        for(InitialInputValue input : this.getFlowDefinition().getInitialInputValuesList())
        {
            DataInFlow dataInFlow = this.flowDefinition.getDataByFinalInputName(input.getInputName());
            if(dataInFlow.getDataType().equals("EnumeratorData")) {
                updateEnumeratorDataInput(input, dataInFlow);
            }
            else if(dataInFlow.getDataType().equals("Integer"))
            {
                updateNumberInput(input, dataInFlow);
            }
            else{
                DataInFlowExecution dataInFlowExecution = new DataInFlowExecutionImp(dataInFlow);
                dataInFlowExecution.setItem(input.getInitialValue());
                this.getDataValues().put(input.getInputName(), dataInFlowExecution);
            }
            this.getFreeInputsExist().remove(input.getInputName());
        }
    }

    private void updateNumberInput(InitialInputValue input, DataInFlow dataInFlow) {
        Integer numberValue = Integer.parseInt(input.getInitialValue());
        DataInFlowExecution dataInFlowExecution = new DataInFlowExecutionImp(dataInFlow);
        dataInFlowExecution.setItem(numberValue);
        this.getDataValues().put(input.getInputName(), dataInFlowExecution);
    }
    private void updateEnumeratorDataInput(InitialInputValue input, DataInFlow dataInFlow) {
        EnumeratorData enumeratorData = new EnumeratorData();
        enumeratorData.add(input.getInitialValue());
        DataInFlowExecution dataInFlowExecution = new DataInFlowExecutionImp(dataInFlow);
        dataInFlowExecution.setItem(enumeratorData);
        this.getDataValues().put(input.getInputName(), dataInFlowExecution);
    }
    @Override
    public void initialStepsData(FlowDefinition flowDefinition)
    {
        for(StepUsageDeclaration oneStep : flowDefinition.getFlowSteps()){
            this.stepsData.add(new StepExecutionDataImpl(oneStep.getFinalStepName()));
        }
    }
    @Override
    public void initialMapFreeInputsExists(Map<String, DataInFlow> freeInputs) {
        for (Map.Entry<String, DataInFlow> entry : freeInputs.entrySet()) {
            this.freeInputsExist.put(entry.getKey(), new DataInFlowExecutionImp(entry.getValue()));
        }
    }
    @Override
    public List<StepExecutionDataImpl> getStepsData() {
        return stepsData;
    }

    @Override
    public User getUserExecute() {
        return userExecute;
    }

    @Override
    public Map<String, DataInFlowExecution> getFreeInputsExist() {
        return freeInputsExist;
    }
    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }
    @Override
    public FlowDefinition getFlowDefinition() {
        return flowDefinition;
    }
    @Override
    public FlowExecutionResult getFlowExecutionResult() {
        return flowExecutionResult;
    }
    @Override
    public void setFlowExecutionResult(FlowExecutionResult flowExecutionResult) {
        this.flowExecutionResult = flowExecutionResult;
    }
    @Override
    public Duration getTotalTime() {
        return totalTime;
    }
    @Override
    public LocalDateTime getActivationDate() {
        return activationDate;
    }
    @Override
    public long getEndTime() {
        return endTime;
    }
    @Override
    public long getStartTime() {
        return startTime;
    }
    @Override
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    @Override
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    @Override
    public void setActivationDate(LocalDateTime activationDate) {
        this.activationDate = activationDate;
    }
    @Override
    public void storeTotalTime(long startTime)
    {
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        totalTime = Duration.ofNanos(elapsedTime);
    }
    @Override
    public List<StepExecutionDataImpl> getStepData() {
        return stepsData;
    }
    @Override
    public Map<String, DataInFlowExecution> getDataValues() {
        return dataValues;
    }

    @Override
    public StepUsageDeclaration getStepUsageDeclartionByStepName(String finalStepName)
    {
        for(StepUsageDeclaration stepUsageDeclaration : this.getFlowDefinition().getSteps())
            if(stepUsageDeclaration.getFinalStepName().equals(finalStepName))
                return stepUsageDeclaration;


        return null;
    }
    @Override
    public StepResult getStepExecutionDataByStepName(String stepName)
    {
        for(StepExecutionDataImpl stepExecution : stepsData)
            if(stepExecution.getFinalName().equals(stepName))
                return stepExecution.getResult();


        return null;
    }

    public List<StepExecutionDataImpl> getStepsDataJavaFX() {
        return stepsDataJavaFX;
    }

    public void setUserExecute(User userExecute) {
        this.userExecute = userExecute;
    }
}
