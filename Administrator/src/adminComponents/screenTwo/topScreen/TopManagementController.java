package adminComponents.screenTwo.topScreen;

import adminComponents.screenTwo.RolesManagementController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import stepper.systemEngine.SystemEngineInterface;
import utilWebApp.DTORole;

import java.io.Closeable;
import java.util.*;

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
    private ListView<?> allowedFlowsList;

    @FXML
    private CheckBox flowNameCheckBox;

    @FXML
    private CheckBox userNameCheckBox;

    @FXML
    private Label roleName;

    @FXML
    private ListView<String> listOfUsersConnected;

    @FXML
    private Button newRoleButton;

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


    @FXML
    void flowNameCheckBoxAction(ActionEvent event) {

    }
    @FXML
    void userNameCheckBoxAction(ActionEvent event) {

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
