package clientComponents.screenThree.lowerScreen.stepListDetails.logDetails;

import clientComponents.screenThree.lowerScreen.stepListDetails.StepListDetailsController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import stepper.step.api.LoggerImpl;
import stepper.systemEngine.SystemEngineInterface;

public class LogDetailsController {

    private clientComponents.screenThree.lowerScreen.stepListDetails.StepListDetailsController mainStepListDetailsController;
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
}
