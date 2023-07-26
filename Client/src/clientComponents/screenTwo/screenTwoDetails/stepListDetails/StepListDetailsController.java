package clientComponents.screenTwo.screenTwoDetails.stepListDetails;

import clientComponents.screenTwo.FlowsExecutionScreenController;
import clientComponents.screenTwo.screenTwoDetails.stepListDetails.inputDetailsList.InputDetailsListController;
import clientComponents.screenTwo.screenTwoDetails.stepListDetails.logDetails.LogDetailsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import stepper.step.api.LoggerImpl;
import stepper.systemEngine.SystemEngineInterface;

import java.io.IOException;

import clientComponents.screenTwo.screenTwoDetails.stepListDetails.outputDetailsList.OutputDetailsListController;
import utilWebApp.DTOStepFlowPastWeb;
import utils.DTOStepFlowPast;


public class StepListDetailsController {

   private clientComponents.screenTwo.FlowsExecutionScreenController mainFlowsExecutionScreenController;
    @FXML
    private BorderPane outputListComponent;
    @FXML
    private OutputDetailsListController outputListComponentController;
    @FXML
    private BorderPane inputListComponent;
    @FXML
    private InputDetailsListController inputListComponentController;
    @FXML
    private GridPane logDetailsGridPane;
    @FXML
    private Label startTime;
    @FXML
    private Label endTime;
    @FXML
    private Label durationTime;
    @FXML
    private Label stepResult;
    @FXML
    private Label logs;

    @FXML
    private void initialize() {

    }

    public void setMainController(FlowsExecutionScreenController mainFlowsExecutionScreenController) {
        this.mainFlowsExecutionScreenController = mainFlowsExecutionScreenController;
    }

    public void updateDetailsFlowRun(DTOStepFlowPastWeb stepDetails) {
        startTime.setText(stepDetails.getStartTime() != null ? stepDetails.getStartTime() : "Still in progress");
        endTime.setText(stepDetails.getEndTime() != null ? stepDetails.getEndTime() : "Still in progress");
        durationTime.setText(stepDetails.getDuration() != null ? stepDetails.getDuration().toMillis() + "" : "Still in progress");
        stepResult.setText(stepDetails.getStepResult() != null ? stepDetails.getStepResult().getDescription() : "Still in progress");
        updateLogDetailsGridPane(stepDetails);
        outputListComponentController.updateOutputsStepDetails(stepDetails.getDtoOutputDetails());
        inputListComponentController.updateInputsStepDetails(stepDetails.getDtoInputsDetails());

    }

    public void updateLogDetailsGridPane(DTOStepFlowPastWeb stepDetails)
    {
        logDetailsGridPane.getChildren().clear();
        logDetailsGridPane.getRowConstraints().clear();
        logDetailsGridPane.getColumnConstraints().clear();
        int row =0;
        for(LoggerImpl log :stepDetails.getLogs())
        {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("logDetails/logDetails.fxml"));
                GridPane content = fxmlLoader.load();
                LogDetailsController controller = fxmlLoader.getController();
                // Store the controller in the TitledPane's properties
                content.getProperties().put("controller", controller);
                controller.updateDetails(log);
                controller.setMainController(this);
                //controller.setSystemEngine(systemEngine);
                logDetailsGridPane.addRow(row, content);
                row++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
