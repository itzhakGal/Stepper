package adminComponents.mainScreen.app;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import adminComponents.mainScreen.body.BodyController;
import adminComponents.mainScreen.header.HeaderController;

public class AppController {

    @FXML private ScrollPane headerComponent;
    @FXML private ScrollPane bodyComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private BodyController bodyComponentController;

    private Stage primaryStage;

    //private SystemEngineInterface systemEngine = new SystemEngine();

    @FXML
    public void initialize() {
        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.setMainController(this);
            bodyComponentController.setMainController(this);

            bodyComponentController.init();
            initListener();
        }
    }

    public void updatePushTabButtons()
    {
        bodyComponentController.updatePushTabButtons();
    }
    public void openTabUserManager() {
        bodyComponentController.openTabUserManager();
    }
    public void initListener() {
        headerComponentController.getHeaderBodyComponentController().getLoadFileButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        bodyComponentController.initListener();
                    }
                });
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public HeaderController getHeaderComponentController() {
        return headerComponentController;
    }

    public void updateRolesScreenTwo() {
        bodyComponentController.updateRolesScreenTwo();
    }
}
