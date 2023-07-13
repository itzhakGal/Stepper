package adminComponents.screenTwo.lowerScreen;

import adminComponents.screenTwo.RolesManagementController;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.CheckTreeView;
import stepper.systemEngine.SystemEngineInterface;
import sun.plugin.javascript.navig.Anchor;
import utilWebApp.DTORole;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LowerManagementController {

    private RolesManagementController mainRolesManagementController;
    //private SystemEngineInterface systemEngine;
    @FXML
    private TextField roleName;

    @FXML
    private TextField roleDescription;

    @FXML
    private AnchorPane selectFlowAssignedAnchor;
    @FXML
    private Button saveRole;

    @FXML
    void saveRoleAction(ActionEvent event) {

    }

    public void setMainController(RolesManagementController rolesManagementController) {
        this.mainRolesManagementController = rolesManagementController;

    }
    public void setSystemEngine(SystemEngineInterface systemEngine) {
        //this.systemEngine = systemEngine;
    }

    public void createNewRole() {

        //List<String> newListOfFOlwConnectedToRole = createListOfFOlwConnectedToRole();
        //DTORole newRole =  DTORole

    }

    /*private List<String> createListOfFOlwConnectedToRole() {
        List<String> newListOfFOlwConnectedToRole = new ArrayList<>();

        CheckBoxTreeItem<String> rootItem = new  CheckBoxTreeItem<String>("Assign Flows To Role");

        for(String flowName : selectedAssignedFlowToRole) {
            CheckBoxTreeItem<String> role = new CheckBoxTreeItem<String>(flowName);
            rootItem.getChildren().add(role);
        }

        CheckTreeView<String> checkTreeView = new CheckTreeView<>(rootItem);

        checkTreeView.getCheckModel().getCheckedItems().addListener(new ListChangeListener<TreeItem<String>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<String>> c) {
                //checkTreeView.getCheckModel().getCheckedItems();
                while (c.next()) {
                    if (c.wasAdded()) {
                        newListOfFOlwConnectedToRole.addAll(
                                c.getAddedSubList()
                                        .stream()
                                        .map(TreeItem::getValue)
                                        .collect(Collectors.toList())
                        );
                    }
                    if (c.wasRemoved()) {
                        newListOfFOlwConnectedToRole.removeAll(
                                c.getRemoved()
                                        .stream()
                                        .map(TreeItem::getValue)
                                        .collect(Collectors.toList())
                        );
                    }
                }
            }
        });

        //selectFlowAssignedAnchor.getChildren().clear();
        //selectFlowAssignedAnchor.getChildren().add(checkTreeView);

        return newListOfFOlwConnectedToRole;

    }*/

}
