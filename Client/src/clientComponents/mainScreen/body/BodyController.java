package clientComponents.mainScreen.body;

import clientComponents.screenOne.flowDefinitionScreen.FlowDefinitionScreenController;
import clientComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import com.google.gson.Gson;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.mainScreen.app.AppController;
import clientComponents.screenTwo.FlowsExecutionScreenController;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOListFullDetailsPastRunWeb;

import java.io.IOException;
import java.util.List;


public class BodyController {

    private AppController mainController;
    @FXML
    private clientComponents.screenOne.flowDefinitionScreen.FlowDefinitionScreenController flowDefinitionScreenComponentController;
    @FXML
    private ScrollPane flowDefinitionScreenComponent;
    @FXML
    private FlowsExecutionScreenController flowExecutionScreenComponentController;
    @FXML
    private ScrollPane flowExecutionScreenComponent;
    @FXML
    private clientComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController flowExecutionHistoryScreenComponentController;
    @FXML
    private ScrollPane flowExecutionHistoryScreenComponent;
    @FXML
    private Tab executionsHistoryButton;
    @FXML
    private Tab flowsExecutionButton;
    @FXML
    private Tab flowDefinitionButton;
    @FXML
    private TabPane optionsTabPane;
    private SimpleBooleanProperty executionsHistoryButtonProperty;
    private SimpleBooleanProperty flowsExecutionButtonProperty;
    private SimpleBooleanProperty flowDefinitionButtonProperty;

    public BodyController()
    {
        executionsHistoryButtonProperty =  new SimpleBooleanProperty(false);
        flowsExecutionButtonProperty =  new SimpleBooleanProperty(false);
        flowDefinitionButtonProperty =  new SimpleBooleanProperty(true);

    }
    @FXML
    public void initialize() {
        if (flowDefinitionScreenComponentController != null  && flowExecutionScreenComponentController != null && flowExecutionHistoryScreenComponentController != null) {
            flowDefinitionScreenComponentController.setMainController(this);
            flowExecutionScreenComponentController.setMainController(this);
            flowExecutionHistoryScreenComponentController.setMainController(this);
        }
        setTabsListener();
    }

    private void animateTabContent(Tab tab) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.2), tab.getContent());
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void updatePushTabButtons()
    {
        flowDefinitionButton.setDisable(false);
        mainController.refresherDataUser();
        flowDefinitionScreenComponentController.setListFlowsDetails();
    }
    public void updateButtons()
    {
        executionsHistoryButton.setDisable(false);
    }

    public void updateExecuteFlowButton(String flowName)
    {
        flowsExecutionButton.setDisable(false);
        flowExecutionScreenComponentController.updateDetailsFlowExecution(flowName, false);
    }
    @FXML
    void executionsHistoryButtonAction(Event event) {

        flowExecutionHistoryScreenComponentController.getTableFlowExecutionController().refresherDataFlowsExecution();
    }


    @FXML
    void flowDefinitionButtonAction(Event event) {
        flowDefinitionScreenComponentController.setListFlowsDetails();
        flowDefinitionScreenComponentController.getSelectedFlowDetailsComponent().setVisible(false);
    }

    @FXML
    void flowsExecutionButtonAction(Event event) {

    }

    public void openTabFlowDefinition() {
        // Open the Tab of flowDefinition
        optionsTabPane.getSelectionModel().select(flowDefinitionButton);

    }

    public void openFlowExecutionTab() {
        // Open the Tab of flowExecution
        optionsTabPane.getSelectionModel().select(flowsExecutionButton);
    }

    public FlowsExecutionScreenController getFlowExecutionScreenComponentController() {
        return flowExecutionScreenComponentController;
    }

    public FlowDefinitionScreenController getFlowDefinitionScreenComponentController() {
        return flowDefinitionScreenComponentController;
    }

    private void setTabsListener() {
        optionsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == flowDefinitionButton) {
                flowDefinitionButtonProperty.set(true);
                executionsHistoryButtonProperty.set(false);
                flowsExecutionButtonProperty.set(false);
            } else if (newTab == executionsHistoryButton) {
                flowDefinitionButtonProperty.set(false);
                executionsHistoryButtonProperty.set(true);
                flowsExecutionButtonProperty.set(false);
            } else if (newTab == flowsExecutionButton) {
                flowDefinitionButtonProperty.set(false);
                executionsHistoryButtonProperty.set(false);
                flowsExecutionButtonProperty.set(true);
            }
        });
    }

    public AppController getMainController() {
        return mainController;
    }

    public SimpleBooleanProperty isExecutionsHistoryButtonProperty() {
        return executionsHistoryButtonProperty;
    }

    public SimpleBooleanProperty isFlowDefinitionButtonProperty() {
        return flowDefinitionButtonProperty;
    }

    public SimpleBooleanProperty isFlowsExecutionButtonProperty() {
        return flowsExecutionButtonProperty;
    }


    public void initListener() {
        flowExecutionScreenComponentController.initListener();
        flowExecutionHistoryScreenComponentController.initListener();
    }

    public FlowExecutionHistoryController getFlowExecutionHistoryScreenComponentController() {
        return flowExecutionHistoryScreenComponentController;
    }

    public ScrollPane getFlowExecutionScreenComponent() {
        return flowExecutionScreenComponent;
    }

}
