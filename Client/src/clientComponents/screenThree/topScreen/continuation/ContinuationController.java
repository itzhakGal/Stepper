package clientComponents.screenThree.topScreen.continuation;
import clientComponents.screenThree.topScreen.TopScreenController;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.UUID;

public class ContinuationController {

    private clientComponents.screenThree.topScreen.TopScreenController mainTopScreenController;
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
        FlowsExecutionScreenController flowsExecutionScreenController = mainTopScreenController.getMainFlowExecutionHistoryController().getMainBodyController().getFlowExecutionScreenComponentController();
        String sourceFlowName = mainTopScreenController.getChosenFlowNameProperty().getValue();

        String finalUrl = HttpUrl
                .parse(Constants.CONTINUATIONS)
                .newBuilder()
                .addQueryParameter("flowUUID", mainTopScreenController.getChosenFlowIdProperty().getValue())
                .addQueryParameter("sourceFlowName",
                        sourceFlowName)
                .addQueryParameter("targetFlowName",
                        targetFlowName)
                .addQueryParameter("userName", mainTopScreenController.getMainFlowExecutionHistoryController().getMainBodyController().getMainController().currentUserNameProperty().getValue())
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
                try{
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        flowsExecutionScreenController.updateDetailsFlowExecution(targetFlowName, true);
                        flowsExecutionScreenController.setVisibleDetails(false);
                        flowsExecutionScreenController.setVisibleContinuation();
                        flowNameContinuationListView.getItems().clear();
                        continueToFlowButton.setVisible(false);
                        mainTopScreenController.getMainFlowExecutionHistoryController().getMainBodyController().openFlowExecutionTab();
                        continuationButtonPressed.set(true);
                        mainTopScreenController.getRerunFlowButton().setDisable(true);
                    });
                }}finally {
                    response.close();
                }
            }
        });
    }

    public void setMainController(TopScreenController mainTopScreenController) {
        this.mainTopScreenController = mainTopScreenController;
    }

    public void updateContinuationDetails(DTOListContinuationFlowName listContinuationFlowName)
   {
       flowNameContinuationListView.getItems().remove(0,flowNameContinuationListView.getItems().size());
       for(String flowName: listContinuationFlowName.getListContinuationFlowName())
       {
           addValueToOutputList(flowName);
       }
       actionContinuationToAnotherFlow();
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
    public void addValueToOutputList(String value) {
        ObservableList<String> items = flowNameContinuationListView.getItems();
        items.add(value);
    }

    public SimpleBooleanProperty getContinuationButtonPressed() {
        return this.continuationButtonPressed;
    }

    public void clearDetails() {
        flowNameContinuationListView.getItems().clear();
        targetFlowName = null;
    }

    public ListView<String> getFlowNameContinuationListView() {
        return flowNameContinuationListView;
    }

    public Button getContinueToFlowButton() {
        return continueToFlowButton;
    }

    public void initListener() {

        mainTopScreenController.getMainFlowExecutionHistoryController().getMainBodyController().isExecutionsHistoryButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        clearDetails();
                        continueToFlowButton.setVisible(false);
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
