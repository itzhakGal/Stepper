package subComponents.screenOne.screenOneRight.stepsList.stepDetails;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenOne.screenOneRight.stepsList.StepListController;
import utilsDesktopApp.DTOInputDetailsConnectedToStep;
import utilsDesktopApp.DTOOutputDetailsConnectedToStep;
import utilsDesktopApp.DTOStepDefinitionJavaFX;

import java.util.List;
import java.util.stream.Collectors;

public class StepDetailsController {
    private StepListController mainStepListController;
    private SystemEngineInterface systemEngine;

    @FXML
    private ListView<String> inputListLV;
    @FXML
    private ListView<String> outputListLV;
    @FXML
    private Label IsReadOnlyLabel;
    @FXML
    private TextField connectedToOutputTF;
    @FXML
    private TextField connectedStepTFInput;

    @FXML
    private TextField isMandatoryTFInput;

    @FXML
    private TextField connectedToInputTF;

    @FXML
    private TextField connectedStepTFOutput;

    @FXML
    private TextField isMandatoryTFOutput;
    @FXML
    private Label isMandatoryLabelInput;

    @FXML
    private Label connectedToOutputLabelInput;

    @FXML
    private Label nameOfConnectedStepLabelInput;

    @FXML
    private Label isMandatoryLabelOutput;

    @FXML
    private Label connectedToInputLabelOutput;

    @FXML
    private Label nameOfConnectedStepLabelOutput;


    @FXML
    void setInputListAction(ActionEvent event) {

    }

    @FXML
    void setOutputListAction(ActionEvent event) {

    }


    public void setMainController(StepListController mainStepListController) {
        this.mainStepListController = mainStepListController;
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
    }

    public void initialDataInAllOfLabelOfTheContent(DTOStepDefinitionJavaFX stepDefinitionJavaFX) {

        if(stepDefinitionJavaFX.isReadOnly())
            IsReadOnlyLabel.setText("Yes");
        else
            IsReadOnlyLabel.setText("No");

        initialListsView(stepDefinitionJavaFX);

        updateInputDetails(stepDefinitionJavaFX);
        updateOutputDetails(stepDefinitionJavaFX);
    }

    private void updateOutputDetails(DTOStepDefinitionJavaFX stepDefinitionJavaFX) {
        outputListLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setVisibleLabelsOutput();
                String currentSelected = outputListLV.getSelectionModel().getSelectedItem();
                DTOOutputDetailsConnectedToStep outputDetailsConnectedToStep = getOutputDetails(currentSelected, stepDefinitionJavaFX);
                if(outputDetailsConnectedToStep.isMandatory())
                    isMandatoryTFOutput.setText("Yes");
                else
                    isMandatoryTFOutput.setText("No");

                connectedToInputTF.setText(outputDetailsConnectedToStep.getConnectedToInput());
                connectedStepTFOutput.setText(outputDetailsConnectedToStep.getNameOfTheConnectedStep());
            }
        });
    }

    private void updateInputDetails(DTOStepDefinitionJavaFX stepDefinitionJavaFX) {
        inputListLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                    setVisibleLabelsInput();

                String currentSelected = inputListLV.getSelectionModel().getSelectedItem();
                DTOInputDetailsConnectedToStep inputDetailsConnectedToStep = getInputDetails(currentSelected, stepDefinitionJavaFX);
                if(inputDetailsConnectedToStep.isMandatory())
                    isMandatoryTFInput.setText("Yes");
                else
                    isMandatoryTFInput.setText("No");

                connectedToOutputTF.setText(inputDetailsConnectedToStep.getConnectedToOutput());
                connectedStepTFInput.setText(inputDetailsConnectedToStep.getNameOfTheConnectedStep());
            }
        });
    }

    private void setVisibleLabelsInput() {
        isMandatoryLabelInput.setVisible(true);
        connectedToOutputLabelInput.setVisible(true);
        nameOfConnectedStepLabelInput.setVisible(true);
        connectedToOutputTF.setVisible(true);
        isMandatoryTFInput.setVisible(true);
        connectedStepTFInput.setVisible(true);
    }

    private void setVisibleLabelsOutput() {
        isMandatoryLabelOutput.setVisible(true);
        connectedToInputLabelOutput.setVisible(true);
        nameOfConnectedStepLabelOutput.setVisible(true);
        connectedToInputTF.setVisible(true);
        isMandatoryTFOutput.setVisible(true);
        connectedStepTFOutput.setVisible(true);
    }

    private void initialListsView(DTOStepDefinitionJavaFX stepDefinitionJavaFX) {
        List<String> inputItems = stepDefinitionJavaFX.getListDTOInputDetailsConnectedToStep().stream()
                .map(DTOInputDetailsConnectedToStep::getFinalName)
                .collect(Collectors.toList());

        inputListLV.setItems(FXCollections.observableArrayList(inputItems));

        List<String> outputItems = stepDefinitionJavaFX.getListDTOOutputDetailsConnectedToStep().stream()
                .map(DTOOutputDetailsConnectedToStep::getFinalName)
                .collect(Collectors.toList());

        outputListLV.setItems(FXCollections.observableArrayList(outputItems));
    }



    public DTOInputDetailsConnectedToStep getInputDetails(String currentSelected, DTOStepDefinitionJavaFX stepDefinitionJavaFX)
    {
        for (DTOInputDetailsConnectedToStep inputDetailsConnectedToStep : stepDefinitionJavaFX.getListDTOInputDetailsConnectedToStep())
        {
           if(inputDetailsConnectedToStep.getFinalName().equals(currentSelected))
               return inputDetailsConnectedToStep;
        }
        return null;
    }

    public DTOOutputDetailsConnectedToStep getOutputDetails(String currentSelected, DTOStepDefinitionJavaFX stepDefinitionJavaFX)
    {
        for (DTOOutputDetailsConnectedToStep outputDetailsConnectedToStep : stepDefinitionJavaFX.getListDTOOutputDetailsConnectedToStep())
        {
            if(outputDetailsConnectedToStep.getFinalName().equals(currentSelected))
                return outputDetailsConnectedToStep;
        }
        return null;
    }



    public void addValueToInputList(String value) {
        ObservableList<String> items = inputListLV.getItems();
        items.add(value);
    }

    public void addValueToOutputList(String value) {
        ObservableList<String> items = outputListLV.getItems();
        items.add(value);
    }
}
