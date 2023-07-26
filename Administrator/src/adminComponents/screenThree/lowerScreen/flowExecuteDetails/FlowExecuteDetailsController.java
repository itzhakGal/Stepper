package adminComponents.screenThree.lowerScreen.flowExecuteDetails;

import adminComponents.screenThree.lowerScreen.flowExecuteDetails.inputDetailsList.InputDetailsListController;
import adminComponents.screenThree.lowerScreen.flowExecuteDetails.outputDetailsList.OutputDetailsListController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import stepper.systemEngine.SystemEngineInterface;
import adminComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import adminComponents.screenThree.lowerScreen.flowExecuteDetails.flowDetails.FlowDetailsController;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utils.DTOFullDetailsPastRun;

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


    public void setMainController(FlowExecutionHistoryController mainFlowExecutionHistoryController) {
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
