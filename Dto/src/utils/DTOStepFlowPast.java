package utils;

import stepper.flow.execution.StepExecutionDataImpl;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.step.api.LoggerImpl;
import stepper.step.api.StepResult;
import utilsDesktopApp.DTOInputDetailsJavaFX;
import utilsDesktopApp.DTOOutputDetailsJAVAFX;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DTOStepFlowPast
{
    private String finalStepName;
    private Duration duration;
    private StepResult completionResult;
    private String summaryLine;
    private StepResult stepResult;
    private List<LoggerImpl> logs;

    private LocalTime startTime;
    private LocalTime endTime;

    private List<DTOOutputDetailsJAVAFX> dtoOutputDetails;

    private List<DTOInputDetailsJavaFX> dtoInputsDetails;


    public DTOStepFlowPast(StepExecutionDataImpl step) {
        this.finalStepName = step.getFinalName();
        this.duration = step.getTotalTimeStep();
        this.completionResult = step.getResult();
        this.summaryLine = step.getSummaryLine();
        this.startTime = step.getStartTime();
        this.endTime = step.getEndTime();
        this.logs = new ArrayList<>();
        this.logs = step.getLoggers();
        this.dtoOutputDetails = new ArrayList<>();
        this.dtoInputsDetails = new ArrayList<>();
        this.stepResult = step.getResult();

        initialLists(step);
    }

    private void initialLists(StepExecutionDataImpl step) {
        for (DataInFlowExecution data :step.getOutputsData())
        {
            dtoOutputDetails.add(new DTOOutputDetailsJAVAFX(data));
        }

        for (DataInFlowExecution data :step.getInputsData())
        {
            dtoInputsDetails.add(new DTOInputDetailsJavaFX(data));
        }
    }

    public String getFinalStepName() {
        return finalStepName;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getSummaryLine() {
        return summaryLine;
    }

    public StepResult getCompletionResult() {
        return completionResult;
    }

    public List<LoggerImpl> getLogs() {
        return logs;
    }

    public String getStartTime() {
        if(startTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            return startTime.format(formatter);
        }
        return null;
    }

    public String getEndTime() {
        if(endTime!=null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            return endTime.format(formatter);
        }
        return null;
    }

    public StepResult getStepResult() {
        return stepResult;
    }

    public List<DTOOutputDetailsJAVAFX> getDtoOutputDetails() {
        return dtoOutputDetails;
    }

    public List<DTOInputDetailsJavaFX> getDtoInputsDetails() {
        return dtoInputsDetails;
    }
}
