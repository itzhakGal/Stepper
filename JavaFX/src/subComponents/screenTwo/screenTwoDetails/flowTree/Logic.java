package subComponents.screenTwo.screenTwoDetails.flowTree;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import stepper.systemEngine.SystemEngineInterface;

import java.util.UUID;

public class Logic {
    private Task<Boolean> currentRunningTask;
    private FlowTreeController controller;

    public Logic(FlowTreeController controller) {
        this.controller = controller;
    }

    public void fetchData(UUID flowId, UIAdapter uiAdapter, SimpleBooleanProperty onFinish, SimpleStringProperty currentFlowId, SystemEngineInterface systemEngineInterface) {
        currentRunningTask = new FlowExecutionTask(flowId, uiAdapter, onFinish, currentFlowId, systemEngineInterface);
        ProgressBar progressBar = controller.mainFlowsExecutionScreenController.getProgressBar();

        // Add a ChangeListener to the task's progress property
        currentRunningTask.progressProperty().addListener((obs, oldProgress, newProgress) -> {
            // Update the progress of the ProgressBar manually
            progressBar.setProgress(newProgress.doubleValue());
        });
        new Thread(currentRunningTask).start();
    }
}
