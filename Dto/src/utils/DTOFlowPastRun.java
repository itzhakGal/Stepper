package utils;

import stepper.flow.execution.FlowExecutionImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class DTOFlowPastRun {

    private String flowName;
    private UUID uniqueId;
    private LocalDateTime activationDate;

    public DTOFlowPastRun(FlowExecutionImpl flowExecution) {
        this.flowName = flowExecution.getFlowDefinition().getName();
        this.uniqueId = flowExecution.getUniqueId();
        this.activationDate = flowExecution.getActivationDate();
        }

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
}
