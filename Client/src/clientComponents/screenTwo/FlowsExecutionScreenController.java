package clientComponents.screenTwo;

import clientComponents.mainScreen.body.BodyController;
import clientComponents.screenTwo.continuation.ContinuationController;
import clientComponents.screenTwo.freeInputs.FreeInputsController;
import clientComponents.screenTwo.freeInputs.freeInputDetails.collectionInputs.CollectionInputs;
import clientComponents.screenTwo.screenTwoDetails.flowExecuteDetails.FlowExecuteDetailsController;
import clientComponents.screenTwo.screenTwoDetails.flowTree.FlowTreeController;
import clientComponents.screenTwo.screenTwoDetails.stepListDetails.StepListDetailsController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.flow.execution.FlowExecutionResult;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenTwo.screenTwoDetails.ExecutedFlowDataController;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utils.DTOFlowExecution;
import utils.DTOFullDetailsPastRun;
import utils.DTOInputFlowPast;
import utils.DTOStepFlowPast;
import utilsDesktopApp.DTOListContinuationFlowName;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.IOException;
import java.util.UUID;

public class FlowsExecutionScreenController {

    private clientComponents.mainScreen.body.BodyController mainBodyController;
    @FXML
    private GridPane flowExecutionScreenGridPane;
    @FXML
    private GridPane freeInputDetailsComponent;
    @FXML
    private clientComponents.screenTwo.freeInputs.FreeInputsController freeInputDetailsComponentController;
    @FXML
    private HBox detailsAnchorPane;
    @FXML
    private TreeView flowTreeViewComponent;
    @FXML
    private clientComponents.screenTwo.screenTwoDetails.flowTree.FlowTreeController flowTreeViewComponentController;
    @FXML
    private VBox flowContinuationComponent;
    @FXML
    private clientComponents.screenTwo.continuation.ContinuationController flowContinuationComponentController;
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

        String finalUrl = HttpUrl
                .parse(Constants.FLOW_EXECUTION_TASK)
                .newBuilder()
                .addQueryParameter("flowId", flowId.toString())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    handleFailure(e.getMessage());
                });
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        DTOFullDetailsPastRunWeb fullDetailsPastRun = new Gson().fromJson(responseData, DTOFullDetailsPastRunWeb.class);

                        Platform.runLater(() -> {
                            mainBodyController.getFlowExecutionScreenComponentController().getFreeInputDetailsComponentController().updateFreeInputForRerunExecution(fullDetailsPastRun.getInputs(), fullDetailsPastRun.getFlowName());
                            mainBodyController.openFlowExecutionTab();
                            rerunButtonProperty.set(true);
                        });
                    }} finally {
                            response.close();
                }
            }
        });
    }



    public void setMainController(clientComponents.mainScreen.body.BodyController mainBodyController) {
        this.mainBodyController = mainBodyController;
        flowTreeViewComponentController.init(mainBodyController);
        executedFlowDataController.init(mainBodyController);
    }

    public void updateDetailsFlowExecution(String flowName, boolean isContinuation)
    {
        this.flowName = flowName;
        flowExecutionScreenGridPane.setVisible(true);

        String finalUrl = HttpUrl
                .parse(Constants.RERUN_EXECUTION)
                .newBuilder()
                .addQueryParameter("flowName", flowName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    handleFailure(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        DTOFlowExecution dtoFlowExecution = new Gson().fromJson(response.body().string(), DTOFlowExecution.class);
                        Platform.runLater(() -> {
                            freeInputDetailsComponentController.updateDetailsFreeInputs(dtoFlowExecution, isContinuation);
                        });
                    }}finally {
                    response.close();
                }

            }
        });

    }
    public void updateDetailsFlowRun(DTOFullDetailsPastRunWeb endOFlowExecution)
    {
        mainBodyController.updateButtons();
    }

    public void updateContinuationDetails(String flowName) {

        UUID flowId = freeInputDetailsComponentController.getCurrFlowId();

        String finalUrl = HttpUrl
                .parse(Constants.LIST_CONTINUATION_FLOW_NAME)
                .newBuilder()
                .addQueryParameter("flowName", flowName)
                .addQueryParameter("flowUUID", flowId.toString())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    handleFailure(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    if (response.isSuccessful()) {
                        DTOListContinuationFlowName listContinuationFlowName = new Gson().fromJson(response.body().string(), DTOListContinuationFlowName.class);
                        Platform.runLater(() -> {
                            if(!listContinuationFlowName.getListContinuationFlowName().isEmpty()) {
                                flowContinuationComponent.setVisible(true);
                                flowContinuationComponentController.updateContinuationDetails(listContinuationFlowName);
                            }
                        });
                    }} finally {
                    response.close();
                }
            }
        });
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

    public clientComponents.screenTwo.freeInputs.FreeInputsController getCollectFlowInputsController() {
        return this.freeInputDetailsComponentController;
    }

    public void setExecutedFlowID(UUID id) {
        this.executedFlowIDProperty.set(id.toString());
    }

    public clientComponents.screenTwo.screenTwoDetails.flowTree.FlowTreeController getFlowTreeComponentController() {
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

                        String finalUrl = HttpUrl
                                .parse(Constants.FLOW_EXECUTION_RESULT)
                                .newBuilder()
                                .addQueryParameter("flowUUID", freeInputDetailsComponentController.getCurrFlowId().toString())
                                .build()
                                .toString();

                        HttpClientUtil.runAsync(finalUrl, new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Platform.runLater(() -> {
                                    handleFailure(e.getMessage());
                                });
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                try{
                                    if (response.isSuccessful()) {
                                        FlowExecutionResult result = new Gson().fromJson(response.body().string(), FlowExecutionResult.class);
                                        Platform.runLater(() -> {
                                            if (!newValue && (result != null)) {
                                                flowExecutionScreenGridPane.setVisible(false);
                                            }
                                        });
                                    }}finally {
                                response.close();
                            }
                            }
                        });
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
    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }
}
