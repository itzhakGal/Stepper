package stepper.flow.execution;

import stepper.step.api.LoggerImpl;
import stepper.step.api.StepResult;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public interface StepExecutionData extends Serializable {
     String getSummaryLine();
     Duration getTotalTimeStep();
     List<LoggerImpl> getLoggers();
     void setLoggers(List<LoggerImpl> loggers);
     void setTotalTimeStep(Duration totalTimeStep);
     void setSummaryLine(String summaryLine);
     String getFinalName();
     StepResult getResult();
     void setResult(StepResult result);
     public void setStartTime(LocalTime startTime);

     public void setEndTime(LocalTime endTime);
}
