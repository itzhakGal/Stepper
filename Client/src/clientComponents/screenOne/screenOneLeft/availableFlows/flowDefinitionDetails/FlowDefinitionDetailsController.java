package clientComponents.screenOne.screenOneLeft.availableFlows.flowDefinitionDetails;
import clientComponents.screenOne.screenOneLeft.availableFlows.AvailableFlowsController;
import clientComponents.screenOne.screenOneLeft.availableFlows.FlowDefinitionRefresher;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import stepper.systemEngine.SystemEngineInterface;
import utilsDesktopApp.DTOFlowDetails;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FlowDefinitionDetailsController {

    private clientComponents.screenOne.screenOneLeft.availableFlows.AvailableFlowsController mainAvailableFlowsController;
    //private SystemEngineInterface systemEngine;
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
        //this.systemEngine = systemEngine;
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
