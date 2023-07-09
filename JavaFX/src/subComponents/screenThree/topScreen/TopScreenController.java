package subComponents.screenThree.topScreen;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import stepper.flow.execution.FlowExecutionResult;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
import subComponents.screenThree.topScreen.continuation.ContinuationController;
import utils.DTOFullDetailsPastRun;
import utilsDesktopApp.DTOListContinuationFlowName;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class TopScreenController implements Initializable {

    private FlowExecutionHistoryController mainFlowExecutionHistoryController;
    private SystemEngineInterface systemEngine;
    @FXML
    private TableView<ExecutionData> tableFlowExecution;
    @FXML
    private TableColumn<ExecutionData, String> flowNameColumn;
    @FXML
    private TableColumn<ExecutionData, String> startDateColumn;
    @FXML
    private TableColumn<ExecutionData, String> resultExecutionColumn;

    @FXML private ComboBox<String> resultComboBox;
    @FXML private VBox continuationComponent;
    @FXML private ContinuationController continuationComponentController;
    @FXML
    private Label historyFlowsLabel;
    @FXML
    private Button rerunFlowButton;
    private SimpleStringProperty chosenFlowIdProperty;
    private SimpleStringProperty chosenFlowNameProperty;

    private SimpleBooleanProperty selectedNameTableView;
    private List<DTOFullDetailsPastRun> flowsExecutedList ;
    private SimpleBooleanProperty rerunFlowButtonProperty;
    public TopScreenController()
    {
        chosenFlowIdProperty = new SimpleStringProperty("");
        chosenFlowNameProperty = new SimpleStringProperty("");
        selectedNameTableView = new SimpleBooleanProperty(false);
        rerunFlowButtonProperty = new SimpleBooleanProperty(false);
    }
    @FXML
    private void handleComboBoxAction(ActionEvent event) {
        String selectedFilter = resultComboBox.getValue();
        filterTableDataByResult(selectedFilter);
    }

    @FXML
    private void rerunFlowButtonAction(ActionEvent event) {
        rerunFlowButtonProperty.set(true);
        mainFlowExecutionHistoryController.getMainBodyController().updateExecuteFlowButton(chosenFlowNameProperty.getValue());
        DTOFullDetailsPastRun fullDetailsPastRun = systemEngine.getFlowExecutedDataDTO((UUID.fromString(chosenFlowIdProperty.getValue())));
        mainFlowExecutionHistoryController.getMainBodyController().getFlowExecutionScreenComponentController().getFreeInputDetailsComponentController().updateFreeInputForRerunExecution(fullDetailsPastRun.getInputs(), fullDetailsPastRun.getFlowName());
        mainFlowExecutionHistoryController.getMainBodyController().openFlowExecutionTab();
        rerunFlowButtonProperty.set(false);
        rerunFlowButton.setDisable(true);
    }
    public void init() {
        // Add action to TableView rows
        tableFlowExecution.setOnMouseClicked(event -> {
            ExecutionData selectedFlow = tableFlowExecution.getSelectionModel().getSelectedItem();
            if (selectedFlow != null) {
                chosenFlowNameProperty.set(selectedFlow.getFlowName());
                chosenFlowIdProperty.set(selectedFlow.getFlowId().toString());
                rerunFlowButton.setDisable(false);
                updateContinuationDetails(selectedFlow.getFlowName());
            }
        });

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (continuationComponentController != null) {
            continuationComponentController.setMainController(this);
        }

        flowNameColumn.setCellValueFactory(new PropertyValueFactory<ExecutionData, String>("flowName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<ExecutionData, String>("startDate"));
        resultExecutionColumn.setCellValueFactory(new PropertyValueFactory<ExecutionData, String>("resultExecutions"));
        setRightToLeftAlignment(flowNameColumn);
        setRightToLeftAlignment(startDateColumn);
        setRightToLeftAlignment(resultExecutionColumn);
        initialComboBox();
        addAnimation();
    }

     private void addAnimation() {
        // Add animation to the table rows
        tableFlowExecution.setRowFactory(tableView -> {
            TableRow<ExecutionData> row = new TableRow<>();
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), row);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), row);
            translateTransition.setFromY(100);
            translateTransition.setToY(0);
            row.setOnMouseClicked(event -> {
                fadeTransition.play();
                translateTransition.play();
            });
            return row;
        });
    }

    private void initialComboBox() {
        resultComboBox.getItems().addAll("All", FlowExecutionResult.SUCCESS.toString()
                , FlowExecutionResult.WARNING.toString(), FlowExecutionResult.FAILURE.toString());
        resultComboBox.setValue("All"); // Set the default filter option
    }
    private <T> void setRightToLeftAlignment(TableColumn<ExecutionData, T> column) {
        column.setCellFactory(new Callback<TableColumn<ExecutionData, T>, TableCell<ExecutionData, T>>() {
            @Override
            public TableCell<ExecutionData, T> call(TableColumn<ExecutionData, T> param) {
                TableCell<ExecutionData, T> cell = new TableCell<ExecutionData, T>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setAlignment(Pos.CENTER);
                        } else {
                            setText(item.toString());
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
                return cell;
            }
        });
    }

    public void setMainController(FlowExecutionHistoryController mainFlowExecutionHistoryController) {
        this.mainFlowExecutionHistoryController = mainFlowExecutionHistoryController;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
        continuationComponentController.setSystemEngine(systemEngine);
    }


    public SimpleStringProperty getChosenFlowIdProperty() {
        return chosenFlowIdProperty;
    }

    public SimpleStringProperty getChosenFlowNameProperty() {
        return chosenFlowNameProperty;
    }

    public void setTableView(List<DTOFullDetailsPastRun> flowsExecutedList) {
        chosenFlowIdProperty.set("");
        setDetailsFlowExecutionHistory(flowsExecutedList);
    }

    public void setDetailsFlowExecutionHistory(List<DTOFullDetailsPastRun> flowsExecutedList) {
        ObservableList<ExecutionData> data = tableFlowExecution.getItems();
        data.clear();

        for (DTOFullDetailsPastRun flowDetails : flowsExecutedList) {
            String flowResult;
            String activationDate = flowDetails.getActivationDate();
            if (flowDetails.getFinalResult() != null)
                 flowResult = flowDetails.getFinalResult().getDescription();
            else
                flowResult = "The flow is still in progress";

            UUID flowId = flowDetails.getUniqueId();
            ExecutionData executionData = new ExecutionData(flowDetails.getFlowName(), flowResult, activationDate, flowId);
            tableFlowExecution.getItems().add(executionData);
        }
        this.flowsExecutedList = flowsExecutedList;
    }

    public void filterTableDataByResult(String selectedFilter) {
        setDetailsFlowExecutionHistory(flowsExecutedList);
        if (!selectedFilter.equals("All")) {
            ObservableList<ExecutionData> filteredList = FXCollections.observableArrayList();
            for (ExecutionData flow : tableFlowExecution.getItems()) {
                if (flow.getResultExecutions().equals(selectedFilter)) {
                    filteredList.add(flow);
                }
            }
            tableFlowExecution.setItems(filteredList);
        }
    }

    public void updateContinuationDetails(String flowName) {
        DTOListContinuationFlowName listContinuationFlowName = systemEngine.setListContinuationFlowName(flowName);
        if(!listContinuationFlowName.getListContinuationFlowName().isEmpty()) {
            continuationComponent.setVisible(true);
            continuationComponentController.getFlowNameContinuationListView().getItems().clear();
            continuationComponentController.updateContinuationDetails(listContinuationFlowName);
        }
    }

    public FlowExecutionHistoryController getMainFlowExecutionHistoryController() {
        return mainFlowExecutionHistoryController;
    }

    public ContinuationController getContinuationComponentController() {
        return continuationComponentController;
    }

    public void clearDetails() {
        ObservableList<ExecutionData> data = tableFlowExecution.getItems();
        data.clear(); // Clear the items in the tableFlowExecution
        continuationComponentController.getFlowNameContinuationListView().getItems().clear(); // Clear the items in the flowNameContinuationListView
    }

    public SimpleBooleanProperty getRerunFlowButtonProperty()
    {
        return rerunFlowButtonProperty;
    }

    public void initListener() {

        mainFlowExecutionHistoryController.getMainBodyController().isExecutionsHistoryButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        rerunFlowButton.setDisable(true);
                    }
                });
        continuationComponentController.initListener();
    }

    public Button getRerunFlowButton() {
        return rerunFlowButton;
    }
}