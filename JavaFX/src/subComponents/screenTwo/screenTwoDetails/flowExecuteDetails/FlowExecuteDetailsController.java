package subComponents.screenTwo.screenTwoDetails.flowExecuteDetails;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenTwo.FlowsExecutionScreenController;
import subComponents.screenTwo.screenTwoDetails.flowExecuteDetails.flowDetails.FlowDetailsController;
import subComponents.screenTwo.screenTwoDetails.flowExecuteDetails.inputDetailsList.InputDetailsListController;
import subComponents.screenTwo.screenTwoDetails.flowExecuteDetails.outputDetailsList.OutputDetailsListController;
import utils.DTOFullDetailsPastRun;

public class FlowExecuteDetailsController {

    private FlowsExecutionScreenController mainFlowsExecutionScreenController;
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

    public void setMainController(FlowsExecutionScreenController mainFlowsExecutionScreenController) {
        this.mainFlowsExecutionScreenController = mainFlowsExecutionScreenController;
    }

    public void setFlowName(String flowName) {
        mainFlowsExecutionScreenController.setFlowName(flowName);
    }

    public void setFlowData(DTOFullDetailsPastRun executedData) {
        detailsGridPaneComponentController.updateDetailsFlowRun(executedData);
        inputsDetailsComponentController.updateDetailsFlowRun(executedData);
        outputDetailsComponentController.updateDetailsFlowRun(executedData);
    }
}
