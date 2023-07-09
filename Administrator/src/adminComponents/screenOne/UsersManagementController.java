package adminComponents.screenOne;

import adminComponents.mainScreen.body.BodyController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import stepper.systemEngine.SystemEngineInterface;
import util.Constants;

import java.io.Closeable;
import java.util.*;

import static util.Constants.REFRESH_RATE;


public class UsersManagementController implements Closeable {

    private Timer timer;
    private TimerTask listRefresher;
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
    private CheckBox roleNameCheckBox;

    private final SimpleStringProperty chosenUser = new SimpleStringProperty();
    private final Set<String> currentUsers = new HashSet<>();

    public void setMainController(BodyController mainController) {
        this.mainbodyController = mainController;
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
    void roleNameCheckBoxAction(ActionEvent event) {

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
