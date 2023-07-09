package adminComponents.mainScreen.header;

import adminComponents.mainScreen.app.AppController;
import adminComponents.mainScreen.header.headerBody.HeaderBodyController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import stepper.systemEngine.SystemEngineInterface;

public class HeaderController {

    //private SystemEngineInterface systemEngine;
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

    public void setSystemEngine(SystemEngineInterface engineManager) {
        //this.systemEngine = engineManager;
        //this.headerBodyComponentController.setSystemEngine(engineManager);
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
}
