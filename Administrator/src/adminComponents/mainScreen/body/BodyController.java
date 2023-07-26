package adminComponents.mainScreen.body;

import adminComponents.mainScreen.app.AppController;
import adminComponents.screenOne.UsersManagementController;
import adminComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import adminComponents.screenTwo.RolesManagementController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import adminComponents.screenFour.statisticScreen.StatisticMainController;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOListFullDetailsPastRunWeb;
import utils.DTOStatistics;
import java.io.IOException;
import java.util.List;


public class BodyController {

    private AppController mainController;
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

        usersManagementComponentController.getListOfUsers().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue == null)
                usersManagementComponentController.getChosenUserFromListProperty().set(newValue);
            else if(newValue == null)
                usersManagementComponentController.getChosenUserFromListProperty().set(oldValue);
            else if(!oldValue.equals(newValue))
                usersManagementComponentController.getChosenUserFromListProperty().set(newValue);
        });

        rolesManagementComponentController.getTopManagementComponentController().getListOfRoles().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue == null)
                rolesManagementComponentController.getTopManagementComponentController().getChosenRoleFromListProperty().set(newValue);
            else if(newValue == null)
                rolesManagementComponentController.getTopManagementComponentController().getChosenRoleFromListProperty().set(oldValue);
            else if(!oldValue.equals(newValue))
                rolesManagementComponentController.getTopManagementComponentController().getChosenRoleFromListProperty().set(newValue);
        });
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
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
        statisticsButton.setDisable(false);
        executionsHistoryButton.setDisable(false);
        usersManagementComponentController.startUserListRefresher();
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
    }
    @FXML
    void executionsHistoryButtonAction(Event event) {

        String finalUrl = HttpUrl
                .parse(Constants.LIST_FLOWS_EXECUTION)
                .newBuilder()
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
                try{
                if (response.isSuccessful()) {
                    DTOListFullDetailsPastRunWeb dtoListFullDetailsPastRun = new Gson().fromJson(response.body().string(), DTOListFullDetailsPastRunWeb.class);
                    List<DTOFullDetailsPastRunWeb> flowsExecutedList = dtoListFullDetailsPastRun.getDtoListFullDetailsPastRun();


                    Platform.runLater(() -> {
                        flowExecutionHistoryScreenComponentController.updateListOfExecutedFlows(flowsExecutedList);
                    });
                }}finally {
                    response.close();
                }
            }
        });
    }

    @FXML
    void statisticsButtonAction(Event event) {

        String finalUrl = HttpUrl
                .parse(Constants.STATISTICS_DATA)
                .newBuilder()
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
                try{
                    if (response.isSuccessful()) {
                        DTOStatistics statistics = new Gson().fromJson(response.body().string(), DTOStatistics.class);

                        Platform.runLater(() -> {
                            if (statistics != null)
                                statisticsScreenComponentController.setTableView(statistics);
                        });
                    }}finally {
                    response.close();
                }
            }
        });
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
        statisticsScreenComponentController.initListener();
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
        rolesManagementComponentController.init(this);
    }

    public HBox getStatisticsScreenComponent() {
        return statisticsScreenComponent;
    }

    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }
}
