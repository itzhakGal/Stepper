package clientComponents.mainScreen.header;

import clientComponents.mainScreen.app.AppController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import stepper.role.Role;
import stepper.role.RoleImpl;
import stepper.systemEngine.SystemEngineInterface;
import stepper.users.User;

import java.io.Closeable;
import java.util.*;

import static util.Constants.REFRESH_RATE;

public class HeaderClientController implements Closeable {
    private Timer timer;
    private TimerTask dataUserRefresher;
    private AppController mainController;
    @FXML
    private Label titleLabel;
    @FXML
    private Label clientName;
    @FXML
    private ListView<String> assignedRolesList;
    @FXML
    private Label isManager;

    @FXML
    public void initialize() {

    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void assignedRolesListActivate(ActionEvent event) {

    }

    private void updateUsersDataHeader(User userData) {

        String isUserManager;
        if(userData.isManager())
            isUserManager = "True";
        else
            isUserManager = "False";

        Map<String, RoleImpl> associatedRole = userData.getAssociatedRole();
        List<String> keyList = new ArrayList<>(associatedRole.keySet());

        Platform.runLater(() -> {
            ObservableList<String> items = assignedRolesList.getItems();
            items.clear();
            items.addAll(keyList);
            clientName.setText(userData.getUserName());
            isManager.setText(isUserManager);
        });
    }


    public void refresherDataUser() {
        dataUserRefresher = new HeaderDataRefresher(
                mainController.currentUserNameProperty().getValue(),
                this::updateUsersDataHeader);
        timer = new Timer();
        timer.schedule(dataUserRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    @Override
    public void close() {
        assignedRolesList.getItems().clear();
        if (dataUserRefresher != null && timer != null) {
            dataUserRefresher.cancel();
            timer.cancel();
        }
    }

    public Label getClientName() {
        return clientName;
    }

    public void setClientName(Label clientName) {
        this.clientName = clientName;
    }

    public Label getIsManager() {
        return isManager;
    }
}
