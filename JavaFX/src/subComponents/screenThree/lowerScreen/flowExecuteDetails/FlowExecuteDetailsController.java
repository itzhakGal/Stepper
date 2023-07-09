package subComponents.screenThree.lowerScreen.flowExecuteDetails;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import subComponents.screenThree.lowerScreen.flowExecuteDetails.flowDetails.FlowDetailsController;
import subComponents.screenThree.lowerScreen.flowExecuteDetails.inputDetailsList.InputDetailsListController;
import subComponents.screenThree.lowerScreen.flowExecuteDetails.outputDetailsList.OutputDetailsListController;
import utils.DTOFullDetailsPastRun;

public class FlowExecuteDetailsController {

    private FlowExecutionHistoryController mainFlowExecutionHistoryController;
    private SystemEngineInterface systemEngine;

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

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
        outputDetailsComponentController.setSystemEngine(systemEngine);
        inputsDetailsComponentController.setSystemEngine(systemEngine);
        detailsGridPaneComponentController.setSystemEngine(systemEngine);
    }

    public void setMainController(FlowExecutionHistoryController mainFlowExecutionHistoryController) {
        this.mainFlowExecutionHistoryController = mainFlowExecutionHistoryController;

    }

    public void setFlowName(String flowName) {
        mainFlowExecutionHistoryController.setFlowName(flowName);
    }

    public void setFlowData(DTOFullDetailsPastRun executedData) {
        detailsGridPaneComponentController.updateDetailsFlowRun(executedData);
        inputsDetailsComponentController.updateDetailsFlowRun(executedData);
        outputDetailsComponentController.updateDetailsFlowRun(executedData);
    }
}
