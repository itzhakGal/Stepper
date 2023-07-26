package adminComponents.screenOne;

import adminComponents.mainScreen.body.BodyController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import stepper.role.RoleImpl;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOSavaNewInfoForUser;
import utilWebApp.DTOUserDataFullInfo;
import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import static util.Constants.REFRESH_RATE;

public class UsersManagementController implements Closeable {

    private Timer timer;
    private TimerTask listRefresher;
    private TimerTask userDataInfoRefresher;
    private BodyController mainbodyController;
    @FXML
    private ListView<String> listOfUsers;
    @FXML
    private Label userName;
    @FXML
    private ListView<String> listOfRoles;
    @FXML
    private ListView<String> listOfFlowsAvailable;
    @FXML
    private ListView<String> totalFlowsPerformed;
    @FXML
    private CheckBox isManager;
    @FXML
    private AnchorPane assignRolesAnchor;
    @FXML
    private Button savaChangeButton;
    @FXML
    private Button autoUpdatesButton;
    @FXML
    private Label labelMassage;
    @FXML
    private TextField countListFlowsAvailable;
    @FXML
    private TextField countFlowsPerformed;
    private List<String> listRolesToAddToTheUser;
    private List<String> listRolesToRemoveFromTheUser;
    private String userSelected;
    private SimpleBooleanProperty checkboxIsManagerProperty;
    private SimpleStringProperty chosenUserFromListProperty;
    private SimpleStringProperty chosenRoleFromListProperty;
    @FXML
    private GridPane availableAndSelectedRolesComponent;
    @FXML
    private AvailableAndSelectedRolesController availableAndSelectedRolesComponentController;

    public void initialize() {
        chosenUserFromListProperty = new SimpleStringProperty();
        chosenRoleFromListProperty = new SimpleStringProperty();
        listRolesToRemoveFromTheUser = new ArrayList<>();
        listRolesToAddToTheUser = new ArrayList<>();

        if (availableAndSelectedRolesComponentController != null ) {
            availableAndSelectedRolesComponentController.setMainController(this);
        }
        setupCheckboxBinding();
    }
    private void setupCheckboxBinding() {
        checkboxIsManagerProperty = new SimpleBooleanProperty();
        checkboxIsManagerProperty.bind(isManager.selectedProperty());
    }
    public SimpleStringProperty getChosenUserFromListProperty() {
        return this.chosenUserFromListProperty;
    }
    public SimpleStringProperty getChosenRoleFromListProperty() {
        return this.chosenRoleFromListProperty;
    }
    public SimpleBooleanProperty getCheckboxIsManagerProperty() {
        return this.checkboxIsManagerProperty;
    }
    public void setMainController(BodyController mainController) {
        this.mainbodyController = mainController;
    }
    public void init(BodyController bodyController) {

        getChosenUserFromListProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue == null) {
                    this.userSelected = newValue;
                    handleUserSelection();
                }

                else if (!this.userSelected.equals(newValue)) {
                    this.userSelected = newValue;
                    cleanListsData();
                    handleUserSelection();
                }
        });

        availableAndSelectedRolesComponentController.initListener();
    }

    public void cleanListsData()
    {
        userName.setText("");
        isManager.setSelected(false);
        listOfRoles.getItems().clear();
        listOfFlowsAvailable.getItems().clear();
        totalFlowsPerformed.getItems().clear();
        labelMassage.setText("");
        countListFlowsAvailable.setText("");
        countFlowsPerformed.setText("");
        availableAndSelectedRolesComponentController.cleanData();
    }
    public void handleUserSelection() {

        String finalUrl = HttpUrl
                .parse(Constants.USER_DATA_INFO_IN_ADMIN)
                .newBuilder()
                .addQueryParameter("username", this.userSelected)
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
                        DTOUserDataFullInfo dtoUserDataFullInfo = new Gson().fromJson(res, new TypeToken<DTOUserDataFullInfo>(){}.getType());
                        Platform.runLater(() -> {
                            updateUserFullData(dtoUserDataFullInfo);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    public void updateUserFullData(DTOUserDataFullInfo userDataFullInfo) {

        if(userDataFullInfo.getUser() == null)
            return;

        // תוסיף רק את הרולים שאין ליוזר שיבחר מהם עוד להוספה
        List<String> selectedAssignedRoles = new ArrayList<>();

        for(String role : userDataFullInfo.getAllRoleInSystem())
        {
            if(!userDataFullInfo.getUser().getAssociatedRole().containsKey(role))
            {
                selectedAssignedRoles.add(role);
            }
        }

        List<String> totalFlowsPreformedByUser = userDataFullInfo.getTotalFlowPreformedByUser();   // לא מאותל כראוי

        Map<String, RoleImpl> associatedRoleMap = userDataFullInfo.getUser().getAssociatedRole();
        List<String> listOfRoles = new ArrayList<>(associatedRoleMap.keySet());

        List<String> listOfFlowAvailable = new ArrayList<>();
        Set<String> uniqueFlows = new HashSet<>(); // Use a set to store unique flow names
        for (RoleImpl role : associatedRoleMap.values()) {
            Set<String> allowedFlows = role.getFlowsAllowed();
            // Add unique flow names to the set
            uniqueFlows.addAll(allowedFlows);
        }

        // Add the unique flow names from the set to the list
        listOfFlowAvailable.addAll(uniqueFlows);

        updateLists(userDataFullInfo.getUser().getUserName(), userDataFullInfo.getUser().getIsManager(), listOfRoles , listOfFlowAvailable, selectedAssignedRoles, totalFlowsPreformedByUser);

    }
    public void updateLists(String userName, Boolean isManager, List<String> listOfRoles, List<String> listOfFlowAvailable, List<String> selectedAssignedRoles, List<String> totalFlowsPreformedByUser) {

        this.userName.setText(userName);


        if(isManager)
            this.isManager.setSelected(true);
        else
            this.isManager.setSelected(false);

        ObservableList<String> itemsListRoles = this.listOfRoles.getItems();
        itemsListRoles.clear();
        itemsListRoles.addAll(listOfRoles);

        ObservableList<String> itemsListOfFlowAvailable = this.listOfFlowsAvailable.getItems();
        itemsListOfFlowAvailable.clear();
        itemsListOfFlowAvailable.addAll(listOfFlowAvailable);

        int countFlows = listOfFlowAvailable.size();
        countListFlowsAvailable.setText(String.valueOf(countFlows));

        ObservableList<String> itemsTotalFlowsPreformedByUser = this.totalFlowsPerformed.getItems();
        itemsTotalFlowsPreformedByUser.clear();
        itemsTotalFlowsPreformedByUser.addAll(totalFlowsPreformedByUser);

        int countFlowsPreformed = totalFlowsPreformedByUser.size();
        countFlowsPerformed.setText(String.valueOf(countFlowsPreformed));

        createSelectedAssignedRolesCheckBoxTreeItem(selectedAssignedRoles, listOfRoles);

    }

    private void createSelectedAssignedRolesCheckBoxTreeItem(List<String> selectedAssignedRoles, List<String> listOfRoles) {

        availableAndSelectedRolesComponent.setVisible(true);

        availableAndSelectedRolesComponentController.insertItemsIntoSourceListView(selectedAssignedRoles, listOfRoles);
    }

    private void updateUsersList(List<String> usersNames) {
        Platform.runLater(() -> {
            ObservableList<String> items = listOfUsers.getItems();
            items.clear();
            items.addAll(usersNames);
            cleanRightScreen();
        });
    }

    private void cleanRightScreen() {
        if(!listOfUsers.getItems().contains(userName.getText()))
            cleanListsData();
    }

    public void startUserListRefresher() {
        listRefresher = new UserListRefresher(
                this::updateUsersList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    @FXML
    void savaChangeButtonAction(ActionEvent event) {

        DTOSavaNewInfoForUser dtoSavaNewInfoForUser = new DTOSavaNewInfoForUser(this.userSelected , this.listRolesToAddToTheUser,this.listRolesToRemoveFromTheUser,  this.isManager.isSelected());

        String finalUrl = HttpUrl
                .parse(Constants.SAVA_NEW_DATA_USER)
                .newBuilder()
                .build()
                .toString();

        String userInfoJson = new Gson().toJson(dtoSavaNewInfoForUser);
        RequestBody body = RequestBody.create(userInfoJson.getBytes());
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
        String finalUrl = HttpUrl
                .parse(Constants.USER_DATA_INFO_IN_ADMIN)
                .newBuilder()
                .addQueryParameter("username", this.userSelected)
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
                        DTOUserDataFullInfo dtoUserDataFullInfo = new Gson().fromJson(res, new TypeToken<DTOUserDataFullInfo>(){}.getType());
                        Platform.runLater(() -> {
                            updateUserFullData(dtoUserDataFullInfo);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }
    @FXML
    void isManagerAction(ActionEvent event) {

    }
    @FXML
    void listOfFlowsAvailableAction(ActionEvent event) {

    }
    @FXML
    void listOfRolesAction(ActionEvent event) {

    }
    @FXML
    void listOfUsersAction(ActionEvent event) {

    }
    @FXML
    void totalFlowsPerformedAction(ActionEvent event) {

    }

    public ListView<String> getListOfUsers() {
        return listOfUsers;
    }

    @Override
    public void close() {
        listOfUsers.getItems().clear();
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }

    public List<String> getListRolesToAddToTheUser() {
        return listRolesToAddToTheUser;
    }

    public List<String> getListRolesToRemoveFromTheUser() {
        return listRolesToRemoveFromTheUser;
    }
}
