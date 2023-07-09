package adminComponents.screenTwo.lowerScreen;

import adminComponents.screenTwo.RolesManagementController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import stepper.systemEngine.SystemEngineInterface;

public class LowerManagementController {

    private RolesManagementController mainRolesManagementController;
    //private SystemEngineInterface systemEngine;
    @FXML
    private Label roleName;
    @FXML
    private Label roleDescription;
    @FXML
    private ChoiceBox<?> selectFlowAssigned;
    @FXML
    private Button saveRole;
    @FXML
    void saveRoleAction(ActionEvent event) {

    }

    @FXML
    void selectFlowAssignedActivate(MouseEvent event) {

    }

    public void setMainController(RolesManagementController rolesManagementController) {
        this.mainRolesManagementController = rolesManagementController;

    }
    public void setSystemEngine(SystemEngineInterface systemEngine) {
        //this.systemEngine = systemEngine;
    }

}
