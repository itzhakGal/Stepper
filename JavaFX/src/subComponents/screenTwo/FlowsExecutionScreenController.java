package subComponents.screenTwo;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import stepper.flow.execution.FlowExecutionResult;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.mainScreen.body.BodyController;
import subComponents.screenTwo.continuation.ContinuationController;
import subComponents.screenTwo.freeInputs.FreeInputsController;
import subComponents.screenTwo.screenTwoDetails.ExecutedFlowDataController;
import subComponents.screenTwo.screenTwoDetails.flowTree.FlowTreeController;
import utils.DTOFlowExecution;
import utils.DTOFullDetailsPastRun;
import utilsDesktopApp.DTOListContinuationFlowName;

import java.util.UUID;

public class FlowsExecutionScreenController {

    private BodyController mainBodyController;
    private SystemEngineInterface systemEngine;

    @FXML
    private GridPane flowExecutionScreenGridPane;
    @FXML
    private GridPane freeInputDetailsComponent;
    @FXML
    private FreeInputsController freeInputDetailsComponentController;

    @FXML
    private HBox detailsAnchorPane;
    @FXML
    private TreeView flowTreeViewComponent;
    @FXML
    private FlowTreeController flowTreeViewComponentController;
    @FXML
    private VBox flowContinuationComponent;
    @FXML
    private ContinuationController flowContinuationComponentController;
    @FXML
    private GridPane flowDetailsGridPane;
    @FXML private Button rerunButton;
    @FXML private ProgressBar progressBar;
    private String flowName;
    private final SimpleStringProperty currentFlowNameProperty;

    private SimpleStringProperty executedFlowIDProperty;

    private SimpleBooleanProperty rerunButtonProperty;

    private ExecutedFlowDataController executedFlowDataController = new ExecutedFlowDataController();

    public FlowsExecutionScreenController() {
        currentFlowNameProperty = new SimpleStringProperty();
        executedFlowIDProperty = new SimpleStringProperty();
        rerunButtonProperty = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() {
        if (freeInputDetailsComponentController != null && flowTreeViewComponentController != null && flowContinuationComponentController != null ) {
            freeInputDetailsComponentController.setMainController(this);
            flowTreeViewComponentController.setMainController(this);
            flowContinuationComponentController.setMainController(this);
        }

        progressBar.setStyle("-fx-accent: #5c88be;");
    }
    @FXML
    public void rerunButtonAction()
    {
        mainBodyController.updateExecuteFlowButton(flowName);
        UUID flowId = freeInputDetailsComponentController.getCurrFlowId();
        DTOFullDetailsPastRun fullDetailsPastRun = systemEngine.getFlowExecutedDataDTO(flowId);
        mainBodyController.getFlowExecutionScreenComponentController().getFreeInputDetailsComponentController().updateFreeInputForRerunExecution(fullDetailsPastRun.getInputs(), fullDetailsPastRun.getFlowName());
        mainBodyController.openFlowExecutionTab();
        rerunButtonProperty.set(true);
    }
    public void setMainController(BodyController mainBodyController) {
        this.mainBodyController = mainBodyController;
        flowTreeViewComponentController.init(mainBodyController);
        executedFlowDataController.init(mainBodyController);
    }

    public void setSystemEngine(SystemEngineInterface engineManager) {
        this.systemEngine = engineManager;
        freeInputDetailsComponentController.setSystemEngine(systemEngine);
        flowTreeViewComponentController.setSystemEngine(systemEngine);
        flowContinuationComponentController.setSystemEngine(systemEngine);
        executedFlowDataController.setSystemEngine(systemEngine);

    }

    public void updateDetailsFlowExecution(String flowName, boolean isContinuation)
    {
        this.flowName = flowName;
        flowExecutionScreenGridPane.setVisible(true);
        DTOFlowExecution dtoFlowExecution = systemEngine.readInputsJavaFX(flowName);
        freeInputDetailsComponentController.updateDetailsFreeInputs(dtoFlowExecution, isContinuation);
    }
    public void updateDetailsFlowRun(DTOFullDetailsPastRun endOFlowExecution)
    {
        mainBodyController.updateButtons();
    }

    public void updateContinuationDetails(String flowName) {
        DTOListContinuationFlowName listContinuationFlowName = systemEngine.setListContinuationFlowName(flowName);
        if(!listContinuationFlowName.getListContinuationFlowName().isEmpty()) {
            flowContinuationComponent.setVisible(true);
            flowContinuationComponentController.updateContinuationDetails(listContinuationFlowName);
        }
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setVisibleDetails(boolean flag) {
        flowDetailsGridPane.setVisible(flag);
    }

    public void setVisibleContinuation() {
        flowContinuationComponent.setVisible(false);
    }

    public BodyController getMainBodyController() {
        return mainBodyController;
    }

    public SimpleStringProperty getExecutedFlowID() {
        return this.executedFlowIDProperty;
    }

    public FreeInputsController getCollectFlowInputsController() {
        return this.freeInputDetailsComponentController;
    }

    public void setExecutedFlowID(UUID id) {
        this.executedFlowIDProperty.set(id.toString());
    }

    public FlowTreeController getFlowTreeComponentController() {
        return this.flowTreeViewComponentController;
    }

    public HBox getDetailsAnchorPane() {
        return detailsAnchorPane;
    }
    public ContinuationController getContinuation() {
        return this.flowContinuationComponentController;
    }

    public void initListener() {
        flowContinuationComponentController.initListener();
        this.getFlowTreeViewComponentController().getIsTaskFinished()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue){
                      updateContinuationDetails(flowName);
                    }
                });

        this.getFlowTreeViewComponentController().getIsTaskFinished()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue){
                        rerunButton.setDisable(false);
                    }
                    else
                        rerunButton.setDisable(true);
                });

        this.getFlowTreeViewComponentController().getIsTaskFinished()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue){
                        progressBar.setVisible(false);
                    }
                    else
                        progressBar.setDisable(false);
                });

        this.mainBodyController.isFlowsExecutionButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (freeInputDetailsComponentController.getCurrFlowId()!= null) {
                        FlowExecutionResult result = systemEngine.getExecutedFlowsMap().get(freeInputDetailsComponentController.getCurrFlowId()).getFlowExecutionResult();
                        if (!newValue && (result != null)) {
                            flowExecutionScreenGridPane.setVisible(false);
                        }
                    }
                });

    }

    public FlowTreeController getFlowTreeViewComponentController() {
        return flowTreeViewComponentController;
    }

    public FreeInputsController getFreeInputDetailsComponentController() {
        return freeInputDetailsComponentController;
    }

    public Button getRerunButton() {
        return rerunButton;
    }

    public SimpleBooleanProperty getRerunButtonProperty ()
    {
        return rerunButtonProperty;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public VBox getFlowContinuationComponent() {
        return flowContinuationComponent;
    }
}
