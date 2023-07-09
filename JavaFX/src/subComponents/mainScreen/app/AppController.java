package subComponents.mainScreen.app;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import stepper.systemEngine.SystemEngine;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.mainScreen.body.BodyController;
import subComponents.mainScreen.header.HeaderController;

public class AppController {

    @FXML private ScrollPane headerComponent;
    //private VBox headerComponent;
    @FXML private ScrollPane bodyComponent;
    //private TabPane bodyComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private BodyController bodyComponentController;

    private Stage primaryStage;

    private SystemEngineInterface systemEngine = new SystemEngine();

    @FXML
    public void initialize() {
        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.setMainController(this);
            headerComponentController.setSystemEngine(systemEngine);
            bodyComponentController.setMainController(this);
            bodyComponentController.setSystemEngine(systemEngine);
            initListener();
        }
    }

    public void updatePushTabButtons()
    {
        bodyComponentController.updatePushTabButtons();
    }
    public void openTabFlowDefinition() {
        bodyComponentController.openTabFlowDefinition();
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
}
