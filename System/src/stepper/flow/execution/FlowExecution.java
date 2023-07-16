package stepper.flow.execution;

import stepper.flow.definition.api.DataInFlow;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.step.api.StepResult;
import stepper.users.User;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FlowExecution extends Serializable
{
    void initialStepsData(FlowDefinition flowDefinition);
    void initialMapFreeInputsExists(Map<String, DataInFlow> freeInputs);
    List<StepExecutionDataImpl> getStepsData();

    User getUserExecute();

    Map<String, DataInFlowExecution> getFreeInputsExist();
    UUID getUniqueId();
    FlowDefinition getFlowDefinition();
    FlowExecutionResult getFlowExecutionResult();
    void setFlowExecutionResult(FlowExecutionResult flowExecutionResult);
    Duration getTotalTime();
    LocalDateTime getActivationDate();
    long getEndTime();
    long getStartTime();
    void setEndTime(long endTime);
    void setStartTime(long startTime);
    void setActivationDate(LocalDateTime activationDate);
    void storeTotalTime(long startTime);
    List<StepExecutionDataImpl> getStepData();
    Map<String, DataInFlowExecution> getDataValues();
    StepResult getStepExecutionDataByStepName(String stepName);
    StepUsageDeclaration getStepUsageDeclartionByStepName(String finalStepName);
    List<StepExecutionDataImpl> getStepsDataJavaFX();

    void updateInitialValues();

}
