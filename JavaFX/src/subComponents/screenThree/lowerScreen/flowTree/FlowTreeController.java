package subComponents.screenThree.lowerScreen.flowTree;


import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import subComponents.screenThree.topScreen.TopScreenController;
import utils.DTOFullDetailsPastRun;
import utils.DTOStepFlowPast;

import java.util.UUID;

public class FlowTreeController {
    public FlowExecutionHistoryController mainFlowExecutionHistoryController;
    private SystemEngineInterface systemEngine;
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
                        mainFlowExecutionHistoryController.getTableFlowExecutionController().getContinuationComponentController().getContinueToFlowButton().setVisible(false);
                        mainFlowExecutionHistoryController.getTableFlowExecutionController().getContinuationComponentController().getFlowNameContinuationListView().getItems().clear();
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

        addExecutedFlowData(systemEngine.getFlowExecutedDataDTO(UUID.fromString(this.tableFlowExecutionController.getChosenFlowIdProperty().getValue())));
        this.flowTreeView.getRoot().setExpanded(true);
    }

    private void addExecutedFlowData(DTOFullDetailsPastRun executedData) {
        boolean found;
        for (DTOStepFlowPast step : executedData.getSteps()) {
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
        this.systemEngine = systemEngine;
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


