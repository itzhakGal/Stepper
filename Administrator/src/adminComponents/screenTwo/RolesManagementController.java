package adminComponents.screenTwo;

import adminComponents.mainScreen.body.BodyController;
import adminComponents.screenTwo.lowerScreen.LowerManagementController;
import adminComponents.screenTwo.topScreen.TopManagementController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import stepper.systemEngine.SystemEngineInterface;
import utils.DTOFullDetailsPastRun;

public class RolesManagementController {

    private BodyController mainBodyController;
    @FXML
    private GridPane topManagementComponent;
    @FXML
    private TopManagementController topManagementComponentController;
    @FXML
    private VBox lowerManagementComponent;
    @FXML
    private LowerManagementController lowerManagementComponentController;

    @FXML
    public void initialize() {
        if (topManagementComponentController != null && lowerManagementComponentController != null) {
            topManagementComponentController.setMainController(this);
            lowerManagementComponentController.setMainController(this);
        }
    }
    public void setMainController(BodyController mainBodyController) {
        this.mainBodyController = mainBodyController;
    }
    public void updateRolesScreenTwo() {
        topManagementComponentController.startRolesListRefresher();
    }
    public void init(BodyController bodyController) {
        topManagementComponentController.init(bodyController);
    }
    public BodyController getMainBodyController() {
        return mainBodyController;
    }
    public LowerManagementController getLowerManagementComponentController() {
        return lowerManagementComponentController;
    }
    public TopManagementController getTopManagementComponentController() {
        return topManagementComponentController;
    }

    public VBox getLowerManagementComponent() {
        return lowerManagementComponent;
    }

    public void updateDetailsFlowRun()
    {
        mainBodyController.updateButtons();
    }

}
