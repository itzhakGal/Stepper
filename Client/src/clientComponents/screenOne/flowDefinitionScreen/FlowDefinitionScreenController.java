package clientComponents.screenOne.flowDefinitionScreen;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.mainScreen.body.BodyController;
import clientComponents.screenOne.screenOneLeft.availableFlows.AvailableFlowsController;
import clientComponents.screenOne.screenOneRight.SelectedFlowDetailsController;

public class FlowDefinitionScreenController {
    private BodyController mainBodyController;
    @FXML
    private BorderPane availableFlowsComponent;
    @FXML private AvailableFlowsController availableFlowsComponentController;
    @FXML
    private GridPane selectedFlowDetailsComponent;
    @FXML private SelectedFlowDetailsController selectedFlowDetailsComponentController;
    @FXML
    public void initialize() {
        if (availableFlowsComponentController != null && selectedFlowDetailsComponentController != null) {
            availableFlowsComponentController.setMainController(this);
            selectedFlowDetailsComponentController.setMainController(this);
        }
    }

    public void updateExecuteFlowButton(String flowName)
    {
        mainBodyController.updateExecuteFlowButton(flowName);
    }

    public void setMainController(BodyController mainBodyController) {
        this.mainBodyController = mainBodyController;
    }

    public void setListFlowsDetails()
    {
        availableFlowsComponentController.startListRefresher();
    }

    public void setFlowSelectedDetails(String flowName)
    {
        selectedFlowDetailsComponent.setVisible(true);
        selectedFlowDetailsComponentController.setFlowSelectedDetails(flowName);
    }

    public void openFlowExecutionTab() {
        mainBodyController.openFlowExecutionTab();
    }

    public SimpleBooleanProperty getIsExecuteFlowButtonClicked() {
        SimpleBooleanProperty booleanProperty = selectedFlowDetailsComponentController.getIsExecuteFlowButtonClicked();
        return booleanProperty;
    }

    public BodyController getMainBodyController() {
        return mainBodyController;
    }

    public GridPane getSelectedFlowDetailsComponent() {
        return selectedFlowDetailsComponent;
    }

    public void initListener() {
        availableFlowsComponentController.initListener();
        selectedFlowDetailsComponentController.initListener();
    }
    public AvailableFlowsController getAvailableFlowsComponentController() {
        return availableFlowsComponentController;
    }
}
