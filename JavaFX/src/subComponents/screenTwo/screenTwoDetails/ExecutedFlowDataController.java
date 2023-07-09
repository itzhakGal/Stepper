package subComponents.screenTwo.screenTwoDetails;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import stepper.step.api.StepResult;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.mainScreen.body.BodyController;
import subComponents.screenTwo.screenTwoDetails.flowExecuteDetails.FlowExecuteDetailsController;
import subComponents.screenTwo.screenTwoDetails.stepListDetails.StepListDetailsController;
import utils.DTOFullDetailsPastRun;
import utils.DTOStepFlowPast;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


public class ExecutedFlowDataController {
    private SystemEngineInterface systemEngine;

    private BodyController bodyController;
    private final SimpleBooleanProperty isTaskFinished;
    private boolean isActive;

    public ExecutedFlowDataController() {
        this.isTaskFinished = new SimpleBooleanProperty(false);
    }

    public void init(BodyController bodyController) {
        this.isTaskFinished.set(false);
        this.bodyController = bodyController;


            bodyController.getFlowExecutionScreenComponentController().getContinuation().getContinuationButtonPressed()
                    .addListener((observable, oldValue, newValue) -> {
                        if (newValue)
                            bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
                    });

            bodyController.getFlowExecutionScreenComponentController().getFlowTreeComponentController().getSelectedItem()
                    .addListener((observable, oldValue, newValue) -> {
                        bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
                        showExecutedFlowData();
                    });

            bodyController.getFlowDefinitionScreenComponentController().getIsExecuteFlowButtonClicked()
                    .addListener((observable, oldValue, newValue) -> {
                        bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
                    });
            bodyController.getFlowExecutionScreenComponentController().getRerunButtonProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
                    });

        bodyController.isFlowDefinitionButtonProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
            }
        });

        bodyController.getFlowExecutionHistoryScreenComponentController().getFlowExecutionTableComponentController().getRerunFlowButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
                    }
                });
    }



    private void showExecutedFlowData() {
        TreeItem<String> selectedTreeItemItem;
        DTOFullDetailsPastRun executedData;
        StepResult stepResult;

        selectedTreeItemItem = bodyController.getFlowExecutionScreenComponentController().getFlowTreeComponentController().getSelectedItem().get();
        executedData = systemEngine.getFlowExecutedDataDTO(
                UUID.fromString(bodyController.getFlowExecutionScreenComponentController().getExecutedFlowID().getValue()));
        boolean isRootSelected = (selectedTreeItemItem != null && selectedTreeItemItem.getParent() == null);


        if (isRootSelected) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flowExecuteDetails/flowExecuteDetails.fxml"));
                //ScrollPane contentPane = fxmlLoader.load();
                HBox contentPane = fxmlLoader.load();
                FlowExecuteDetailsController controller = fxmlLoader.getController();
                controller.setMainController(bodyController.getFlowExecutionScreenComponentController());
                controller.setSystemEngine(systemEngine);

                bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().add(contentPane);

                // Store the controller in the TitledPane's properties
                controller.setFlowData(executedData);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stepListDetails/stepListDetails.fxml"));
                HBox contentPane = fxmlLoader.load();

                StepListDetailsController controller = fxmlLoader.getController();
                controller.setMainController(bodyController.getFlowExecutionScreenComponentController());
                controller.setSystemEngine(systemEngine);

                bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().add(contentPane);

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