package clientComponents.screenTwo.freeInputs.freeInputDetails.freeInputEnumerator;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import stepper.dataDefinition.impl.Enumerator.EnumeratorData;
import stepper.step.api.DataNecessity;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenTwo.freeInputs.FreeInputsController;
import clientComponents.screenTwo.freeInputs.freeInputDetails.collectionInputs.CollectionInputs;
import utils.DTOInputExecution;

import java.util.ArrayList;
import java.util.List;

public class FreeInputEnumeratorController implements CollectionInputs {
    private FreeInputsController mainFreeInputsController;

    @FXML
    private Label userStringLabel;
    @FXML
    private ComboBox<String> freeInputEnumerator;

    private String name;
    private DataNecessity necessity;
    private SimpleBooleanProperty isInputFieldEmptyProperty;

    private List<String> choices;

    public FreeInputEnumeratorController() {
        isInputFieldEmptyProperty = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() {
        this.choices = new ArrayList<String>() {{
            add("ZIP");
            add("UNZIP");
        }};
        freeInputEnumerator.getItems().addAll(choices);
        freeInputEnumerator.valueProperty().addListener((observable, oldValue, newValue) -> {
            isInputFieldEmptyProperty.set(!newValue.isEmpty());
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
        return this.freeInputEnumerator.getValue();
    }

    public void updateDetails(DTOInputExecution inputExecution)
    {
        userStringLabel.setText(inputExecution.getFinalName());
    }

    @Override
    public void setFreeInput(Object item) {
        EnumeratorData enumeratorData = ((EnumeratorData)item);
        String choice = enumeratorData.getAllMembers();
        freeInputEnumerator.setValue(choice);
    }

    public void setMainController(FreeInputsController mainFreeInputsController) {
        this.mainFreeInputsController = mainFreeInputsController;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        //this.systemEngine = systemEngine;
    }

}
