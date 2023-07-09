package subComponents.screenTwo.freeInputs.freeInputDetails.freeInputNumber;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.step.api.DataNecessity;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenTwo.freeInputs.FreeInputsController;
import subComponents.screenTwo.freeInputs.freeInputDetails.collectionInputs.CollectionInputs;
import utils.DTOInputExecution;

public class FreeInputNumberController implements CollectionInputs {
    private FreeInputsController mainFreeInputsController;
    private SystemEngineInterface systemEngine;
    @FXML
    private Label userStringLabel;
    @FXML
    private Spinner<Integer> freeInputNumberSpinner;

    private String name;
    private DataNecessity necessity;
    private final SimpleBooleanProperty isInputFieldEmptyProperty;
    public FreeInputNumberController() {
        isInputFieldEmptyProperty = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                -100, Integer.MAX_VALUE, 0, 1);
        freeInputNumberSpinner.setValueFactory(valueFactory);

        // Add a listener to the spinner's value property
        freeInputNumberSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                // Update the isInputFieldEmptyProperty based on the spinner's value
                isInputFieldEmptyProperty.set(newValue != oldValue);
            }
        });
    }
    @Override
    public void init(String name, DataNecessity necessity) {
        this.necessity = necessity;
        this.name = name;
    }

    public Label getFieldLabel() {
        return this.userStringLabel;
    }

    public String getName() {
        return this.name;
    }

    public DataNecessity getNecessity() {
        return this.necessity;
    }

    public SimpleBooleanProperty getIsInputFieldEmptyProperty() {
        return this.isInputFieldEmptyProperty;
    }

    @Override
    public String getInputData() {
        int value = freeInputNumberSpinner.getValue();
        return Integer.toString(value);
    }

    public void setMainController(FreeInputsController mainFreeInputsController) {
        this.mainFreeInputsController = mainFreeInputsController;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
    }


    public void updateDetails(DTOInputExecution inputExecution) {
        userStringLabel.setText(inputExecution.getFinalName());
    }

    public void setFreeInputSpinner(DataInFlowExecution freeInputDataInFlow) {
        Object item = freeInputDataInFlow.getItem();
        int value = (int) item; // Casting to int
        // Use the value as needed
        freeInputNumberSpinner.getValueFactory().setValue(value);
    }

    public void setFreeInput(Object item)
    {
        int value = Integer.parseInt(item.toString());
        freeInputNumberSpinner.getValueFactory().setValue(value);
    }
    public void setEditableSpinner() {
        freeInputNumberSpinner.setEditable(false);
    }
}
