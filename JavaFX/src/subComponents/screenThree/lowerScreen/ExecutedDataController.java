package subComponents.screenThree.lowerScreen;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import stepper.step.api.StepResult;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import subComponents.screenThree.lowerScreen.flowExecuteDetails.FlowExecuteDetailsController;
import subComponents.screenThree.lowerScreen.stepListDetails.StepListDetailsController;
import utils.DTOFullDetailsPastRun;
import utils.DTOStepFlowPast;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ExecutedDataController {

    private SystemEngineInterface systemEngine;

    private FlowExecutionHistoryController flowExecutionHistoryController;

    public void init(FlowExecutionHistoryController flowExecutionHistoryController) {
        this.flowExecutionHistoryController = flowExecutionHistoryController;

     /*   bodyController.getFlowExecutionScreenComponentController().getContinuation().getContinuationButtonPressed()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue)
                        bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
                });*/

        this.flowExecutionHistoryController.getFlowTreeComponentController().getSelectedItem()
                .addListener((observable, oldValue, newValue) -> {
                    this.flowExecutionHistoryController.getVboxDetails().getChildren().clear();
                    showExecutedFlowData();
                });

        this.flowExecutionHistoryController.getMainBodyController().isExecutionsHistoryButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        this.flowExecutionHistoryController.getVboxDetails().getChildren().clear();
                    }
                });
        this.flowExecutionHistoryController.getTableFlowExecutionController().getChosenFlowIdProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue.equals("")) {
                        this.flowExecutionHistoryController.getVboxDetails().getChildren().clear();
                    }
                });

    }

    private void showExecutedFlowData() {
        TreeItem<String> selectedTreeItemItem;
        DTOFullDetailsPastRun executedData;
        StepResult stepResult;

        selectedTreeItemItem =  this.flowExecutionHistoryController.getFlowTreeComponentController().getSelectedItem().get();
        executedData = systemEngine.getFlowExecutedDataDTO(UUID.fromString(this.flowExecutionHistoryController.getTableFlowExecutionController().getChosenFlowIdProperty().getValue()));
        boolean isRootSelected = (selectedTreeItemItem != null && selectedTreeItemItem.getParent() == null);


        if (isRootSelected) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flowExecuteDetails/flowExecuteDetails.fxml"));
                VBox contentPane = fxmlLoader.load();
                FlowExecuteDetailsController controller = fxmlLoader.getController();
                controller.setMainController(this.flowExecutionHistoryController);
                controller.setSystemEngine(systemEngine);

                this.flowExecutionHistoryController.getVboxDetails().getChildren().add(contentPane);

                // Store the controller in the TitledPane's properties
                controller.setFlowData(executedData);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stepListDetails/stepListDetails.fxml"));
                ScrollPane contentPane = fxmlLoader.load();

                StepListDetailsController controller = fxmlLoader.getController();
                controller.setMainController(this.flowExecutionHistoryController);
                controller.setSystemEngine(systemEngine);

                this.flowExecutionHistoryController.getVboxDetails().getChildren().add(contentPane);

                // Store the controller in the TitledPane's properties
                DTOStepFlowPast stepDetails = getStepDetails(selectedTreeItemItem.getValue().toString(), executedData.getSteps());
                controller.updateDetailsFlowRun(stepDetails);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public DTOStepFlowPast getStepDetails(String currentSelected, List<DTOStepFlowPast> steps )
    {
        for(DTOStepFlowPast stepDetails : steps)
        {
            if(stepDetails.getFinalStepName().equals(currentSelected))
                return stepDetails;
        }
        return null;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
    }

}
