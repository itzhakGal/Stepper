package adminComponents.screenThree.lowerScreen;

import adminComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import adminComponents.screenThree.lowerScreen.flowExecuteDetails.FlowExecuteDetailsController;
import adminComponents.screenThree.lowerScreen.stepListDetails.StepListDetailsController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.step.api.StepResult;
import stepper.systemEngine.SystemEngineInterface;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOStepFlowPastWeb;
import utils.DTOFullDetailsPastRun;


import java.io.IOException;
import java.util.List;

public class ExecutedDataController {

    private FlowExecutionHistoryController flowExecutionHistoryController;

    public void init(FlowExecutionHistoryController flowExecutionHistoryController) {
        this.flowExecutionHistoryController = flowExecutionHistoryController;

        this.flowExecutionHistoryController.getFlowTreeComponentController().getSelectedItem()
                .addListener((observable, oldValue, newValue) -> {
                    this.flowExecutionHistoryController.getVboxDetails().getChildren().clear();
                    showExecutedFlowData();
                });

        this.flowExecutionHistoryController.getMainBodyController().isExecutionsHistoryButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        this.flowExecutionHistoryController.getVboxDetails().getChildren().clear();
                    }
                });
        this.flowExecutionHistoryController.getTableFlowExecutionController().getChosenFlowIdProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue.equals("")) {
                        this.flowExecutionHistoryController.getVboxDetails().getChildren().clear();
                    }
                });

    }

    private void showExecutedFlowData() {
        TreeItem<String> selectedTreeItemItem;
        DTOFullDetailsPastRun executedData;
        StepResult stepResult;

        selectedTreeItemItem =  this.flowExecutionHistoryController.getFlowTreeComponentController().getSelectedItem().get();

        String finalUrl = HttpUrl
                .parse(Constants.FLOW_EXECUTION_TASK)
                .newBuilder()
                // .addQueryParameter("flowUUID", this.flowExecutionHistoryController.getTableFlowExecutionController().getChosenFlowIdProperty().getValue()
                .addQueryParameter("flowId", this.flowExecutionHistoryController.getTableFlowExecutionController().getChosenFlowIdProperty().getValue())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    handleFailure((e.getMessage()));
                });
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        DTOFullDetailsPastRunWeb executedData = new Gson().fromJson(responseData, DTOFullDetailsPastRunWeb.class);

                        Platform.runLater(() -> {
                            boolean isRootSelected = (selectedTreeItemItem != null && selectedTreeItemItem.getParent() == null);

                            if (isRootSelected) {
                                try {
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flowExecuteDetails/flowExecuteDetails.fxml"));
                                    VBox contentPane = fxmlLoader.load();
                                    FlowExecuteDetailsController controller = fxmlLoader.getController();
                                    controller.setMainController(flowExecutionHistoryController);
                                    //controller.setSystemEngine(systemEngine);

                                    flowExecutionHistoryController.getVboxDetails().getChildren().add(contentPane);

                                    // Store the controller in the TitledPane's properties
                                    controller.setFlowData(executedData);

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                            else {
                                try {
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stepListDetails/stepListDetails.fxml"));
                                    ScrollPane contentPane = fxmlLoader.load();

                                    StepListDetailsController controller = fxmlLoader.getController();
                                    controller.setMainController(flowExecutionHistoryController);
                                    //controller.setSystemEngine(systemEngine);

                                    flowExecutionHistoryController.getVboxDetails().getChildren().add(contentPane);

                                    // Store the controller in the TitledPane's properties
                                    DTOStepFlowPastWeb stepDetails = getStepDetails(selectedTreeItemItem.getValue().toString(), executedData.getSteps());
                                    controller.updateDetailsFlowRun(stepDetails);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }}finally {
                    response.close();
                }

            }
        });

    }
    public DTOStepFlowPastWeb getStepDetails(String currentSelected, List<DTOStepFlowPastWeb> steps )
    {
        for(DTOStepFlowPastWeb stepDetails : steps)
        {
            if(stepDetails.getFinalStepName().equals(currentSelected))
                return stepDetails;
        }
        return null;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        //this.systemEngine = systemEngine;
    }
    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }
}
