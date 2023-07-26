package clientComponents.screenTwo.continuation;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenTwo.FlowsExecutionScreenController;
import util.Constants;
import util.http.HttpClientUtil;
import utilsDesktopApp.DTOListContinuationFlowName;
import java.io.IOException;
import java.util.List;

public class ContinuationController {

    private FlowsExecutionScreenController mainFlowsExecutionScreenController;
    @FXML
    private Button continueToFlowButton;
    @FXML
    private Label continuationLabel;
    @FXML
    private ListView<String> flowNameContinuationListView;
    private ChangeListener<String> continuationListListener;

    private String targetFlowName;
    private ListProperty<String> flowNameContinuationListProperty;

    private SimpleBooleanProperty continuationButtonPressed;

    public ContinuationController() {
        flowNameContinuationListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        continuationButtonPressed = new SimpleBooleanProperty(false);
    }
    @FXML
    private void initialize() {
        flowNameContinuationListView.itemsProperty().bind(flowNameContinuationListProperty);
    }

    @FXML
    void continueToFlowButtonAction(ActionEvent event) {
        mainFlowsExecutionScreenController.getFlowContinuationComponent().setVisible(true);
        String sourceFlowName = mainFlowsExecutionScreenController.getFlowName();

        String finalUrl = HttpUrl
                .parse(Constants.CONTINUATIONS)
                .newBuilder()
                .addQueryParameter("flowUUID",
                        mainFlowsExecutionScreenController.getFreeInputDetailsComponentController().getFlowIdRerun().toString())
                .addQueryParameter("sourceFlowName",
                        sourceFlowName)
                .addQueryParameter("targetFlowName",
                        targetFlowName)
                .addQueryParameter("userName", mainFlowsExecutionScreenController.getMainBodyController().getMainController().currentUserNameProperty().getValue())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    handleFailure(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        mainFlowsExecutionScreenController.updateDetailsFlowExecution(targetFlowName, true);
                        mainFlowsExecutionScreenController.setVisibleDetails(false);
                        mainFlowsExecutionScreenController.setVisibleContinuation();
                        continueToFlowButton.setVisible(false);
                        continuationButtonPressed.set(true);
                    });
                }
            }
        });

    }

    public void setMainController(FlowsExecutionScreenController mainFlowsExecutionScreenController) {
        this.mainFlowsExecutionScreenController = mainFlowsExecutionScreenController;
    }

    public void updateContinuationDetails(DTOListContinuationFlowName listContinuationFlowName) {
        Platform.runLater(() -> {
            flowNameContinuationListProperty.clear();
            flowNameContinuationListProperty.addAll(listContinuationFlowName.getListContinuationFlowName());
            actionContinuationToAnotherFlow();
        });
    }

    public void clearDetails() {
        Platform.runLater(() -> flowNameContinuationListProperty.clear());
    }

    private void actionContinuationToAnotherFlow() {
        if (continuationListListener != null) {
            flowNameContinuationListView.getSelectionModel().selectedItemProperty().removeListener(continuationListListener);
        }
        continuationListListener = new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                continueToFlowButton.setVisible(true);
                String currentSelected = flowNameContinuationListView.getSelectionModel().getSelectedItem();
                if (currentSelected != null) {
                    targetFlowName= currentSelected;
                }
            }
        };
        flowNameContinuationListView.getSelectionModel().selectedItemProperty().addListener(continuationListListener);
    }

    public SimpleBooleanProperty getContinuationButtonPressed() {
        return this.continuationButtonPressed;
    }

    public void initListener() {
        mainFlowsExecutionScreenController.getMainBodyController().getFlowExecutionHistoryScreenComponentController().getFlowExecutionTableComponentController().getRerunFlowButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        clearDetails();
                        continueToFlowButton.setVisible(false);
                    }
                });

        mainFlowsExecutionScreenController.getRerunButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        clearDetails();
                        continueToFlowButton.setVisible(false);
                    }
                });


        mainFlowsExecutionScreenController.getMainBodyController().getFlowDefinitionScreenComponentController().getIsExecuteFlowButtonClicked()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        mainFlowsExecutionScreenController.getFlowContinuationComponent().setVisible(false);
                    }
                });
    }
    public void handleFailure(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error In The Server");
        alert.setContentText(errorMessage);
        alert.setWidth(300);
        alert.show();
    }

}
