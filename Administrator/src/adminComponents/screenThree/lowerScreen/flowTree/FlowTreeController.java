package adminComponents.screenThree.lowerScreen.flowTree;


import adminComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import adminComponents.screenThree.topScreen.TopScreenController;
import com.google.gson.Gson;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.systemEngine.SystemEngineInterface;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOStepFlowPastWeb;


import java.io.IOException;

public class FlowTreeController {
    public FlowExecutionHistoryController mainFlowExecutionHistoryController;
    //private SystemEngineInterface systemEngine;
    @FXML
    private TreeView<String> flowTreeView;

    private TopScreenController tableFlowExecutionController;

    private SimpleObjectProperty<TreeItem<String>> selectedItem;

    public FlowTreeController() {
        this.selectedItem = new SimpleObjectProperty<>();
    }

      public void init() {

        flowTreeView.setOnMouseClicked(event -> selectedItem.set(flowTreeView.getSelectionModel().getSelectedItem()));

        mainFlowExecutionHistoryController.getTableFlowExecutionController().getChosenFlowIdProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue.equals("")) {
                        insertDataToTreeView();
                        //getTableFlowExecutionController().getContinuationComponentController().getContinueToFlowButton().setVisible(false);
                        //
                        // mainFlowExecutionHistoryController.getTableFlowExecutionController().getContinuationComponentController().getFlowNameContinuationListView().getItems().clear();
                    } else {
                        if(flowTreeView.getRoot() != null) {
                            flowTreeView.getRoot().getChildren().clear();
                            flowTreeView.setRoot(null);
                        }
                    }
                });

    }

    private void insertDataToTreeView() {

        TreeItem<String> root = new TreeItem<>(
                this.tableFlowExecutionController.getChosenFlowNameProperty().getValue()
        );
        this.flowTreeView.setRoot(root);

        String finalUrl = HttpUrl
                .parse(Constants.FLOW_EXECUTION_TASK)
                .newBuilder()
                .addQueryParameter("flowId", this.tableFlowExecutionController.getChosenFlowIdProperty().getValue())
                //.addQueryParameter("flowUUID", this.tableFlowExecutionController.getChosenFlowIdProperty().getValue())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        DTOFullDetailsPastRunWeb executedData = new Gson().fromJson(responseData, DTOFullDetailsPastRunWeb.class);

                        Platform.runLater(() -> {
                            addExecutedFlowData(executedData);
                            flowTreeView.getRoot().setExpanded(true);
                        });
                    }}finally {
                    response.close();
                }

            }
        });

        //addExecutedFlowData(systemEngine.getFlowExecutedDataDTO(UUID.fromString(this.tableFlowExecutionController.getChosenFlowIdProperty().getValue())));
        //this.flowTreeView.getRoot().setExpanded(true);
    }


    private void addExecutedFlowData(DTOFullDetailsPastRunWeb executedData) {
        boolean found;
        for (DTOStepFlowPastWeb step : executedData.getSteps()) {
            found = false;
            for (TreeItem<String> item : this.flowTreeView.getRoot().getChildren()) {
                if (item.getValue().equals(step.getFinalStepName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                TreeItem<String> newTreeItem = new TreeItem<>(step.getFinalStepName());
                if (step.getStepResult() != null) {
                    newTreeItem.setGraphic(createStepResultText(step.getStepResult().toString()));

                    // Apply fade-in effect to the newTreeItem
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), newTreeItem.getGraphic());
                    fadeTransition.setFromValue(0.0);
                    fadeTransition.setToValue(1.0);
                    fadeTransition.play();

                    this.flowTreeView.getRoot().getChildren().add(newTreeItem);
                }
            }
        }
    }
    private Node createStepResultText(String result) {
        Text text = new Text(result);
        text.setFill(getColorForResult(result));
        text.setStyle("-fx-font-family: Georgia");
        return text;
    }

    private Color getColorForResult(String result) {
        switch (result) {
            case "SUCCESS":
                return Color.DARKSEAGREEN;
            case "FAILURE":
                return Color.INDIANRED;
            case "WARNING":
                return Color.ORANGE;
            default:
                return Color.BLACK;
        }
    }

    public void setMainController(FlowExecutionHistoryController mainFlowExecutionHistoryController) {
        this.mainFlowExecutionHistoryController = mainFlowExecutionHistoryController;
        this.tableFlowExecutionController = mainFlowExecutionHistoryController.getTableFlowExecutionController();
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        //this.systemEngine = systemEngine;
    }

    public SimpleObjectProperty<TreeItem<String>> getSelectedItem() {
        return this.selectedItem;
    }

    public void clearDetails() {
        if(flowTreeView.getRoot()!=null) {
            flowTreeView.getRoot().getChildren().clear(); // Clear the children of the root TreeItem
            flowTreeView.setRoot(null); // Set the root of the TreeView to null
        }
    }
}


