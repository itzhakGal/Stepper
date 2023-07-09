package subComponents.screenTwo.screenTwoDetails.stepListDetails.logDetails;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import stepper.step.api.LoggerImpl;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenTwo.screenTwoDetails.stepListDetails.StepListDetailsController;

public class LogDetailsController {

    private SystemEngineInterface systemEngine;

    private StepListDetailsController mainStepListDetailsController;
    @FXML
    private TextArea logDetailsTextArea;
    @FXML
    private Label logTimeLabel;

    private SimpleStringProperty logDetailsTextAreaProperty;
    private SimpleObjectProperty logTimeLabelProperty;

    public LogDetailsController() {
        logDetailsTextAreaProperty =  new SimpleStringProperty();
        logTimeLabelProperty =  new SimpleObjectProperty();
    }

    @FXML
    private void initialize() {
        logDetailsTextArea.textProperty().bind(logDetailsTextAreaProperty);
        logTimeLabel.textProperty().bind(logTimeLabelProperty);
    }

    public void setMainController(StepListDetailsController mainStepListDetailsController) {
        this.mainStepListDetailsController = mainStepListDetailsController;
    }

    public void updateDetails(LoggerImpl log)
    {
        logDetailsTextAreaProperty.set(log.getLog());
        logTimeLabelProperty.set(log.getLogTimeAsString());
    }
    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
    }
}
