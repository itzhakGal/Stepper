package clientComponents.screenThree.lowerScreen.flowExecuteDetails;

import clientComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenThree.lowerScreen.flowExecuteDetails.flowDetails.FlowDetailsController;
import clientComponents.screenThree.lowerScreen.flowExecuteDetails.inputDetailsList.InputDetailsListController;
import clientComponents.screenThree.lowerScreen.flowExecuteDetails.outputDetailsList.OutputDetailsListController;
import utilWebApp.DTOFullDetailsPastRunWeb;

public class FlowExecuteDetailsController {

    private FlowExecutionHistoryController mainFlowExecutionHistoryController;
    @FXML
    private GridPane  detailsGridPaneComponent;
    @FXML
    private FlowDetailsController detailsGridPaneComponentController;
    @FXML
    private BorderPane inputsDetailsComponent;
    @FXML
    private InputDetailsListController inputsDetailsComponentController;
    @FXML
    private BorderPane outputDetailsComponent;
    @FXML
    private OutputDetailsListController outputDetailsComponentController;

    @FXML
    public void initialize() {
        if (outputDetailsComponentController != null && inputsDetailsComponentController != null && detailsGridPaneComponentController !=null) {
            outputDetailsComponentController.setMainController(this);
            inputsDetailsComponentController.setMainController(this);
            detailsGridPaneComponentController.setMainController(this);
        }
    }

    public void setMainController(clientComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController mainFlowExecutionHistoryController) {
        this.mainFlowExecutionHistoryController = mainFlowExecutionHistoryController;
    }

    public void setFlowName(String flowName) {
        mainFlowExecutionHistoryController.setFlowName(flowName);
    }

    public void setFlowData(DTOFullDetailsPastRunWeb executedData) {
        detailsGridPaneComponentController.updateDetailsFlowRun(executedData);
        inputsDetailsComponentController.updateDetailsFlowRun(executedData);
        outputDetailsComponentController.updateDetailsFlowRun(executedData);
    }
}
