package clientComponents.screenTwo.screenTwoDetails;

import clientComponents.mainScreen.body.BodyController;
import clientComponents.screenTwo.screenTwoDetails.flowExecuteDetails.FlowExecuteDetailsController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.step.api.StepResult;
import clientComponents.screenTwo.screenTwoDetails.stepListDetails.StepListDetailsController;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOStepFlowPastWeb;
import utils.DTOFullDetailsPastRun;
import java.io.IOException;
import java.util.List;


public class ExecutedFlowDataController {
    private clientComponents.mainScreen.body.BodyController bodyController;
    private final SimpleBooleanProperty isTaskFinished;

    public ExecutedFlowDataController() {
        this.isTaskFinished = new SimpleBooleanProperty(false);
    }

    public void init(BodyController bodyController) {
        this.isTaskFinished.set(false);
        this.bodyController = bodyController;


            bodyController.getFlowExecutionScreenComponentController().getContinuation().getContinuationButtonPressed()
                    .addListener((observable, oldValue, newValue) -> {
                        if (newValue)
                            bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
                    });

            bodyController.getFlowExecutionScreenComponentController().getFlowTreeComponentController().getSelectedItem()
                    .addListener((observable, oldValue, newValue) -> {
                        bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
                        showExecutedFlowData();
                    });

            bodyController.getFlowDefinitionScreenComponentController().getIsExecuteFlowButtonClicked()
                    .addListener((observable, oldValue, newValue) -> {
                        bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
                    });
            bodyController.getFlowExecutionScreenComponentController().getRerunButtonProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
                    });

        bodyController.isFlowDefinitionButtonProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
            }
        });

        bodyController.getFlowExecutionHistoryScreenComponentController().getFlowExecutionTableComponentController().getRerunFlowButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().clear();
                    }
                });
    }



    private void showExecutedFlowData() {
        TreeItem<String> selectedTreeItemItem;
        DTOFullDetailsPastRun executedData;
        StepResult stepResult;

        selectedTreeItemItem = bodyController.getFlowExecutionScreenComponentController().getFlowTreeComponentController().getSelectedItem().get();

        String finalUrl = HttpUrl
                .parse(Constants.FLOW_EXECUTION_TASK)
                .newBuilder()
                .addQueryParameter("flowId", bodyController.getFlowExecutionScreenComponentController().getExecutedFlowID().getValue())
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
                    DTOFullDetailsPastRunWeb executedData = new Gson().fromJson(responseData, DTOFullDetailsPastRunWeb.class);

                    Platform.runLater(() -> {
                        boolean isRootSelected = (selectedTreeItemItem != null && selectedTreeItemItem.getParent() == null);


                        if (isRootSelected) {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flowExecuteDetails/flowExecuteDetails.fxml"));
                                HBox contentPane = fxmlLoader.load();
                                FlowExecuteDetailsController controller = fxmlLoader.getController();
                                controller.setMainController(bodyController.getFlowExecutionScreenComponentController());
                                //controller.setSystemEngine(systemEngine);

                                bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().add(contentPane);
                                controller.setFlowData(executedData);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        else {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stepListDetails/stepListDetails.fxml"));
                                HBox contentPane = fxmlLoader.load();

                                StepListDetailsController controller = fxmlLoader.getController();
                                controller.setMainController(bodyController.getFlowExecutionScreenComponentController());
                                //controller.setSystemEngine(systemEngine);

                                bodyController.getFlowExecutionScreenComponentController().getDetailsAnchorPane().getChildren().add(contentPane);

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

    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }

}