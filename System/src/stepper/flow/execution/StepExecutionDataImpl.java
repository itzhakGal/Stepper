package stepper.flow.execution;

import stepper.flow.execution.context.DataInFlowExecution;
import stepper.step.api.LoggerImpl;
import stepper.step.api.StepResult;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StepExecutionDataImpl implements StepExecutionData{

    private String finalName;
    private List<LoggerImpl> loggers;
    private String summaryLine;
    private Duration totalTimeStep;
    private LocalTime startTime;
    private LocalTime endTime;
    private StepResult result;

    private List<DataInFlowExecution> inputsData;

    private List<DataInFlowExecution> outputsData;


    public StepExecutionDataImpl(String name)
    {
        this.loggers = new ArrayList<>();
        this.summaryLine = "";
        this.totalTimeStep = Duration.ofNanos(0);
        this.finalName = name;
        this.result= null;
        this.startTime = null;
        this.endTime = null;
        this.inputsData = new ArrayList<>();
        this.outputsData = new ArrayList<>();

    }
    @Override
    public String getSummaryLine() {
        return summaryLine;
    }
    @Override
    public Duration getTotalTimeStep() {
        return totalTimeStep;
    }
    @Override
    public List<LoggerImpl> getLoggers() {
        return loggers;
    }
    @Override
    public void setLoggers(List<LoggerImpl> loggers) {
        this.loggers = loggers;
    }
    @Override
    public void setTotalTimeStep(Duration totalTimeStep) {
        this.totalTimeStep = totalTimeStep;
    }
    @Override
    public void setSummaryLine(String summaryLine) {
        this.summaryLine = summaryLine;
    }
    @Override
    public String getFinalName() {
        return finalName;
    }
    @Override
    public StepResult getResult() {
        return result;
    }
    @Override
    public void setResult(StepResult result) {
        this.result = result;
    }

    @Override
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public List<DataInFlowExecution> getInputsData() {
        return inputsData;
    }

    public List<DataInFlowExecution> getOutputsData() {
        return outputsData;
    }
}

