package clientComponents.screenTwo.screenTwoDetails.flowTree;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import stepper.systemEngine.SystemEngineInterface;
import util.Constants;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Logic {

    private TimerTask currentRunningTask;
    private FlowTreeController controller;
    private Timer timer;
    private SimpleBooleanProperty isTaskFinished; // Add a reference to the isTaskFinished property

    public Logic(FlowTreeController controller) {
        this.controller = controller;
        this.isTaskFinished = new SimpleBooleanProperty(false); // Initialize the isTaskFinished property
    }

    public void fetchData(UUID flowId, UIAdapter uiAdapter, SimpleBooleanProperty isTaskFinished, SimpleStringProperty currentFlowId) {
        this.isTaskFinished = isTaskFinished; // Update the reference to the isTaskFinished property
        currentRunningTask = new FlowExecutionTask(flowId, uiAdapter, currentFlowId, this.isTaskFinished); // Pass the reference to the ExecuteFlowTask
        timer = new Timer();
        new Thread(() -> timer.schedule(currentRunningTask, Constants.REFRESH_RATE1, Constants.REFRESH_RATE1)).start();

    }



}
