package adminComponents.screenOne;

import adminComponents.mainScreen.body.BodyController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import stepper.role.Role;
import stepper.systemEngine.SystemEngineInterface;
import utilWebApp.DTOUserDataFullInfo;

import java.io.Closeable;
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
    private ListView<String> selectRoleNameList;

    private final SimpleStringProperty chosenUserFromList = new SimpleStringProperty();
    private final SimpleStringProperty chosenRoleFromList = new SimpleStringProperty();

    public SimpleStringProperty getChosenUserFromList() {
        return this.chosenUserFromList;
    }

    public SimpleStringProperty getChosenRoleFromList() {
        return this.chosenRoleFromList;
    }
    public void setMainController(BodyController mainController) {
        this.mainbodyController = mainController;
    }

    public void initListener() {

        /*this.chosenUserFromList.addListener((observable, oldValue, newValue) -> {
                    if (!(newValue.equals(oldValue))){
                        optionsTabPane.getSelectionModel().select(usersManagementButton);
                    }
                });*/

    }


    public void init(BodyController bodyController) {

        listOfUsers.setOnMouseClicked(event -> handleUserSelection());
        //selectRoleNameList.setOnMouseClicked(event -> handleSelectRoleName());

    }

    private void handleUserSelection() {

        String selectedUser = listOfUsers.getSelectionModel().getSelectedItem();

        userDataInfoRefresher = new UserInfoRefresher(
                selectedUser,
                this::updateUserFullData);
        timer = new Timer();
        timer.schedule(userDataInfoRefresher, REFRESH_RATE, REFRESH_RATE);

    }


    private void updateUserFullData(DTOUserDataFullInfo userDataFullInfo) {

        List<String> selectedAssignedRoles = userDataFullInfo.getAllRoleInSystem();
        //List<String> totalFlowsPreformedByUser = userDataFullInfo.getTotalFlowPreformedByUser();   // לא מאותל כראוי

        Map<String, Role> associatedRoleMap = userDataFullInfo.getUser().getAssociatedRole();
        List<String> listOfRoles = new ArrayList<>(associatedRoleMap.keySet());

        List<String> listOfFlowAvailable = new ArrayList<>();
        for (Role role : associatedRoleMap.values()) {
            Set<String> allowedFlows = role.getFlowsAllowed();
            // Add all the allowed flows to the list
            listOfFlowAvailable.addAll(allowedFlows);
        }

        Platform.runLater(() -> {
            updateLists(userDataFullInfo.getUser().getUserName(), listOfRoles, listOfFlowAvailable, selectedAssignedRoles);
           
        });
    }
    
    /*public void updateLists(String userName, List<String> listOfRoles, List<String> listOfFlowAvailable, List<String> selectedAssignedRoles)
    {
        this.userName.setText(userName);
        
        ObservableList<String> itemsListRoles = this.listOfRoles.getItems();
        itemsListRoles.clear();
        itemsListRoles.addAll(listOfRoles);


        ObservableList<String> itemsListOfFlowAvailable = this.listOfFlowsAvailable.getItems();
        itemsListOfFlowAvailable.clear();
        itemsListOfFlowAvailable.addAll(listOfFlowAvailable);

        ObservableList<String> itemsTotalFlowsPreformedByUser = this.totalFlowsPerformed.getItems();
        itemsListOfFlowAvailable.clear();
        itemsListOfFlowAvailable.addAll(totalFlowsPreformedByUser);

        ObservableList<String> itemsListSelectedAssignedRoles = this.selectRoleNameList.getItems();
        itemsListSelectedAssignedRoles.clear();
        itemsListSelectedAssignedRoles.addAll(selectedAssignedRoles);
    }*/
    public void updateLists(String userName, List<String> listOfRoles, List<String> listOfFlowAvailable, List<String> selectedAssignedRoles) {
        this.userName.setText(userName);

        ObservableList<String> itemsListRoles = this.listOfRoles.getItems();
        for (String role : listOfRoles) {
            if (!itemsListRoles.contains(role)) {
                itemsListRoles.add(role);
            }
        }

        ObservableList<String> itemsListOfFlowAvailable = this.listOfFlowsAvailable.getItems();
        for (String flow : listOfFlowAvailable) {
            if (!itemsListOfFlowAvailable.contains(flow)) {
                itemsListOfFlowAvailable.add(flow);
            }
        }

        /*ObservableList<String> itemsTotalFlowsPreformedByUser = this.totalFlowsPerformed.getItems();
        itemsListOfFlowAvailable.clear();
        itemsListOfFlowAvailable.addAll(totalFlowsPreformedByUser);*/

        ObservableList<String> itemsListSelectedAssignedRoles = this.selectRoleNameList.getItems();
        for (String selectedRole : selectedAssignedRoles) {
            if (!itemsListSelectedAssignedRoles.contains(selectedRole)) {
                itemsListSelectedAssignedRoles.add(selectedRole);
            }
        }
    }

    private void updateUsersList(List<String> usersNames) {
        Platform.runLater(() -> {
            ObservableList<String> items = listOfUsers.getItems();
            items.clear();
            items.addAll(usersNames);
        });
    }

    public void startUserListRefresher() {
        listRefresher = new UserListRefresher(
                this::updateUsersList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        //this.systemEngine = systemEngine;
    }

    @FXML
    void isManagerAction(ActionEvent event) {

    }

    @FXML
    void roleNameListAction(ActionEvent event) {

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

    @Override
    public void close() {
        listOfUsers.getItems().clear();
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }


}
