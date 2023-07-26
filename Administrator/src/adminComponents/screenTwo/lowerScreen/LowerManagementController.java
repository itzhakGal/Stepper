package adminComponents.screenTwo.lowerScreen;

import adminComponents.screenTwo.RolesManagementController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;
import org.controlsfx.control.CheckTreeView;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;
import utilWebApp.DTOListOfFlowsAvailable;
import utilWebApp.DTORole;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private SimpleStringProperty isRoleNameEmptyProperty;
    private SimpleStringProperty isRoleDescriptionEmptyProperty;
    public LowerManagementController() {
        isRoleNameEmptyProperty = new SimpleStringProperty("");
        isRoleDescriptionEmptyProperty = new SimpleStringProperty("");
    }
    @FXML
    public void initialize() {
        roleName.textProperty().addListener((observable, oldValue, newValue) -> {
            isRoleNameEmptyProperty.set(newValue);
        });

        roleDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            isRoleDescriptionEmptyProperty.set(newValue);
        });
    }
    public SimpleStringProperty getIsRoleNameEmptyProperty() {
        return this.isRoleNameEmptyProperty;
    }
    public SimpleStringProperty getIsRoleDescriptionEmptyProperty() {
        return this.isRoleNameEmptyProperty;
    }
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
                Platform.runLater(() -> {
                    handleFailure((e.getMessage()));
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Platform.runLater(() -> {
                            labelMassage.setText(res.toString());
                            mainRolesManagementController.getLowerManagementComponent().setVisible(false);
                            mainRolesManagementController.getTopManagementComponentController().getNewRoleButton().setDisable(false);
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

    public void updateListOfFlowInSystemFromServer() {

        String finalUrl = HttpUrl
                .parse(Constants.LIST_OF_FLOW_AVAILABLE_FOR_ROLE)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    handleFailure((e.getMessage()));
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        DTOListOfFlowsAvailable dtoListOfFlowsAvailable = new Gson().fromJson(res, DTOListOfFlowsAvailable.class);
                        List<String> allFlowInTheSystem = dtoListOfFlowsAvailable.getAllFlowInTheSystem();
                        Platform.runLater(() -> {
                            createNewRole(allFlowInTheSystem);
                        });
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public void createNewRole(List<String> allFlowInTheSystem) {

        roleName.setText("");
        roleDescription.setText("");
        labelMassage.setText("");

        this.saveListOfFlowConnectedToRole = createListOfFlowConnectedToRole(allFlowInTheSystem);

        // Add listeners to the properties
        isRoleNameEmptyProperty.addListener((observable, oldValue, newValue) -> {
            checkPropertiesAndPerformAction();
        });

        isRoleDescriptionEmptyProperty.addListener((observable, oldValue, newValue) -> {
            checkPropertiesAndPerformAction();
        });

    }

    public void checkPropertiesAndPerformAction() {

        if (!isRoleNameEmptyProperty.get().isEmpty() && !isRoleDescriptionEmptyProperty.get().isEmpty() && !saveListOfFlowConnectedToRole.isEmpty())
        {
            this.newRole = new DTORole(roleName.getText(), roleDescription.getText(), new HashSet<>(saveListOfFlowConnectedToRole));
            saveRole.setDisable(false);
        }
        else
            saveRole.setDisable(true);
    }

    public List<String> createListOfFlowConnectedToRole(List<String> allFlowInTheSystem) {
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

                checkPropertiesAndPerformAction(); // לא יודעת אם נכון לשים פה
            }
        });

        selectFlowAssignedAnchor.getChildren().clear();
        selectFlowAssignedAnchor.getChildren().add(checkTreeView);

        return saveListOfFlowConnectedToRole;
    }

    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }

}
