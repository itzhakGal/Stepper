package subComponents.mainScreen.body;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.mainScreen.app.AppController;
import subComponents.screenFour.statisticScreen.StatisticMainController;
import subComponents.screenOne.flowDefinitionScreen.FlowDefinitionScreenController;
import subComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import subComponents.screenTwo.FlowsExecutionScreenController;
import utils.DTOFullDetailsPastRun;
import utils.DTOStatistics;

import java.util.List;


public class BodyController {

    private AppController mainController;
    private SystemEngineInterface systemEngine;
    @FXML
    private FlowDefinitionScreenController flowDefinitionScreenComponentController;
    @FXML
    private ScrollPane flowDefinitionScreenComponent;
    @FXML
    private StatisticMainController statisticsScreenComponentController;
    @FXML
    private HBox statisticsScreenComponent;
    @FXML
    private FlowsExecutionScreenController flowExecutionScreenComponentController;
    @FXML
    private ScrollPane flowExecutionScreenComponent;
    @FXML
    private FlowExecutionHistoryController flowExecutionHistoryScreenComponentController;
    @FXML
    private ScrollPane flowExecutionHistoryScreenComponent;
    @FXML
    private Tab statisticsButton;
    @FXML
    private Tab executionsHistoryButton;
    @FXML
    private Tab flowsExecutionButton;
    @FXML
    private Tab flowDefinitionButton;
    @FXML
    private TabPane optionsTabPane;

    private SimpleBooleanProperty statisticsButtonProperty;
    private SimpleBooleanProperty executionsHistoryButtonProperty;
    private SimpleBooleanProperty flowsExecutionButtonProperty;
    private SimpleBooleanProperty flowDefinitionButtonProperty;

    public BodyController()
    {
        statisticsButtonProperty =  new SimpleBooleanProperty(false);
        executionsHistoryButtonProperty =  new SimpleBooleanProperty(false);
        flowsExecutionButtonProperty =  new SimpleBooleanProperty(false);
        flowDefinitionButtonProperty =  new SimpleBooleanProperty(true);
    }


    @FXML
    public void initialize() {
        if (flowDefinitionScreenComponentController != null && statisticsScreenComponentController != null  && flowExecutionScreenComponentController != null && flowExecutionHistoryScreenComponentController != null) {
            flowDefinitionScreenComponentController.setMainController(this);
            statisticsScreenComponentController.setMainController(this);
            flowExecutionScreenComponentController.setMainController(this);
            flowExecutionHistoryScreenComponentController.setMainController(this);
        }
        setTabsListener();
        setTabAnimation();
    }

    private void setTabAnimation() {
        optionsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null && mainController.getHeaderComponentController().getCheckboxValueProperty().getValue()) {
                animateTabContent(newTab);
            }
        });
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

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
        flowDefinitionScreenComponentController.setSystemEngine(systemEngine);
        statisticsScreenComponentController.setSystemEngine(systemEngine);
        flowExecutionScreenComponentController.setSystemEngine(systemEngine);
        flowExecutionHistoryScreenComponentController.setSystemEngine(systemEngine);
    }

    public void updatePushTabButtons()
    {
        flowDefinitionButton.setDisable(false);
        flowDefinitionScreenComponentController.setListFlowsDetails();
    }
    public void updateButtons()
    {
        statisticsButton.setDisable(false);
        statisticsScreenComponentController.updateVisible();
        executionsHistoryButton.setDisable(false);
    }

    public void updateExecuteFlowButton(String flowName)
    {
        flowsExecutionButton.setDisable(false);
        flowExecutionScreenComponentController.updateDetailsFlowExecution(flowName, false);
    }
    @FXML
    void executionsHistoryButtonAction(Event event) {
        List<DTOFullDetailsPastRun> flowsExecutedList = systemEngine.getFlowsExecutedDataDTOHistory();
        flowExecutionHistoryScreenComponentController.updateListOfExecutedFlows(flowsExecutedList);
    }


    @FXML
    void flowDefinitionButtonAction(Event event) {
        flowDefinitionScreenComponentController.setListFlowsDetails();
        flowDefinitionScreenComponentController.getSelectedFlowDetailsComponent().setVisible(false);
    }

    @FXML
    void flowsExecutionButtonAction(Event event) {

    }

    @FXML
    void statisticsButtonAction(Event event) {

        DTOStatistics statistics = systemEngine.readStatistics();
        if(statistics != null)
            statisticsScreenComponentController.setTableView(statistics);
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
                statisticsButtonProperty.set(false);
            } else if (newTab == executionsHistoryButton) {
                flowDefinitionButtonProperty.set(false);
                executionsHistoryButtonProperty.set(true);
                flowsExecutionButtonProperty.set(false);
                statisticsButtonProperty.set(false);
            } else if (newTab == flowsExecutionButton) {
                flowDefinitionButtonProperty.set(false);
                executionsHistoryButtonProperty.set(false);
                flowsExecutionButtonProperty.set(true);
                statisticsButtonProperty.set(false);
            } else if (newTab == statisticsButton) {
                flowDefinitionButtonProperty.set(false);
                executionsHistoryButtonProperty.set(false);
                flowsExecutionButtonProperty.set(false);
                statisticsButtonProperty.set(true);
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

    public SimpleBooleanProperty isStatisticsButtonProperty() {
        return statisticsButtonProperty;
    }


    public void initListener() {
        flowDefinitionScreenComponentController.initListener();
        statisticsScreenComponentController.initListener();
        flowExecutionScreenComponentController.initListener();
        flowExecutionHistoryScreenComponentController.initListener();

        this.getMainController().getHeaderComponentController().getHeaderBodyComponentController().getIsFileCorrectProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue){
                        optionsTabPane.getSelectionModel().select(flowDefinitionButton);
                    }
                    else
                        flowDefinitionButton.setDisable(true);
                });

        this.flowDefinitionButtonProperty
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue){
                        mainController.getHeaderComponentController().getHeaderBodyComponentController().getLoadFileButton().setDisable(false);
                    }
                    else
                        mainController.getHeaderComponentController().getHeaderBodyComponentController().getLoadFileButton().setDisable(true);
                });
        this.mainController.getHeaderComponentController().getHeaderBodyComponentController().getLoadFileButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        flowsExecutionButton.setDisable(true);
                        executionsHistoryButton.setDisable(true);
                        statisticsButton.setDisable(true);
                        flowExecutionHistoryScreenComponentController.clearDetails();
                        statisticsScreenComponentController.clearData();
                    }
                });
        
    }

    public FlowExecutionHistoryController getFlowExecutionHistoryScreenComponentController() {
        return flowExecutionHistoryScreenComponentController;
    }

    public ScrollPane getFlowExecutionScreenComponent() {
        return flowExecutionScreenComponent;
    }


}
