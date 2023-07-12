package adminComponents.screenTwo.topScreen;

import adminComponents.screenTwo.RolesManagementController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.controlsfx.control.CheckTreeView;
import org.jetbrains.annotations.NotNull;
import stepper.role.RoleImpl;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTORole;
import utilWebApp.DTORoleDataFullInfo;
import utilWebApp.DTOUserDataFullInfo;

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

    private String roleSelected;

    private List<String> listFlowsToAddToTheRole;
    private List<String> listUserToAddToTheRole;

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

        updateLists(roleDataFullInfo,selectedAssignedFlowToRole, selectedAssignedUserToRole);


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

    private List<String> createSelectedAssignedUsersCheckBoxTreeItem(List<String> selectedAssignedUserToRole) {

        List<String> listUserToAddToTheRole = new ArrayList<>();
        CheckBoxTreeItem<String> rootItem = new  CheckBoxTreeItem<String>("Assign Flows To Role");

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

    private List<String> createSelectedAssignedFlowsCheckBoxTreeItem(List<String> selectedAssignedFlowToRole) {

        List<String> listFlowsToAddToTheRole = new ArrayList<>();
        CheckBoxTreeItem<String> rootItem = new  CheckBoxTreeItem<String>("Assign Roles To User");

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
    void listOfRolesAction(ActionEvent event) {

    }
    @FXML
    void listOfUsersConnectedAction(ActionEvent event) {

    }
    @FXML
    void newRoleButtonActivate(ActionEvent event) {

    }
    @FXML
    void allowedFlowsListAction(ActionEvent event) {

    }
    private void updateRolesRefresher(Map<String, DTORole> roleMap) {

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
    @Override
    public void close() {
        listOfRoles.getItems().clear();
        if (roleListRefresher != null && timer != null) {
            roleListRefresher.cancel();
            timer.cancel();
        }
    }
}
