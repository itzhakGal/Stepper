package clientComponents.screenThree.flowExecutionHistory;

import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.mainScreen.body.BodyController;
import clientComponents.screenThree.lowerScreen.ExecutedDataController;
import clientComponents.screenThree.lowerScreen.flowTree.FlowTreeController;
import clientComponents.screenThree.topScreen.TopScreenController;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utils.DTOFullDetailsPastRun;

import java.util.List;

public class FlowExecutionHistoryController {
    private BodyController mainBodyController;
    @FXML
    private GridPane flowExecutionTableComponent;
    @FXML
    private TopScreenController flowExecutionTableComponentController;
    @FXML
    private TreeView flowTreeComponent;
    @FXML
    private FlowTreeController flowTreeComponentController;

    private ExecutedDataController executedDataController = new ExecutedDataController();

    @FXML
    private AnchorPane vboxDetails;

    @FXML
    public void initialize() {
        if (flowExecutionTableComponentController != null && flowTreeComponentController != null) {
            flowExecutionTableComponentController.setMainController(this);
            flowTreeComponentController.setMainController(this);
        }
    }

    public void setMainController(BodyController mainBodyController) {
        this.mainBodyController = mainBodyController;
        flowExecutionTableComponentController.init();
        flowTreeComponentController.init();
        executedDataController.init(this);
    }

    public void setFlowName(String flowName) {

    }

    public void updateListOfExecutedFlows(List<DTOFullDetailsPastRunWeb> flowsExecutedList) {
        flowExecutionTableComponentController.setTableView(flowsExecutedList);
    }

    public TopScreenController getTableFlowExecutionController() {
        return flowExecutionTableComponentController;
    }

    public FlowTreeController getFlowTreeComponentController() {
        return flowTreeComponentController;
    }

    public AnchorPane getVboxDetails() {
        return vboxDetails;
    }

    public void initListener() {

        flowExecutionTableComponentController.initListener();
    }

    public BodyController getMainBodyController() {
        return mainBodyController;
    }

    public TopScreenController getFlowExecutionTableComponentController() {
        return flowExecutionTableComponentController;
    }

    public void clearDetails() {
        flowExecutionTableComponentController.clearDetails();
        flowTreeComponentController.clearDetails();
    }


}
