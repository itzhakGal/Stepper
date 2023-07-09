package subComponents.screenOne.screenOneLeft.availableFlows.flowDefinitionDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenOne.screenOneLeft.availableFlows.AvailableFlowsController;
import utilsDesktopApp.DTOFlowDetails;

public class FlowDefinitionDetailsController {

    private AvailableFlowsController mainAvailableFlowsController;
    private SystemEngineInterface systemEngine;
    @FXML
    private Label flowNameButton;

    @FXML
    private TextArea descriptionButton;

    @FXML
    private Label amountOfStepsButton;

    @FXML
    private Label amountOfInputButton;

    @FXML
    private Label amountOfContinuationButton;

    @FXML
    private Button showFlowDetailsButton;

    @FXML
    void showFlowDetailsButtonAction(ActionEvent event) {
          mainAvailableFlowsController.setFlowSelectedDetails(flowNameButton.getText());
    }

    public void setMainController(AvailableFlowsController availableFlowsController) {
        this.mainAvailableFlowsController = availableFlowsController;
    }
    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
    }

    public void setFlowData(DTOFlowDetails flowDetails)
    {
        flowNameButton.setText(flowDetails.getName());
        descriptionButton.setText(flowDetails.getDescription());
        amountOfStepsButton.setText(String.valueOf(flowDetails.getAmountOfSteps()));
        amountOfInputButton.setText(String.valueOf(flowDetails.getAmountOfInput()));
        amountOfContinuationButton.setText(String.valueOf(flowDetails.getAmountOfContinuation()));
    }
}
