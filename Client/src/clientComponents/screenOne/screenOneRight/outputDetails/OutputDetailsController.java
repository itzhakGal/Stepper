package clientComponents.screenOne.screenOneRight.outputDetails;

import clientComponents.screenOne.screenOneRight.SelectedFlowDetailsController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import stepper.systemEngine.SystemEngineInterface;
import utils.DTOFlowDefinition;
import utils.DTOOutputDefinition;

import java.util.List;

public class OutputDetailsController {

    private clientComponents.screenOne.screenOneRight.SelectedFlowDetailsController mainSelectedFlowDetailsController;
    @FXML
    private Label finalNameOutputLabel;

    @FXML
    private Label typeOutputLabel;

    @FXML
    private Label producingStepLabel;

    @FXML
    private ListView<String> outputsListView;
    @FXML
    private Label typeLabelLeft;

    @FXML
    private Label finalNameLabelLeft;

    @FXML
    private Label producingStepLabelLeft;
    private ChangeListener<String> outputDetailsListener;


    public void setMainController(SelectedFlowDetailsController mainSelectedFlowDetailsController) {
        this.mainSelectedFlowDetailsController = mainSelectedFlowDetailsController;
    }

    public void setOutputOfFlowSelected(DTOFlowDefinition dtoFlowDefinition)
    {
        outputsListView.getItems().remove(0,outputsListView.getItems().size());
        for(DTOOutputDefinition outputDefinition :dtoFlowDefinition.getOutputs())
        {
            addValueToOutputList(outputDefinition.getName());
        }
        updateFreeInputDetails(dtoFlowDefinition.getOutputs());
    }
    public void addValueToOutputList(String value) {
        ObservableList<String> items = outputsListView.getItems();
        items.add(value);
    }

    private void updateFreeInputDetails(List<DTOOutputDefinition> outputs) {
        if (outputDetailsListener != null) {
            outputsListView.getSelectionModel().selectedItemProperty().removeListener(outputDetailsListener);
        }
        outputDetailsListener = new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setVisibleLabels();
                String currentSelected = outputsListView.getSelectionModel().getSelectedItem();
                if (currentSelected != null) {
                    DTOOutputDefinition outputDefinition = getOutputDetails(currentSelected, outputs);
                    finalNameOutputLabel.setText(outputDefinition.getName());
                    typeOutputLabel.setText(outputDefinition.getType());
                    producingStepLabel.setText(outputDefinition.getStepName());
                }
            }
        };
        outputsListView.getSelectionModel().selectedItemProperty().addListener(outputDetailsListener);
    }

    public DTOOutputDefinition getOutputDetails(String currentSelected, List<DTOOutputDefinition> outputs)
    {
        for (DTOOutputDefinition outputDefinition  : outputs)
        {
            if(outputDefinition.getName().equals(currentSelected))
                return outputDefinition;
        }
        return null;
    }

    private void setVisibleLabels() {
        typeLabelLeft.setVisible(true);
        finalNameLabelLeft.setVisible(true);
        producingStepLabelLeft.setVisible(true);
    }
}
