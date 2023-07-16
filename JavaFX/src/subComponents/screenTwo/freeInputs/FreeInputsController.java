package subComponents.screenTwo.freeInputs;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import stepper.dataDefinition.impl.number.IntWrapper;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.step.api.DataNecessity;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenTwo.FlowsExecutionScreenController;
import subComponents.screenTwo.freeInputs.freeInputDetails.collectionInputs.CollectionInputs;
import utils.DTOFlowExecution;
import utils.DTOFullDetailsPastRun;
import utils.DTOInputExecution;
import utils.DTOInputFlowPast;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FreeInputsController {

    private FlowsExecutionScreenController mainFlowsExecutionScreenController;
    private SystemEngineInterface systemEngine;
    @FXML
    private GridPane freeInputsGridPane;
    @FXML
    private Button runButton;
    private final SimpleBooleanProperty startButtonClickProperty = new SimpleBooleanProperty(false);
    private UUID flowIdRerun;
    private UUID currFlowId;
    @FXML
    public void initialize() {

    }

    public void setMainController(FlowsExecutionScreenController mainFlowsExecutionScreenController) {
        this.mainFlowsExecutionScreenController = mainFlowsExecutionScreenController;
    }

    public void setSystemEngine(SystemEngineInterface engineManager) {
        this.systemEngine = engineManager;
    }

    private int createTitleFreeInputs(String text, String value, int row) {
        Label freeInputsLabel = new Label(text);
        freeInputsGridPane.setMargin(freeInputsLabel, new javafx.geometry.Insets(10, 10, 10, 10));
        freeInputsLabel.setStyle("-fx-font-family: Georgia; -fx-text-fill: #5c88be;" + value);
        freeInputsGridPane.addRow(row, freeInputsLabel);
        row++;
        return row;
    }

    public void updateDetailsFreeInputs(DTOFlowExecution dtoFlowExecution, boolean isContinuation)
    {
        startButtonClickProperty.set(false);
        mainFlowsExecutionScreenController.getProgressBar().setVisible(false);
        freeInputsGridPane.getChildren().clear();
        freeInputsGridPane.getRowConstraints().clear();
        freeInputsGridPane.getColumnConstraints().clear();
        int row=0;
        int amountOfMandatoryInputs = 0;
        int amountOfOptionalInput = 0;
        IntWrapper countAmountUpdateData = new IntWrapper(0);
        this.flowIdRerun = systemEngine.updateOptionalExecution(dtoFlowExecution.getFlowName(), isContinuation);
        mainFlowsExecutionScreenController.setExecutedFlowID(flowIdRerun);
        row = createTitleFreeInputs("Free Inputs:", "-fx-font-weight: bold; -fx-font-size: 16", row);
        row = createTitleFreeInputs("Mandatory Inputs:", "-fx-font-weight: bold; -fx-font-size: 14", row);
        systemEngine.removeInitialFreeInputFromDTO(dtoFlowExecution);
        for(DTOInputExecution inputExecution : dtoFlowExecution.getInputsExecution())
        {
            if(inputExecution.getNecessity() == DataNecessity.MANDATORY) {
                row = getRow(row, inputExecution, dtoFlowExecution.getFlowName(), countAmountUpdateData);
                amountOfMandatoryInputs++;
            }
            else
                amountOfOptionalInput++;
        }
        if(amountOfOptionalInput>0) {
            row = createTitleFreeInputs("Optional Inputs:", "-fx-font-weight: bold; -fx-font-size: 14", row);
            for (DTOInputExecution inputExecution : dtoFlowExecution.getInputsExecution()) {
                if (inputExecution.getNecessity() == DataNecessity.OPTIONAL) {
                    row = getRow(row, inputExecution, dtoFlowExecution.getFlowName(), countAmountUpdateData);
                }
            }
        }
        updateVisibleButtonRun(amountOfMandatoryInputs, dtoFlowExecution, countAmountUpdateData);
    }

    private int getRow(int row, DTOInputExecution inputExecution, String flowName, IntWrapper countAmountUpdateData) {
        try {
            FXMLLoader fxmlLoader;
            GridPane content;

            if (inputExecution.getType().equals("EnumeratorData"))
                fxmlLoader = new FXMLLoader(getClass().getResource("freeInputDetails/freeInputEnumerator/freeInputEnumerator.fxml"));
            else if (inputExecution.getType().equals("Integer"))
                fxmlLoader = new FXMLLoader(getClass().getResource("freeInputDetails/freeInputNumber/freeInputNumber.fxml"));
            else
            {
                if (inputExecution.isFile())
                    fxmlLoader = new FXMLLoader(getClass().getResource("freeInputDetails/freeInputLoadFile/freeInputFile.fxml"));
                else
                    fxmlLoader = new FXMLLoader(getClass().getResource("freeInputDetails/freeInputString/freeInputDetails.fxml"));
            }

            content = fxmlLoader.load();
            CollectionInputs controller = fxmlLoader.getController();
            controller.init(inputExecution.getFinalName(), inputExecution.getNecessity());
            content.getProperties().put("controller", controller);
            controller.updateDetails(inputExecution);
            controller.setMainController(this);
            controller.setSystemEngine(systemEngine);

            DataInFlowExecution freeInputDataInFlow = systemEngine.findValueOfFreeInput(flowName,inputExecution.getFinalName());
            if(freeInputDataInFlow.getItem() != null)
            {
                controller.setFreeInput(freeInputDataInFlow.getItem());
                countAmountUpdateData.setValue(countAmountUpdateData.getValue() + 1);
            }
            freeInputsGridPane.addRow(row, content);
            row++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return row;
    }

    private void updateVisibleButtonRun(int amountOfMandatoryInputs, DTOFlowExecution dtoFlowExecution, IntWrapper countAmountUpdateData) {
        AtomicInteger mandatoryInputs = new AtomicInteger(amountOfMandatoryInputs - countAmountUpdateData.getValue());
        runButton.setOnAction(event -> handleButtonClick(amountOfMandatoryInputs, dtoFlowExecution));
        if (mandatoryInputs.get() > 0)
            runButton.setDisable(true);
       if (mandatoryInputs.get() == 0) {
            runButton.setDisable(false);
            mainFlowsExecutionScreenController.getRerunButton().setDisable(true);
        }

        for (Node node : freeInputsGridPane.getChildren()) {
            if (node instanceof GridPane) {
                GridPane childGridPane = (GridPane) node;
                Integer rowIndex = GridPane.getRowIndex(childGridPane);
                if (rowIndex != null && rowIndex >= 2 && rowIndex < (2 + amountOfMandatoryInputs)) {
                    CollectionInputs controller = (CollectionInputs) childGridPane.getProperties().get("controller");
                    if (controller.getNecessity() == DataNecessity.MANDATORY) {
                        SimpleBooleanProperty isInputFieldEmpty = controller.getIsInputFieldEmptyProperty();
                        isInputFieldEmpty.addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                mandatoryInputs.getAndDecrement();
                                if (mandatoryInputs.get() == 0) {
                                    runButton.setDisable(false);
                                    mainFlowsExecutionScreenController.getRerunButton().setDisable(true);
                                }
                            }
                            else {
                                mandatoryInputs.getAndIncrement();
                                runButton.setDisable(true);
                                mainFlowsExecutionScreenController.getRerunButton().setDisable(false);
                            }
                        });
                    }
                }
            }
        }
    }

    private void handleButtonClick(int amountOfMandatoryInputs, DTOFlowExecution dtoFlowExecution) {
        CollectionInputs controller;

        for (int row = 2; row < (dtoFlowExecution.getInputsExecution().size()+3); row++) {
            if(row != (2+amountOfMandatoryInputs)) {
                GridPane childGridPane = (GridPane) freeInputsGridPane.getChildren().get(row);
                controller = (CollectionInputs) childGridPane.getProperties().get("controller");
                String labelValue = controller.getName();
                String value = controller.getInputData();
                String type= dtoFlowExecution.getInputByFinalName(labelValue).getType();
                if(!value.isEmpty()) {
                    if (type.equals("Integer")) {
                        try {
                            // Try to parse the value as an integer
                            int intValue = Integer.parseInt(value);
                            systemEngine.updateMandatoryInput(labelValue, intValue);
                        } catch (NumberFormatException e) {
                             createTitleFreeInputs("The input value is not an int", "-fx-font-weight: bold; -fx-font-size: 12", row);
                        }
                    } else  if (type.equals("EnumeratorData"))
                    {
                        systemEngine.updateMandatoryInputEnumerator(labelValue, value);
                    }else {
                        systemEngine.updateMandatoryInput(labelValue, value);
                    }
                }
            }
        }
        executionFlow(dtoFlowExecution);
        mainFlowsExecutionScreenController.setVisibleDetails(true);
        mainFlowsExecutionScreenController.getRerunButtonProperty().set(false);
        mainFlowsExecutionScreenController.getProgressBar().setVisible(true);
    }
    private void executionFlow(DTOFlowExecution dtoFlowExecution) {
        DTOFullDetailsPastRun fullDetails = systemEngine.flowActivationAndExecutionJavaFX(dtoFlowExecution.getFlowName());
        currFlowId = fullDetails.getUniqueId();
        runButton.setDisable(true);
        mainFlowsExecutionScreenController.updateDetailsFlowRun(fullDetails);

        startButtonClickProperty.set(true);
    }
    public SimpleBooleanProperty getStartButtonClickProperty() {
        return this.startButtonClickProperty;
    }

    public void clearDetails() {
        startButtonClickProperty.set(false);
        freeInputsGridPane.getChildren().clear();
        freeInputsGridPane.getRowConstraints().clear();
        freeInputsGridPane.getColumnConstraints().clear();
    }

    public void updateFreeInputForRerunExecution(List<DTOInputFlowPast> inputs, String flowName) {
        DTOFlowExecution dtoFlowExecution = systemEngine.readInputsJavaFX(flowName);
        int amountOfMandatoryInputs = dtoFlowExecution.getAmountOfMandatoryInputs();
        for (Node node : freeInputsGridPane.getChildren()) {
            if (node instanceof GridPane) {
                GridPane childGridPane = (GridPane) node;
                CollectionInputs controller = (CollectionInputs) childGridPane.getProperties().get("controller");
                DTOInputFlowPast inputFlowPast = findDTOInputFlowPastByName(controller.getName(), inputs);
                if (inputFlowPast.getValue() != null)
                    controller.setFreeInput(inputFlowPast.getValue());
            }
        }
    }

    private DTOInputFlowPast findDTOInputFlowPastByName(String name, List<DTOInputFlowPast> inputs) {
        for(DTOInputFlowPast dtoInputFlowPast : inputs)
        {
            if(name.equals(dtoInputFlowPast.getFinalName()))
                return dtoInputFlowPast;
        }
        return null;
    }

    public UUID getFlowIdRerun() {
        return flowIdRerun;
    }

    public UUID getCurrFlowId() {
        return currFlowId;
    }
}
