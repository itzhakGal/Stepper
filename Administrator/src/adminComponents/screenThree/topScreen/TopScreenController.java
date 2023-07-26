package adminComponents.screenThree.topScreen;

import adminComponents.screenThree.flowExecutionHistory.FlowExecutionHistoryController;
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
import javafx.util.Callback;
import javafx.util.Duration;
import stepper.flow.execution.FlowExecutionResult;
import stepper.systemEngine.SystemEngineInterface;
import utilWebApp.DTOFullDetailsPastRunWeb;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class TopScreenController implements Initializable {

    private FlowExecutionHistoryController mainFlowExecutionHistoryController;
    @FXML
    private TableView<ExecutionData> tableFlowExecution;
    @FXML
    private TableColumn<ExecutionData, String> flowNameColumn;
    @FXML
    private TableColumn<ExecutionData, String> startDateColumn;
    @FXML
    private TableColumn<ExecutionData, String> resultExecutionColumn;
    @FXML
    private TableColumn<ExecutionData, String>  userNameTableColum;
    @FXML private ComboBox<String> resultComboBox;
    @FXML
    private Label historyFlowsLabel;
    private SimpleStringProperty chosenFlowIdProperty;
    private SimpleStringProperty chosenFlowNameProperty;
    private SimpleBooleanProperty selectedNameTableView;
    private List<DTOFullDetailsPastRunWeb> flowsExecutedList ;
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
        mainFlowExecutionHistoryController.getMainBodyController().openFlowExecutionTab();
        rerunFlowButtonProperty.set(false);
    }
    public void init() {
        tableFlowExecution.setOnMouseClicked(event -> {
            ExecutionData selectedFlow = tableFlowExecution.getSelectionModel().getSelectedItem();
            if (selectedFlow != null) {
                chosenFlowNameProperty.set(selectedFlow.getFlowName());
                chosenFlowIdProperty.set(selectedFlow.getFlowId().toString());
                updateContinuationDetails(selectedFlow.getFlowName());
            }
        });

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        flowNameColumn.setCellValueFactory(new PropertyValueFactory<ExecutionData, String>("flowName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<ExecutionData, String>("startDate"));
        resultExecutionColumn.setCellValueFactory(new PropertyValueFactory<ExecutionData, String>("resultExecutions"));
        userNameTableColum.setCellValueFactory(new PropertyValueFactory<ExecutionData, String>("userName"));
        setRightToLeftAlignment(flowNameColumn);
        setRightToLeftAlignment(startDateColumn);
        setRightToLeftAlignment(resultExecutionColumn);
        setRightToLeftAlignment(userNameTableColum);
        initialComboBox();
        addAnimation();
    }

     private void addAnimation() {
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

    public SimpleStringProperty getChosenFlowIdProperty() {
        return chosenFlowIdProperty;
    }

    public SimpleStringProperty getChosenFlowNameProperty() {
        return chosenFlowNameProperty;
    }

    public void setTableView(List<DTOFullDetailsPastRunWeb> flowsExecutedList) {
        chosenFlowIdProperty.set("");
        setDetailsFlowExecutionHistory(flowsExecutedList);
    }

    public void setDetailsFlowExecutionHistory(List<DTOFullDetailsPastRunWeb> flowsExecutedList) {
        ObservableList<ExecutionData> data = tableFlowExecution.getItems();
        data.clear();

        for (DTOFullDetailsPastRunWeb flowDetails : flowsExecutedList) {
            String flowResult;
            String activationDate = flowDetails.getActivationDate();
            if (flowDetails.getFinalResult() != null)
                 flowResult = flowDetails.getFinalResult().getDescription();
            else
                flowResult = "The flow is still in progress";

            UUID flowId = flowDetails.getUniqueId();
            ExecutionData executionData = new ExecutionData(flowDetails.getFlowName(), flowResult, activationDate, flowId, flowDetails.getUserName());
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

    }

    public FlowExecutionHistoryController getMainFlowExecutionHistoryController() {
        return mainFlowExecutionHistoryController;
    }

    public void clearDetails() {
        ObservableList<ExecutionData> data = tableFlowExecution.getItems();
        data.clear(); // Clear the items in the tableFlowExecution
    }

    public SimpleBooleanProperty getRerunFlowButtonProperty()
    {
        return rerunFlowButtonProperty;
    }

    public void initListener() {

        mainFlowExecutionHistoryController.getMainBodyController().isExecutionsHistoryButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        //rerunFlowButton.setDisable(true);
                    }
                });
    }

}