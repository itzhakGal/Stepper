package subComponents.screenThree.lowerScreen.flowExecuteDetails.flowDetails;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenThree.lowerScreen.flowExecuteDetails.FlowExecuteDetailsController;
import utils.DTOFullDetailsPastRun;


public class FlowDetailsController {

    private FlowExecuteDetailsController maimFlowExecuteDetailsController;
    private SystemEngineInterface systemEngine;
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
        //totalRunningTime.textProperty().bind(Bindings.format("%,d", totalRunningTimeProperty));
    }

    public void setMainController(FlowExecuteDetailsController maimFlowExecuteDetailsController) {
        this.maimFlowExecuteDetailsController = maimFlowExecuteDetailsController;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
    }

    public void updateDetailsFlowRun(DTOFullDetailsPastRun endOFlowExecution) {
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