package adminComponents.screenTwo.lowerScreen;

import adminComponents.screenTwo.RolesManagementController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;
import org.controlsfx.control.CheckTreeView;
import org.jetbrains.annotations.NotNull;
import stepper.systemEngine.SystemEngineInterface;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTORole;
import utilWebApp.DTORoleDataFullInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LowerManagementController {

    private RolesManagementController mainRolesManagementController;
    @FXML
    private TextField roleName;
    @FXML
    private TextField roleDescription;
    @FXML
    private AnchorPane selectFlowAssignedAnchor;
    @FXML
    private Button saveRole;
    @FXML
    private Label labelMassage;

    private List<String> saveListOfFlowConnectedToRole;
    private DTORole newRole;

    private
    @FXML
    void saveRoleAction(ActionEvent event) {

        String finalUrl = HttpUrl
                .parse(Constants.NEW_ROLE_DATA_TO_SAVE)
                .newBuilder()
                .build()
                .toString();

        String roleInfoJson = new Gson().toJson(newRole);
        RequestBody body = RequestBody.create(roleInfoJson.getBytes());
        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Platform.runLater(() -> {
                            labelMassage.setText(res.toString());
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public void setMainController(RolesManagementController rolesManagementController) {
        this.mainRolesManagementController = rolesManagementController;
    }
    public void createNewRole() {

        List<String> allFlowInTheSystem = mainRolesManagementController.getTopManagementComponentController().getAllFlowsInTheSystem();
        this.saveListOfFlowConnectedToRole = createListOfFlowConnectedToRole(allFlowInTheSystem);

        if(!((roleName.getText().isEmpty()) || (roleDescription.getText().isEmpty()))) {
            this.newRole = new DTORole(roleName.getText(), roleDescription.getText(), new HashSet<>(this.saveListOfFlowConnectedToRole));
            saveRole.setVisible(true);
        }
    }

    private List<String> createListOfFlowConnectedToRole(List<String> allFlowInTheSystem) {
        List<String> saveListOfFlowConnectedToRole = new ArrayList<>();

        CheckBoxTreeItem<String> rootItem = new  CheckBoxTreeItem<String>("Assign Flows To Role");

        for(String flowName : allFlowInTheSystem) {
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
                        saveListOfFlowConnectedToRole.addAll(
                                c.getAddedSubList()
                                        .stream()
                                        .map(TreeItem::getValue)
                                        .collect(Collectors.toList())
                        );
                    }
                    if (c.wasRemoved()) {
                        saveListOfFlowConnectedToRole.removeAll(
                                c.getRemoved()
                                        .stream()
                                        .map(TreeItem::getValue)
                                        .collect(Collectors.toList())
                        );
                    }
                }
            }
        });

        selectFlowAssignedAnchor.getChildren().clear();
        selectFlowAssignedAnchor.getChildren().add(checkTreeView);

        return saveListOfFlowConnectedToRole;
    }

}
