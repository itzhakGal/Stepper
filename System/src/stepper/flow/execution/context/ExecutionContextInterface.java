package stepper.flow.execution.context;

import stepper.flow.execution.FlowExecutionImpl;
import stepper.flow.execution.StepExecutionDataImpl;
import stepper.step.api.StepDefinition;
import stepper.step.api.StepResult;

import java.io.Serializable;
import java.time.LocalTime;

public interface ExecutionContextInterface extends Serializable {
    <T> T getDataValue(String dataName, Class<T> expectedDataType, StepDefinition stepDefinition);
    boolean storeDataValue(String dataName, DataInFlowExecution value);
    void storeLogsData(String dataLog);
    void storeTotalTimeStep(LocalTime localStartTime, LocalTime localEndTime, long startTime);
    void updateLogDataAndSummeryLine(String str);
    FlowExecutionImpl getFlowExecution();
    String getStepName();
    void setStepName(String stepName);
    StepExecutionDataImpl convertFromNameStepExecutionData();
    void updateStatusStep(StepResult stepResult);
    void updateOutputDataList (DataInFlowExecution outputData);
}
