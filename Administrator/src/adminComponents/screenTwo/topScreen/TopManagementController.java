package adminComponents.screenTwo.topScreen;

import adminComponents.mainScreen.body.BodyController;
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
    private List<String> listFlowsToAddToTheRole;
    private List<String> listUserToAddToTheRole;
    private List<String> allFlowsInTheSystem;
    private SimpleStringProperty chosenRoleFromListProperty;
    public void initialize() {
        chosenRoleFromListProperty = new SimpleStringProperty();
        this.allFlowsInTheSystem = new ArrayList<>();
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

    }
    public void cleanListsData()
    {
        allowedFlowsList.getItems().clear();
        listOfUsersConnected.getItems().clear();
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
        /*List<String> selectedAssignedFlowToRole = new ArrayList<>();

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
        }*/

        updateLists(roleDataFullInfo,roleDataFullInfo.getAllFlowsInTheSystem(), roleDataFullInfo.getAllUsersInTheSystem());


    }
    public void updateLists(DTORoleDataFullInfo roleDataFullInfo, List<String> selectedAssignedFlowToRole, List<String> selectedAssignedUserToRole) {

        this.roleName.setText(roleDataFullInfo.getRoleData().getRoleName());
        this.roleDescription.setText(roleDataFullInfo.getRoleData().getDescription());

        ObservableList<String> itemsFlowsList = this.allowedFlowsList.getItems();
        for (String flow : roleDataFullInfo.getRoleData().getAllowedFlows()) {
            if (!itemsFlowsList.contains(flow)) {
                itemsFlowsList.add(flow);
            }
        }

        ObservableList<String> itemsListOfUserConnected = this.listOfUsersConnected.getItems();
        for (String user : roleDataFullInfo.getAllUserConnectedToRole()) {
            if (!itemsListOfUserConnected.contains(user)) {
                itemsListOfUserConnected.add(user);
            }
        }

        this.listFlowsToAddToTheRole = createSelectedAssignedFlowsCheckBoxTreeItem(selectedAssignedFlowToRole);
        this.listUserToAddToTheRole = createSelectedAssignedUsersCheckBoxTreeItem(selectedAssignedUserToRole);

    }
    public List<String> createSelectedAssignedUsersCheckBoxTreeItem(List<String> selectedAssignedUserToRole) {

        List<String> listUserToAddToTheRole = new ArrayList<>();
        CheckBoxTreeItem<String> rootItem = new  CheckBoxTreeItem<String>("Assign User To User Roles");

        for(String userName : selectedAssignedUserToRole) {
            CheckBoxTreeItem<String> role = new CheckBoxTreeItem<String>(userName);
            rootItem.getChildren().add(role);
        }

        CheckTreeView<String> checkTreeView = new CheckTreeView<>(rootItem);

        checkTreeView.getCheckModel().getCheckedItems().addListener(new ListChangeListener<TreeItem<String>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<String>> c) {
                //checkTreeView.getCheckModel().getCheckedItems();
                while (c.next()) {
                    if (c.wasAdded()) {
                        listUserToAddToTheRole.addAll(
                                c.getAddedSubList()
                                        .stream()
                                        .map(TreeItem::getValue)
                                        .collect(Collectors.toList())
                        );
                    }
                    if (c.wasRemoved()) {
                        listUserToAddToTheRole.removeAll(
                                c.getRemoved()
                                        .stream()
                                        .map(TreeItem::getValue)
                                        .collect(Collectors.toList())
                        );
                    }
                }
            }
        });

        assignUserAnchor.getChildren().clear();
        assignUserAnchor.getChildren().add(checkTreeView);

        return listUserToAddToTheRole;

    }
    public List<String> createSelectedAssignedFlowsCheckBoxTreeItem(List<String> selectedAssignedFlowToRole) {

        List<String> listFlowsToAddToTheRole = new ArrayList<>();
        CheckBoxTreeItem<String> rootItem = new  CheckBoxTreeItem<String>("Assign Flows To Role");

        for(String flowName : selectedAssignedFlowToRole) {
            CheckBoxTreeItem<String> role = new CheckBoxTreeItem<String>(flowName);
            rootItem.getChildren().add(role);
        }

        CheckTreeView<String> checkTreeView = new CheckTreeView<>(rootItem);

        checkTreeView.getCheckModel().getCheckedItems().addListener(new ListChangeListener<TreeItem<String>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<String>> c) {
                //checkTreeView.getCheckModel().getCheckedItems();
                while (c.next()) {
                    if (c.wasAdded()) {
                        listFlowsToAddToTheRole.addAll(
                                c.getAddedSubList()
                                        .stream()
                                        .map(TreeItem::getValue)
                                        .collect(Collectors.toList())
                        );
                    }
                    if (c.wasRemoved()) {
                        listFlowsToAddToTheRole.removeAll(
                                c.getRemoved()
                                        .stream()
                                        .map(TreeItem::getValue)
                                        .collect(Collectors.toList())
                        );
                    }
                }
            }
        });

        assignFlowsAnchor.getChildren().clear();
        assignFlowsAnchor.getChildren().add(checkTreeView);

        return listFlowsToAddToTheRole;
    }
    @FXML
    void savaChangeButtonAction(ActionEvent event) {
        DTOSavaNewInfoForRole dtoSavaNewInfoForRole = new DTOSavaNewInfoForRole(this.roleSelected , this.listFlowsToAddToTheRole, this.listUserToAddToTheRole);

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
        //mainRolesManagementController.getLowerManagementComponentController().createNewRole();
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
}
