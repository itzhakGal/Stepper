package adminComponents.screenTwo.topScreen;

import adminComponents.mainScreen.body.BodyController;
import adminComponents.screenOne.AvailableAndSelectedRolesController;
import adminComponents.screenTwo.RolesManagementController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import okhttp3.*;
import org.controlsfx.control.CheckTreeView;
import org.jetbrains.annotations.NotNull;
import stepper.role.RoleImpl;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static util.Constants.REFRESH_RATE;

public class TopManagementController implements Closeable {

    private Timer timer;
    private TimerTask roleListRefresher;
    private RolesManagementController mainRolesManagementController;
    @FXML
    private Label roleDescription;
    @FXML
    private ListView<String> listOfRoles;
    @FXML
    private ListView<String> allowedFlowsList;
    @FXML
    private AnchorPane assignUserAnchor;
    @FXML
    private AnchorPane assignFlowsAnchor;
    @FXML
    private Label roleName;
    @FXML
    private ListView<String> listOfUsersConnected;
    @FXML
    private Button newRoleButton;
    @FXML
    private Button autoUpdatesButton;
    @FXML
    private Button savaChangeButton;
    @FXML
    private Label labelMassage;
    private String roleSelected;
    private List<String> allFlowsInTheSystem;
    private SimpleStringProperty chosenRoleFromListProperty;
    @FXML
    private GridPane availableAndSelectedUsersComponent;
    @FXML
    private AvailableAndSelectedUserController availableAndSelectedUsersComponentController;
    @FXML
    private GridPane availableAndSelectedFlowsComponent;
    @FXML
    private AvailableAndSelectedFlowsController availableAndSelectedFlowsComponentController;

    private List<String> listUsersToAddToTheRole;
    private List<String> listUsersToRemoveFromTheRole;

    private List<String> listFlowsToAddToTheRole;
    private List<String> listFlowsToRemoveFromTheRole;

    public void initialize() {
        chosenRoleFromListProperty = new SimpleStringProperty();
        this.allFlowsInTheSystem = new ArrayList<>();

        listUsersToAddToTheRole = new ArrayList<>();
        listUsersToRemoveFromTheRole = new ArrayList<>();
        listFlowsToAddToTheRole = new ArrayList<>();
        listFlowsToRemoveFromTheRole = new ArrayList<>();

        if (availableAndSelectedUsersComponentController != null && availableAndSelectedFlowsComponentController != null) {
            availableAndSelectedUsersComponentController.setMainController(this);
            availableAndSelectedFlowsComponentController.setMainController(this);
        }
    }
    public void init(BodyController bodyController) {

        getChosenRoleFromListProperty().addListener((observable, oldValue, newValue) -> {

            if (oldValue == null) {
                this.roleSelected = newValue;
                handleRoleSelection();
            }
            else if (!this.roleSelected.equals(newValue)) {
                this.roleSelected = newValue;
                cleanListsData();
                handleRoleSelection();
            }
        });

        availableAndSelectedFlowsComponentController.initListener();
        availableAndSelectedUsersComponentController.initListener();
    }
    public void cleanListsData()
    {
        allowedFlowsList.getItems().clear();
        listOfUsersConnected.getItems().clear();
        labelMassage.setText("");

    }
    public SimpleStringProperty getChosenRoleFromListProperty() {
        return this.chosenRoleFromListProperty;
    }
    public void handleRoleSelection() {

        String finalUrl = HttpUrl
                .parse(Constants.ROLE_DATA_INFO_IN_ADMIN)
                .newBuilder()
                .addQueryParameter("roleName", this.roleSelected)
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
                        String res = response.body().string();
                        DTORoleDataFullInfo dtoRoleDataFullInfo = new Gson().fromJson(res, new TypeToken<DTORoleDataFullInfo>(){}.getType());
                        allFlowsInTheSystem = dtoRoleDataFullInfo.getAllFlowsInTheSystem();
                        Platform.runLater(() -> {
                            updateRoleFullData(dtoRoleDataFullInfo);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    public void updateRoleFullData(DTORoleDataFullInfo roleDataFullInfo) {

        if(roleDataFullInfo.getRoleData() == null)
            return;

        // תוסיף רק את הפלואים שאין ליוזר שיבחר מהם עוד להוספה
        List<String> selectedAssignedFlowToRole = new ArrayList<>();

        for(String flow : roleDataFullInfo.getAllFlowsInTheSystem())
        {
            if(!roleDataFullInfo.getRoleData().getAllowedFlows().contains(flow))
            {
                selectedAssignedFlowToRole.add(flow);
            }
        }

        List<String> selectedAssignedUserToRole = new ArrayList<>();

        for(String user : roleDataFullInfo.getAllUsersInTheSystem())
        {
            if(!roleDataFullInfo.getAllUserConnectedToRole().contains(user))
            {
                selectedAssignedUserToRole.add(user);
            }
        }

        updateLists(roleDataFullInfo, selectedAssignedFlowToRole , selectedAssignedUserToRole);


    }
    public void updateLists(DTORoleDataFullInfo roleDataFullInfo, List<String> selectedAssignedFlowToRole, List<String> selectedAssignedUserToRole) {

        this.roleName.setText(roleDataFullInfo.getRoleData().getRoleName());
        this.roleDescription.setText(roleDataFullInfo.getRoleData().getDescription());

        ObservableList<String> itemsFlowsList = this.allowedFlowsList.getItems();
        itemsFlowsList.clear();
        itemsFlowsList.addAll(roleDataFullInfo.getRoleData().getAllowedFlows());


        ObservableList<String> itemsListOfUserConnected = this.listOfUsersConnected.getItems();
        itemsListOfUserConnected.clear();
        itemsListOfUserConnected.addAll(roleDataFullInfo.getAllUserConnectedToRole());

        createSelectedAssignedFlowsCheckBoxTreeItem(selectedAssignedFlowToRole, roleDataFullInfo.getRoleData().getAllowedFlows());
        createSelectedAssignedUsersCheckBoxTreeItem(selectedAssignedUserToRole, roleDataFullInfo.getAllUserConnectedToRole());

    }

    private void createSelectedAssignedFlowsCheckBoxTreeItem(List<String> selectedAssignedFlowToRole, Set<String> SetAllowedFlows) {

        availableAndSelectedFlowsComponent.setVisible(true);
        List<String> listAllowedFlows = new ArrayList<>(SetAllowedFlows);

        availableAndSelectedFlowsComponentController.insertItemsIntoSourceListView(selectedAssignedFlowToRole, listAllowedFlows);
    }

    private void createSelectedAssignedUsersCheckBoxTreeItem(List<String> selectedAssignedUserToRole, List<String> listUserConnectedToRole) {

        availableAndSelectedUsersComponent.setVisible(true);

        availableAndSelectedUsersComponentController.insertItemsIntoSourceListView(selectedAssignedUserToRole, listUserConnectedToRole);
    }
    @FXML
    void savaChangeButtonAction(ActionEvent event) {
        DTOSavaNewInfoForRole dtoSavaNewInfoForRole = new DTOSavaNewInfoForRole(this.roleSelected , this.listFlowsToAddToTheRole, this.listFlowsToRemoveFromTheRole, this.listUsersToAddToTheRole, this.listUsersToRemoveFromTheRole);

        String finalUrl = HttpUrl
                .parse(Constants.SAVA_NEW_DATA_ROLE)
                .newBuilder()
                .build()
                .toString();

        String roleInfoJson = new Gson().toJson(dtoSavaNewInfoForRole);
        RequestBody body = RequestBody.create(roleInfoJson.getBytes());
        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    handleFailure((e.getMessage()));
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    if (response.isSuccessful()) {
                        Platform.runLater(() -> {
                            labelMassage.setText("Saved.");
                        });
                    }
                }finally {
                    response.close();
                }
            }
        });
    }
    @FXML
    void autoUpdatesButtonAction(ActionEvent event) {
        cleanListsData();
        handleRoleSelection();
    }
    @FXML
    void listOfRolesAction(ActionEvent event) {

    }
    @FXML
    void listOfUsersConnectedAction(ActionEvent event) {

    }
    @FXML
    void newRoleButtonActivate(ActionEvent event) {
        newRoleButton.setDisable(true);
        mainRolesManagementController.getLowerManagementComponent().setVisible(true);
        mainRolesManagementController.getLowerManagementComponentController().updateListOfFlowInSystemFromServer();
    }
    @FXML
    void allowedFlowsListAction(ActionEvent event) {

    }
    public void updateRolesRefresher(Map<String, DTORole> roleMap) {

        List<String> keyList = new ArrayList<>(roleMap.keySet());
        List<DTORole> dtoRole = new ArrayList<>(roleMap.values()); // לא השתמשתי עדין
        Platform.runLater(() -> {
            ObservableList<String> items = listOfRoles.getItems();
            items.clear();
            items.addAll(keyList);
        });


    }
    public void startRolesListRefresher() {
        roleListRefresher = new RolesListRefresher(
                this::updateRolesRefresher);
        timer = new Timer();
        timer.schedule(roleListRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    public void setMainController(RolesManagementController rolesManagementController) {
        this.mainRolesManagementController = rolesManagementController;
    }
    public ListView<String> getListOfRoles() {
        return listOfRoles;
    }
    public List<String> getAllFlowsInTheSystem() {
        return allFlowsInTheSystem;
    }
    public Button getNewRoleButton() {
        return newRoleButton;
    }

    @Override
    public void close() {
        listOfRoles.getItems().clear();
        if (roleListRefresher != null && timer != null) {
            roleListRefresher.cancel();
            timer.cancel();
        }
    }

    public List<String> getListUsersToAddToTheRole() {
        return listUsersToAddToTheRole;
    }

    public List<String> getListUsersToRemoveFromTheRole() {
        return listUsersToRemoveFromTheRole;
    }

    public List<String> getListFlowsToAddToTheRole() {
        return listFlowsToAddToTheRole;
    }

    public List<String> getListFlowsToRemoveFromTheRole() {
        return listFlowsToRemoveFromTheRole;
    }

    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }
}
