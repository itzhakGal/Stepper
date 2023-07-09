package subComponents.screenOne.screenOneRight.freeInputDetails;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenOne.screenOneRight.SelectedFlowDetailsController;
import utils.DTOFlowDefinition;
import utils.DTOInputDefinition;

import java.util.List;

public class FreeInputDetailsController {

    private SelectedFlowDetailsController mainSelectedFlowDetailsController;

    private SystemEngineInterface systemEngine;

    @FXML
    private SplitMenuButton relatedStepButton;

    @FXML
    private Label finalNameInputLabel;

    @FXML
    private Label typeInputLabel;

    @FXML
    private Label mandatoryInputLabel;

    @FXML
    private ListView<String> freeInputListView;

    @FXML
    private Label typeLabelLeft;

    @FXML
    private Label finalNameLabelLeft;

    @FXML
    private Label relatedStepLabelLeft;

    @FXML
    private Label isMandatoryLabelLeft;

    private ChangeListener<String> freeInputListener;

    @FXML
    void relatedStepButtonAction(ActionEvent event) {

    }

    public void setMainController(SelectedFlowDetailsController mainSelectedFlowDetailsController) {
        this.mainSelectedFlowDetailsController = mainSelectedFlowDetailsController;
    }

    public void setSystemEngine(SystemEngineInterface engineManager) {
        this.systemEngine = engineManager;
    }

    public void setFreeInputOfFlowSelected(DTOFlowDefinition dtoFlowDefinition)
    {
        freeInputListView.getItems().remove(0,freeInputListView.getItems().size());

        for(DTOInputDefinition freeInput :dtoFlowDefinition.getInputs())
        {
            addValueToInputList(freeInput.getName());
        }
        updateFreeInputDetails(dtoFlowDefinition.getInputs());
    }
    public void addValueToInputList(String value) {
        ObservableList<String> items = freeInputListView.getItems();
        items.add(value);
    }

    private void updateFreeInputDetails(List<DTOInputDefinition> freeInputs) {
        if (freeInputListener != null) {
            freeInputListView.getSelectionModel().selectedItemProperty().removeListener(freeInputListener);
        }

        freeInputListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setVisibleLabels();
                String currentSelected = freeInputListView.getSelectionModel().getSelectedItem();
                if (currentSelected != null) {
                    DTOInputDefinition freeInputDefinition = getInputDetails(currentSelected, freeInputs);
                    if (freeInputDefinition.isMandatory())
                        mandatoryInputLabel.setText("Yes");
                    else
                        mandatoryInputLabel.setText("No");

                    finalNameInputLabel.setText(freeInputDefinition.getName());
                    typeInputLabel.setText(freeInputDefinition.getType());
                    relatedStepButton.getItems().clear();
                    for (String relatedStep : freeInputDefinition.getRelatedSteps()) {
                        MenuItem menuItem1 = new MenuItem(relatedStep);
                        relatedStepButton.getItems().add(menuItem1);
                    }
                }
            }
        };

        freeInputListView.getSelectionModel().selectedItemProperty().addListener(freeInputListener);
    }

    public DTOInputDefinition getInputDetails(String currentSelected, List<DTOInputDefinition> freeInputs)
    {
        for (DTOInputDefinition freeInputDefinition : freeInputs)
        {
            if(freeInputDefinition.getName().equals(currentSelected))
                return freeInputDefinition;
        }
        return null;
    }

    private void setVisibleLabels() {
        typeLabelLeft.setVisible(true);
        finalNameLabelLeft.setVisible(true);
        relatedStepLabelLeft.setVisible(true);
        isMandatoryLabelLeft.setVisible(true);
        relatedStepButton.setVisible(true);
    }

}
