package subComponents.screenTwo.screenTwoDetails.flowTree;


import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.mainScreen.body.BodyController;
import subComponents.screenTwo.FlowsExecutionScreenController;
import utils.DTOFullDetailsPastRun;
import utils.DTOStepFlowPast;

import java.util.UUID;

public class FlowTreeController {
    public FlowsExecutionScreenController mainFlowsExecutionScreenController;
    private SystemEngineInterface engineManager;
    @FXML private TreeView<String> flowTreeView;
    private BodyController bodyController;
    private final Logic logic;
    private final SimpleBooleanProperty isTaskFinished;
    private final SimpleObjectProperty<TreeItem<String>> selectedItem;

    public FlowTreeController() {
        this.logic = new Logic(this);
        this.isTaskFinished = new SimpleBooleanProperty(false);
        this.selectedItem = new SimpleObjectProperty<>();
    }

    public void init(BodyController bodyController) {
        this.bodyController = bodyController;
        flowTreeView.setOnMouseClicked(event -> selectedItem.set(flowTreeView.getSelectionModel().getSelectedItem()));

            bodyController.getFlowExecutionScreenComponentController().getMainBodyController().getFlowDefinitionScreenComponentController().getIsExecuteFlowButtonClicked()
                    .addListener((observable, oldValue, newValue) -> {
                        if (newValue && flowTreeView.getRoot() != null) {
                            flowTreeView.getRoot().getChildren().clear();
                            flowTreeView.setRoot(null);
                        }
                    });

        bodyController.getFlowExecutionScreenComponentController().getRerunButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue && flowTreeView.getRoot() != null) {
                        flowTreeView.getRoot().getChildren().clear();
                        flowTreeView.setRoot(null);
                    }
                });

            bodyController.getFlowExecutionScreenComponentController().getCollectFlowInputsController().getStartButtonClickProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            this.isTaskFinished.set(false);
                            fetchData();
                        }
                    });

        bodyController.getFlowExecutionScreenComponentController().getContinuation().getContinuationButtonPressed().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                flowTreeView.getRoot().getChildren().clear();
        });

        bodyController.getFlowExecutionHistoryScreenComponentController().getFlowExecutionTableComponentController().getRerunFlowButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue && flowTreeView.getRoot() != null) {
                        flowTreeView.getRoot().getChildren().clear();
                        flowTreeView.setRoot(null);
                    }
                });
        }




    private void fetchData() {

            TreeItem<String> root = new TreeItem<>(bodyController.getFlowExecutionScreenComponentController().getFlowName());
            this.flowTreeView.setRoot(root);
            UIAdapter uiAdapter = createUIAdapter();
            logic.fetchData(
                    UUID.fromString(bodyController.getFlowExecutionScreenComponentController().getExecutedFlowID().getValue()),
                    uiAdapter, isTaskFinished, bodyController.getFlowExecutionScreenComponentController().getExecutedFlowID(), engineManager);


        this.flowTreeView.getRoot().setExpanded(true);
    }

    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                this::addExecutedFlowData
        );
    }

    private void addExecutedFlowData(DTOFullDetailsPastRun executedData) {
        if (flowTreeView.getRoot() == null) {
            return; // Return early if the root is null
        }

        boolean found;
        for (DTOStepFlowPast step : executedData.getSteps()) {
            found = false;
            for (TreeItem<String> item : flowTreeView.getRoot().getChildren()) {
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

                    flowTreeView.getRoot().getChildren().add(newTreeItem);
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
    public SimpleBooleanProperty getIsTaskFinished() {
        return this.isTaskFinished;
    }

    public SimpleObjectProperty<TreeItem<String>> getSelectedItem() {
        return this.selectedItem;
    }

    public void setListeners() {

    }

    public void setMainController(FlowsExecutionScreenController mainFlowsExecutionScreenController) {
        this.mainFlowsExecutionScreenController = mainFlowsExecutionScreenController;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.engineManager=systemEngine;
    }

    public void clearTree() {
        if(flowTreeView.getRoot() != null) {
            flowTreeView.getRoot().getChildren().clear();
            flowTreeView.setRoot(null);
        }
    }
}
