package adminComponents.mainScreen.body;

import adminComponents.mainScreen.app.AppController;
import adminComponents.screenOne.UsersManagementController;
import adminComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import adminComponents.screenTwo.RolesManagementController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import stepper.systemEngine.SystemEngineInterface;
import adminComponents.screenFour.statisticScreen.StatisticMainController;


public class BodyController {

    private AppController mainController;
    //private SystemEngineInterface systemEngine;
    @FXML
    private UsersManagementController usersManagementComponentController;
    @FXML
    private ScrollPane usersManagementComponent;

    @FXML
    private ScrollPane rolesManagementComponent;
    @FXML
    private RolesManagementController rolesManagementComponentController;

    @FXML
    private StatisticMainController statisticsScreenComponentController;
    @FXML
    private HBox statisticsScreenComponent;
    @FXML
    private FlowExecutionHistoryController flowExecutionHistoryScreenComponentController;
    @FXML
    private ScrollPane flowExecutionHistoryScreenComponent;
    @FXML
    private Tab statisticsButton;
    @FXML
    private Tab executionsHistoryButton;
    @FXML
    private Tab rolesManagementButton;
    @FXML
    private Tab usersManagementButton;
    @FXML
    private TabPane optionsTabPane;

    private SimpleBooleanProperty statisticsButtonProperty;
    private SimpleBooleanProperty executionsHistoryButtonProperty;
    private SimpleBooleanProperty rolesManagementButtonProperty;
    private SimpleBooleanProperty usersManagementButtonProperty;

    public BodyController()
    {
        statisticsButtonProperty =  new SimpleBooleanProperty(false);
        executionsHistoryButtonProperty =  new SimpleBooleanProperty(false);
        rolesManagementButtonProperty =  new SimpleBooleanProperty(false);
        usersManagementButtonProperty =  new SimpleBooleanProperty(true);
    }


    @FXML
    public void initialize() {
        if (usersManagementComponentController != null && statisticsScreenComponentController != null && flowExecutionHistoryScreenComponentController != null && rolesManagementComponentController != null) {
            usersManagementComponentController.setMainController(this);
            statisticsScreenComponentController.setMainController(this);
            rolesManagementComponentController.setMainController(this);
            flowExecutionHistoryScreenComponentController.setMainController(this);
        }
        setTabsListener();
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        //this.systemEngine = systemEngine;
        //usersManagementComponentController.setSystemEngine(systemEngine);
        //statisticsScreenComponentController.setSystemEngine(systemEngine);
        //flowExecutionScreenComponentController.setSystemEngine(systemEngine);
        //flowExecutionHistoryScreenComponentController.setSystemEngine(systemEngine);
    }

    @FXML
    void rolesManagementButtonAction(Event event) {

    }

    @FXML
    void usersManagementButtonAction(Event event) {

    }

    public void updatePushTabButtons()
    {
        usersManagementButton.setDisable(false);
        usersManagementComponentController.startUserListRefresher();
        //flowDefinitionScreenComponentController.setListFlowsDetails();
    }
    public void updateButtons()
    {
        statisticsButton.setDisable(false);
        statisticsScreenComponentController.updateVisible();
        executionsHistoryButton.setDisable(false);
    }

    public void updateExecuteFlowButton(String flowName)
    {
        rolesManagementButton.setDisable(false);
        //flowExecutionScreenComponentController.updateDetailsFlowExecution(flowName, false);
    }
    @FXML
    void executionsHistoryButtonAction(Event event) {
        //List<DTOFullDetailsPastRun> flowsExecutedList = systemEngine.getFlowsExecutedDataDTOHistory();
        //flowExecutionHistoryScreenComponentController.updateListOfExecutedFlows(flowsExecutedList);
    }


    @FXML
    void flowDefinitionButtonAction(Event event) {
        //flowDefinitionScreenComponentController.setListFlowsDetails();
        //flowDefinitionScreenComponentController.getSelectedFlowDetailsComponent().setVisible(false);
    }

    @FXML
    void flowsExecutionButtonAction(Event event) {

    }

    @FXML
    void statisticsButtonAction(Event event) {

        //DTOStatistics statistics = systemEngine.readStatistics();
        //if(statistics != null)
            //statisticsScreenComponentController.setTableView(statistics);
    }


    public void openTabUserManager() {
        // Open the Tab of flowDefinition
        optionsTabPane.getSelectionModel().select(usersManagementButton);
        rolesManagementButton.setDisable(false);
    }

    public void openFlowExecutionTab() {
        // Open the Tab of flowExecution
        optionsTabPane.getSelectionModel().select(rolesManagementButton);
    }

    private void setTabsListener() {
        optionsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == usersManagementButton) {
                usersManagementButtonProperty.set(true);
                executionsHistoryButtonProperty.set(false);
                rolesManagementButtonProperty.set(false);
                statisticsButtonProperty.set(false);
            } else if (newTab == executionsHistoryButton) {
                usersManagementButtonProperty.set(false);
                executionsHistoryButtonProperty.set(true);
                rolesManagementButtonProperty.set(false);
                statisticsButtonProperty.set(false);
            } else if (newTab == rolesManagementButton) {
                usersManagementButtonProperty.set(false);
                executionsHistoryButtonProperty.set(false);
                rolesManagementButtonProperty.set(true);
                statisticsButtonProperty.set(false);
            } else if (newTab == statisticsButton) {
                usersManagementButtonProperty.set(false);
                executionsHistoryButtonProperty.set(false);
                rolesManagementButtonProperty.set(false);
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

    public SimpleBooleanProperty isUsersManagementButtonProperty() {
        return usersManagementButtonProperty;
    }

    public SimpleBooleanProperty isRolesManagementButtonProperty() {
        return rolesManagementButtonProperty;
    }

    public SimpleBooleanProperty isStatisticsButtonProperty() {
        return statisticsButtonProperty;
    }


    public void initListener() {
        usersManagementComponentController.initListener();
        statisticsScreenComponentController.initListener();
        //flowExecutionScreenComponentController.initListener();
        flowExecutionHistoryScreenComponentController.initListener();

        this.getMainController().getHeaderComponentController().getHeaderBodyComponentController().getIsFileCorrectProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue){
                        optionsTabPane.getSelectionModel().select(usersManagementButton);
                    }
                    else
                        usersManagementButton.setDisable(true);
                });

        this.usersManagementButtonProperty
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
                        //שונה
                        //rolesManagementButton.setDisable(true);
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


    public void updateRolesScreenTwo() {
        rolesManagementComponentController.updateRolesScreenTwo();
    }

    public void init() {
        usersManagementComponentController.init(this);
        //rolesManagementComponentController.init(this);
        //flowExecutionHistoryScreenComponentController.init(this);
        //statisticsScreenComponentController.init(this);

    }
}
