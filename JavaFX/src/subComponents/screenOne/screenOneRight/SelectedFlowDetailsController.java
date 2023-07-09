package subComponents.screenOne.screenOneRight;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenOne.flowDefinitionScreen.FlowDefinitionScreenController;
import subComponents.screenOne.screenOneRight.freeInputDetails.FreeInputDetailsController;
import subComponents.screenOne.screenOneRight.outputDetails.OutputDetailsController;
import subComponents.screenOne.screenOneRight.stepsList.StepListController;
import utils.DTOFlowDefinition;

public class SelectedFlowDetailsController {
    private FlowDefinitionScreenController mainFlowDefinitionScreenController;
    private SystemEngineInterface systemEngine;
    @FXML
    private GridPane selectedFlowDetailsGridPane;
    @FXML
    private Label flowNameLabel;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Label formalOutputLabel;
    @FXML
    private Label readOnlyLabel;
    @FXML
    private HBox listStepAccordionComponent;
    @FXML private StepListController listStepAccordionComponentController;
    @FXML
    private GridPane freeInputGridPaneComponent;
    @FXML
    private FreeInputDetailsController freeInputGridPaneComponentController;
    @FXML
    private GridPane outputGridPaneComponent;
    @FXML
    private OutputDetailsController outputGridPaneComponentController;

    @FXML
    private Button executeFlowButton;

    private SimpleBooleanProperty isExecuteFlowButtonClicked;

    @FXML
    public void executeFlowButtonAction(Event event)
    {
        mainFlowDefinitionScreenController.updateExecuteFlowButton(flowNameLabel.getText());
        mainFlowDefinitionScreenController.openFlowExecutionTab();
        isExecuteFlowButtonClicked.set(true);
        isExecuteFlowButtonClicked.set(false);

    }
    @FXML
    public void initialize() {
        if (listStepAccordionComponentController != null && freeInputGridPaneComponentController != null && outputGridPaneComponentController != null) {
            listStepAccordionComponentController.setMainController(this);
            freeInputGridPaneComponentController.setMainController(this);
            outputGridPaneComponentController.setMainController(this);
        }
    }

    public SelectedFlowDetailsController() {
        isExecuteFlowButtonClicked = new SimpleBooleanProperty(false);
    }
    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
        this.listStepAccordionComponentController.setSystemEngine(systemEngine);
        this.freeInputGridPaneComponentController.setSystemEngine(systemEngine);
        this.outputGridPaneComponentController.setSystemEngine(systemEngine);
    }
    public void setMainController(FlowDefinitionScreenController mainFlowDefinitionScreenController) {
        this.mainFlowDefinitionScreenController = mainFlowDefinitionScreenController;

    }
    public void setFlowSelectedDetails(String flowName)
    {
        DTOFlowDefinition dtoFlowDefinition = systemEngine.introducingFlowDefinitionJavaFX(flowName);
        flowNameLabel.setText(dtoFlowDefinition.getName());
        descriptionTextArea.setText(dtoFlowDefinition.getDescription());
        String stringFormalOutput = String.join(", ", dtoFlowDefinition.getFormalOutputList());
        formalOutputLabel.setText(stringFormalOutput);
        if(dtoFlowDefinition.isReadOnly())
             readOnlyLabel.setText("Yes");
        else
            readOnlyLabel.setText("No");

        listStepAccordionComponentController.setFlowSelectedDetails(dtoFlowDefinition);
        freeInputGridPaneComponentController.setFreeInputOfFlowSelected(dtoFlowDefinition);
        outputGridPaneComponentController.setOutputOfFlowSelected(dtoFlowDefinition);
    }

    public SimpleBooleanProperty getIsExecuteFlowButtonClicked() {
        return isExecuteFlowButtonClicked;
    }

    public void initListener() {

        mainFlowDefinitionScreenController.getAvailableFlowsComponentController().isAccordionClickedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue){
                        // Delete every component here
                        selectedFlowDetailsGridPane.setVisible(false);
                    }
                });
    }
}