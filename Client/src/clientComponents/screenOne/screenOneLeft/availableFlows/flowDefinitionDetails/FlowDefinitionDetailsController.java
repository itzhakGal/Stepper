package clientComponents.screenOne.screenOneLeft.availableFlows.flowDefinitionDetails;
import clientComponents.screenOne.screenOneLeft.availableFlows.AvailableFlowsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import utilsDesktopApp.DTOFlowDetails;

public class FlowDefinitionDetailsController {

    private clientComponents.screenOne.screenOneLeft.availableFlows.AvailableFlowsController mainAvailableFlowsController;
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

    public void setFlowData(DTOFlowDetails flowDetails)
    {
        flowNameButton.setText(flowDetails.getName());
        descriptionButton.setText(flowDetails.getDescription());
        amountOfStepsButton.setText(String.valueOf(flowDetails.getAmountOfSteps()));
        amountOfInputButton.setText(String.valueOf(flowDetails.getAmountOfInput()));
        amountOfContinuationButton.setText(String.valueOf(flowDetails.getAmountOfContinuation()));
    }


}
