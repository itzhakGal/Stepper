package subComponents.screenTwo.freeInputs.freeInputDetails.freeInputString;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import stepper.step.api.DataNecessity;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenTwo.freeInputs.FreeInputsController;
import subComponents.screenTwo.freeInputs.freeInputDetails.collectionInputs.CollectionInputs;
import utils.DTOInputExecution;

public class FreeInputDetailsController implements CollectionInputs {

    private FreeInputsController mainFreeInputsController;
    private SystemEngineInterface systemEngine;
    @FXML
    private Label userStringLabel;
    @FXML
    private TextArea freeInputTextArea;

    private String name;
    private DataNecessity necessity;
    private SimpleBooleanProperty isInputFieldEmptyProperty;
    //private SimpleStringProperty freeInputTextAreaProperty;
    private SimpleStringProperty userStringLabelProperty;

    public FreeInputDetailsController() {
        isInputFieldEmptyProperty = new SimpleBooleanProperty(false);
        //freeInputTextAreaProperty = new SimpleStringProperty();
        userStringLabelProperty = new SimpleStringProperty();
    }

    @FXML
    public void initialize() {
        freeInputTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            isInputFieldEmptyProperty.set(!newValue.isEmpty());
        });
        //freeInputTextArea.textProperty().bind(freeInputTextAreaProperty);
        userStringLabel.textProperty().bind(userStringLabelProperty);
    }

    public TextArea getFreeInputTextArea() {
        return freeInputTextArea;
    }

    @Override
    public void init(String name, DataNecessity necessity) {
        this.necessity = necessity;
        this.name = name;
    }

    public Label getFieldLabel() {
        return this.userStringLabel;
    }

    public void setFreeInputTextArea(TextArea freeInputTextArea) {
        this.freeInputTextArea = freeInputTextArea;
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
        return freeInputTextArea.getText();
    }

    public void setMainController(FreeInputsController mainFreeInputsController) {
        this.mainFreeInputsController = mainFreeInputsController;
    }
    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
    }

    public void updateDetails(DTOInputExecution inputExecution)
    {
        userStringLabelProperty.set(inputExecution.getFinalName());
        freeInputTextArea.setPromptText(inputExecution.getUserString());
    }

    public void setFreeInput(Object item)
    {
        freeInputTextArea.setText(item.toString());
    }

    public void setEditableTextArea()
    {
        freeInputTextArea.setEditable(false);
    }

}
