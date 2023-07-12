package stepper.flow.execution.context;

import stepper.dataDefinition.api.DataDefinition;
import stepper.exception.DifferentTypeException;
import stepper.flow.execution.FlowExecutionImpl;
import stepper.flow.execution.StepExecutionDataImpl;
import stepper.step.api.LoggerImpl;
import stepper.step.api.StepDefinition;
import stepper.step.api.StepResult;

import java.time.Duration;
import java.time.LocalTime;

public class ExecutionContextImpl implements ExecutionContextInterface {
    private FlowExecutionImpl flowExecution;
    private String finalStepName;

    public ExecutionContextImpl(FlowExecutionImpl flowExecution) {
        this.flowExecution = flowExecution;
        this.finalStepName = null;
    }
    public StepExecutionDataImpl convertFromNameStepExecutionData()
    {
        for(StepExecutionDataImpl dataStep : flowExecution.getStepData())
        {
            if (dataStep.getFinalName().equals(finalStepName))
                return dataStep;
        }
        return null;
    }

    public StepExecutionDataImpl convertFromNameStepExecutionDataJavaFx()
    {
        for(StepExecutionDataImpl dataStep : flowExecution.getStepsDataJavaFX())
        {
            if (dataStep.getFinalName().equals(finalStepName))
                return dataStep;
        }
        return null;
    }

    public StepExecutionDataImpl StepExecutionDataByStepName(String finalName)
    {
        for(StepExecutionDataImpl dataStep : flowExecution.getStepData())
        {
            if (dataStep.getFinalName().equals(finalName))
                return dataStep;
        }
        return null;
    }

    public StepExecutionDataImpl StepExecutionDataByStepNameJavaFX(String finalName)
    {
        for(StepExecutionDataImpl dataStep : flowExecution.getStepsDataJavaFX())
        {
            if (dataStep.getFinalName().equals(finalName))
                return dataStep;
        }
        return null;
    }
    @Override
    public <T> T getDataValue(String originalNameData, Class<T> expectedDataType, StepDefinition stepDefinition) {
        // assuming that from the data name we can get to its data definition
        DataDefinition theExpectedDataDefinition = stepDefinition.getDataDefinitionByName(originalNameData);

        String name = this.getFlowExecution().getFlowDefinition().getStepInputs().get(finalStepName).get(originalNameData).getFinalName();

        if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.getType())) {
           DataInFlowExecution data = flowExecution.getDataValues().get(name);
            if (data != null) {
                updateListInputsStep(finalStepName, data);
                Object aValue = data.getItem();
                return expectedDataType.cast(aValue);
            }else
                return null;
        } else {
            throw new DifferentTypeException();
        }
    }
    private void updateListInputsStep(String finalStepName,  DataInFlowExecution data) {

        StepExecutionDataImpl step = StepExecutionDataByStepName(finalStepName);
        StepExecutionDataImpl stepJavaFX = StepExecutionDataByStepNameJavaFX(finalStepName);
        step.getInputsData().add(data);
        stepJavaFX.getInputsData().add(data);
    }

    @Override
    public boolean storeDataValue(String originalNameData, DataInFlowExecution value) {
        // assuming that from the data name we can get to its data definition
        String finalName;
        DataDefinition theData = value.getDataDefinitionInFlow().getDataDefinition();

        if (finalStepName != null) {
            finalName = this.getFlowExecution().getFlowDefinition().getStepOutput().get(finalStepName).get(originalNameData).getFinalName();
        }
        else
            finalName = value.getDataDefinitionInFlow().getFinalName();

        if (theData.getType().isAssignableFrom(value.getItem().getClass())) {
            value.setFinalName(finalName);
            flowExecution.getDataValues().put(finalName, value);
        } else {
            throw new DifferentTypeException();
        }
        return false;
    }
    @Override
    public void storeLogsData(String dataLog){
        StepExecutionDataImpl stepExecutionData = convertFromNameStepExecutionData();
        StepExecutionDataImpl stepExecutionDataJavaFx = convertFromNameStepExecutionDataJavaFx();
        stepExecutionData.getLoggers().add(new LoggerImpl(dataLog));
        stepExecutionDataJavaFx.getLoggers().add(new LoggerImpl(dataLog));
    }
    @Override
    public void updateLogDataAndSummeryLine(String str)
    {
        StepExecutionDataImpl stepExecutionData = convertFromNameStepExecutionData();
        StepExecutionDataImpl stepExecutionDataJavaFx = convertFromNameStepExecutionDataJavaFx();
        storeLogsData(str);
        stepExecutionData.setSummaryLine(str);
        stepExecutionDataJavaFx.setSummaryLine(str);
    }
    @Override
    public void storeTotalTimeStep(LocalTime localStartTime, LocalTime localEndTime, long startTime)
    {
        StepExecutionDataImpl stepExecutionData = convertFromNameStepExecutionData();
        StepExecutionDataImpl stepExecutionDataJavaFx = convertFromNameStepExecutionDataJavaFx();

        stepExecutionData.setStartTime(localStartTime);
        stepExecutionData.setEndTime(localEndTime);

        stepExecutionDataJavaFx.setStartTime(localStartTime);
        stepExecutionDataJavaFx.setEndTime(localEndTime);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        stepExecutionData.setTotalTimeStep(Duration.ofNanos(elapsedTime));
        stepExecutionDataJavaFx.setTotalTimeStep(Duration.ofNanos(elapsedTime));
    }
    @Override
    public void setStepName(String stepName) {
        this.finalStepName = stepName;
    }
    @Override
    public String getStepName() {
        return finalStepName;
    }
    @Override
    public FlowExecutionImpl getFlowExecution() {
        return flowExecution;
    }
    @Override
    public void updateStatusStep(StepResult stepResult)
    {
        StepExecutionDataImpl stepExecutionDataJavaFx = convertFromNameStepExecutionDataJavaFx();
        StepExecutionDataImpl stepExecutionData = convertFromNameStepExecutionData();
        stepExecutionData.setResult(stepResult);
        stepExecutionDataJavaFx.setResult(stepResult);
    }

    @Override
    public void updateOutputDataList (DataInFlowExecution outputData)
    {
        StepExecutionDataImpl stepExecutionData = convertFromNameStepExecutionData();
        StepExecutionDataImpl stepExecutionDataJavaFx = convertFromNameStepExecutionDataJavaFx();
        stepExecutionData.getOutputsData().add(outputData);
        stepExecutionDataJavaFx.getOutputsData().add(outputData);
    }



}
