package adminComponents.mainScreen.header;

import adminComponents.mainScreen.app.AppController;
import adminComponents.mainScreen.header.headerBody.HeaderBodyController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class HeaderController {

    private AppController mainController;
    @FXML
    private HeaderBodyController headerBodyComponentController;
    @FXML
    private GridPane headerBodyComponent;
    @FXML
    private Label titleLabel;
    @FXML
    private CheckBox animationCheckbox;

    private SimpleBooleanProperty checkboxValueProperty;

    @FXML
    public void initialize() {
        if (headerBodyComponentController != null) {
            headerBodyComponentController.setMainController(this);
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void updatePushTabButtons() {
        mainController.updatePushTabButtons();
    }

    public void openTabUserManager() {
        mainController.openTabUserManager();
    }

    public HeaderBodyController getHeaderBodyComponentController() {
        return headerBodyComponentController;
    }

    public SimpleBooleanProperty getCheckboxValueProperty() {
        return checkboxValueProperty;
    }

    public AppController getMainController() {
        return mainController;
    }

    public void updateRolesScreenTwo() {
        mainController.updateRolesScreenTwo();
    }
}
