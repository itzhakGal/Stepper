package adminComponents.screenThree.lowerScreen.flowExecuteDetails.flowDetails;

import adminComponents.screenThree.lowerScreen.flowExecuteDetails.FlowExecuteDetailsController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import stepper.systemEngine.SystemEngineInterface;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utils.DTOFullDetailsPastRun;


public class FlowDetailsController {

    private FlowExecuteDetailsController maimFlowExecuteDetailsController;
    @FXML
    private Label flowName;
    @FXML
    private Label uniqueName;
    @FXML
    private Label flowResult;
    @FXML
    private Label totalRunningTime;

    private SimpleStringProperty flowNameProperty;
    private SimpleStringProperty uniqueNameProperty;
    private SimpleStringProperty flowResultProperty;
    private SimpleObjectProperty totalRunningTimeProperty;

    public FlowDetailsController() {
        flowNameProperty =  new SimpleStringProperty();
        uniqueNameProperty =  new SimpleStringProperty();
        flowResultProperty =  new SimpleStringProperty();
        totalRunningTimeProperty =  new SimpleObjectProperty();
    }

    @FXML
    public void initialize() {
        flowName.textProperty().bind(flowNameProperty);
        uniqueName.textProperty().bind(uniqueNameProperty);
        flowResult.textProperty().bind(flowResultProperty);
        totalRunningTime.textProperty().bind(totalRunningTimeProperty);
    }

    public void setMainController(FlowExecuteDetailsController maimFlowExecuteDetailsController) {
        this.maimFlowExecuteDetailsController = maimFlowExecuteDetailsController;
    }

    public void updateDetailsFlowRun(DTOFullDetailsPastRunWeb endOFlowExecution) {
        flowNameProperty.set(endOFlowExecution.getFlowName());
        maimFlowExecuteDetailsController.setFlowName(endOFlowExecution.getFlowName());
        uniqueNameProperty.set(endOFlowExecution.getUniqueId().toString());
        if(endOFlowExecution.getFinalResult() != null)
            flowResultProperty.set(endOFlowExecution.getFinalResult().getDescription());
        else
            flowResultProperty.set("The flow is still in progress");

        if(endOFlowExecution.getTotalTime() != null)
            totalRunningTimeProperty.set(endOFlowExecution.getTotalTime().toMillis() +"");
        else
            totalRunningTimeProperty.set("The flow is still in progress");
    }

}