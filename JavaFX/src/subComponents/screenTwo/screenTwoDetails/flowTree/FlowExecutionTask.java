package subComponents.screenTwo.screenTwoDetails.flowTree;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import stepper.systemEngine.SystemEngineInterface;
import utils.DTOFullDetailsPastRun;
import utils.DTOStepFlowPast;

import java.util.List;
import java.util.UUID;

public class FlowExecutionTask extends Task<Boolean> {
    private final SystemEngineInterface systemEngine;
    private final UIAdapter uiAdapter;
    private final UUID flowId;
    private final SimpleStringProperty currentFlowId;
    private final SimpleBooleanProperty onFinish;
    private SimpleDoubleProperty progress;

    public FlowExecutionTask(UUID flowId, UIAdapter uiAdapter, SimpleBooleanProperty onFinish, SimpleStringProperty currentFlowId, SystemEngineInterface systemEngineInterface) {
        this.flowId = flowId;
        this.uiAdapter = uiAdapter;
        this.onFinish = onFinish;
        this.currentFlowId = currentFlowId;
        this.systemEngine = systemEngineInterface;
        this.progress = new SimpleDoubleProperty(0.0);
    }

   /* @Override
    protected Boolean call() {
        int SLEEP_TIME = 100;
        DTOFullDetailsPastRun executedData = systemEngine.getFlowExecutedDataDTO(this.flowId);
        uiAdapter.update(executedData);

        while (executedData.getFinalResult() == null) {
            executedData = systemEngine.getFlowExecutedDataDTO(this.flowId);
            if (currentFlowId.getValue().equals(flowId.toString()))
                uiAdapter.update(executedData);

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ignored) {}
        }

     //   systemEngine.setStats(flowId);
        onFinish.setValue(true);
        return Boolean.TRUE;
    }*/

    @Override
    protected Boolean call() {
        int SLEEP_TIME = 100;
        DTOFullDetailsPastRun executedData = systemEngine.getFlowExecutedDataDTO(this.flowId);
        uiAdapter.update(executedData);

        while (executedData.getFinalResult() == null) {
            executedData = systemEngine.getFlowExecutedDataDTO(this.flowId);
            if (currentFlowId.getValue().equals(flowId.toString())) {
                uiAdapter.update(executedData);
                // Calculate and update the progress based on your logic
                double progressValue = calculateProgress(executedData);  // Implement this method
                progress.set(progressValue);
            }

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ignored) {}
        }
        onFinish.setValue(true);
        return Boolean.TRUE;
    }

    public SimpleDoubleProperty getProgressBar()
    {
        return progress;
    }

    private double calculateProgress(DTOFullDetailsPastRun executedData) {
        // Assuming DTOFullDetailsPastRun has a list of members
        List<DTOStepFlowPast> members = executedData.getSteps();

        if (members.isEmpty()) {
            return 0.0; // No progress if there are no members
        }

        int completedCount = 0;

        for (DTOStepFlowPast member : members) {
            if (member.getStepResult() != null) {
                completedCount++;
            }
        }
        double progressValue = (double) completedCount / members.size();
        return progressValue;
    }
}



