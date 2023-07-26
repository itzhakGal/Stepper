package clientComponents.screenTwo.screenTwoDetails.flowExecuteDetails;

import clientComponents.screenTwo.FlowsExecutionScreenController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenTwo.screenTwoDetails.flowExecuteDetails.flowDetails.FlowDetailsController;
import clientComponents.screenTwo.screenTwoDetails.flowExecuteDetails.inputDetailsList.InputDetailsListController;
import clientComponents.screenTwo.screenTwoDetails.flowExecuteDetails.outputDetailsList.OutputDetailsListController;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utils.DTOFullDetailsPastRun;

public class FlowExecuteDetailsController {

    private clientComponents.screenTwo.FlowsExecutionScreenController mainFlowsExecutionScreenController;

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

    public void setMainController(FlowsExecutionScreenController mainFlowsExecutionScreenController) {
        this.mainFlowsExecutionScreenController = mainFlowsExecutionScreenController;
    }

    public void setFlowName(String flowName) {
        mainFlowsExecutionScreenController.setFlowName(flowName);
    }

    public void setFlowData(DTOFullDetailsPastRunWeb executedData) {
        detailsGridPaneComponentController.updateDetailsFlowRun(executedData);
        inputsDetailsComponentController.updateDetailsFlowRun(executedData);
        outputDetailsComponentController.updateDetailsFlowRun(executedData);
    }
}
