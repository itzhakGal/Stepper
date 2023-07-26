package clientComponents.screenThree.lowerScreen.stepListDetails;

import clientComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import stepper.step.api.LoggerImpl;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenThree.lowerScreen.stepListDetails.inputDetailsList.InputDetailsListController;
import clientComponents.screenThree.lowerScreen.stepListDetails.logDetails.LogDetailsController;
import clientComponents.screenThree.lowerScreen.stepListDetails.outputDetailsList.OutputDetailsListController;
import utilWebApp.DTOStepFlowPastWeb;
import utils.DTOStepFlowPast;

import java.io.IOException;


public class StepListDetailsController {

   private clientComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController mainFlowExecutionHistoryController;
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

    public void setMainController(FlowExecutionHistoryController mainFlowExecutionHistoryController) {
        this.mainFlowExecutionHistoryController = mainFlowExecutionHistoryController;
    }

    public void updateDetailsFlowRun(DTOStepFlowPastWeb stepDetails) {

        startTime.setText(stepDetails.getStartTime());
        endTime.setText(stepDetails.getEndTime());
        durationTime.setText(stepDetails.getDuration().toMillis() + "");
        stepResult.setText(stepDetails.getStepResult().getDescription());
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
