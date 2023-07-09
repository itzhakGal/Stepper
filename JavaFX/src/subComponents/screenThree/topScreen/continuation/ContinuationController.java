package subComponents.screenThree.topScreen.continuation;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenThree.topScreen.TopScreenController;
import subComponents.screenTwo.FlowsExecutionScreenController;
import utilsDesktopApp.DTOListContinuationFlowName;

import java.util.UUID;

public class ContinuationController {

    private TopScreenController mainTopScreenController;
    private SystemEngineInterface systemEngine;
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
       systemEngine.continuationToOtherFlow(UUID.fromString(mainTopScreenController.getChosenFlowIdProperty().getValue()),sourceFlowName, targetFlowName);
        flowsExecutionScreenController.updateDetailsFlowExecution(targetFlowName, true);
        flowsExecutionScreenController.setVisibleDetails(false);
        flowsExecutionScreenController.setVisibleContinuation();
        flowNameContinuationListView.getItems().clear();
        continueToFlowButton.setVisible(false);
        mainTopScreenController.getMainFlowExecutionHistoryController().getMainBodyController().openFlowExecutionTab();
        continuationButtonPressed.set(true);
        mainTopScreenController.getRerunFlowButton().setDisable(true);
    }

    public void setMainController(TopScreenController mainTopScreenController) {
        this.mainTopScreenController = mainTopScreenController;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
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


}
