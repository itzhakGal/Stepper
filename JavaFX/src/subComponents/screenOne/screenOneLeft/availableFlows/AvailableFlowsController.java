package subComponents.screenOne.screenOneLeft.availableFlows;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenOne.flowDefinitionScreen.FlowDefinitionScreenController;
import subComponents.screenOne.screenOneLeft.availableFlows.flowDefinitionDetails.FlowDefinitionDetailsController;
import utilsDesktopApp.DTOFlowDetails;
import utilsDesktopApp.DTOListFlowsDetails;

import java.io.IOException;

public class AvailableFlowsController {

    private FlowDefinitionScreenController mainFlowDefinitionController;
    private SystemEngineInterface systemEngine;
    @FXML
    private Accordion listFlowsName;
    @FXML private HBox detailsComponent;
    @FXML private FlowDefinitionDetailsController detailsComponentController;

    @FXML private Label availableFlowsLabel;
    private SimpleBooleanProperty isAccordionClicked;


    @FXML
    public void initialize() {
        if (detailsComponentController != null) {
            detailsComponentController.setMainController(this);
        }
        isAccordionClicked = new SimpleBooleanProperty(false);
    }

    public void initListener()
    {
        mainFlowDefinitionController.getMainBodyController().getMainController().getHeaderComponentController().getHeaderBodyComponentController().getLoadFileButtonProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue){
                        // Delete every component here
                        listFlowsName.getPanes().clear();
                        detailsComponent.getChildren().clear();
                        availableFlowsLabel.setVisible(false);
                    }
                });

        listFlowsName.expandedPaneProperty().addListener((observable, oldPane, newPane) -> {
            if (newPane != null) {
                isAccordionClicked.set(true);
            } else {
                isAccordionClicked.set(false);
            }
        });

        listFlowsName.expandedPaneProperty().addListener((observable, oldPane, newPane) -> {
            if (newPane != null && mainFlowDefinitionController.getMainBodyController().getMainController().getHeaderComponentController().getCheckboxValueProperty().getValue()) {
                animateAccordion(newPane);
            }
        });
    }

    private void animateAccordion(TitledPane pane) {
        FadeTransition fadeAnimation = new FadeTransition(Duration.seconds(0.5), pane.getContent());

        if (pane.isExpanded()) {
            fadeAnimation.setFromValue(0.0);
            fadeAnimation.setToValue(1.0);
        } else {
            fadeAnimation.setFromValue(1.0);
            fadeAnimation.setToValue(0.0);
        }

        fadeAnimation.play();
    }
    public void initListFlows() {
        availableFlowsLabel.setVisible(true);
        DTOListFlowsDetails listFlowsDetails = systemEngine.readFlowsDetails();

        listFlowsName.getPanes().clear();

        for (DTOFlowDetails flowDetails : listFlowsDetails.getDtoFlowDetailsList()) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flowDefinitionDetails/flowDefinitionDetails.fxml"));
                HBox contentPane = fxmlLoader.load();

                FlowDefinitionDetailsController controller = fxmlLoader.getController();
                controller.setMainController(this);
                controller.setSystemEngine(systemEngine);

                TitledPane titledPane = new TitledPane(flowDetails.getName(), contentPane);
                titledPane.getProperties().put("controller", controller);

                controller.setFlowData(flowDetails);

                listFlowsName.getPanes().add(titledPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void setMainController(FlowDefinitionScreenController flowDefinitionController) {
        this.mainFlowDefinitionController = flowDefinitionController;
    }
    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
        this.detailsComponentController.setSystemEngine(systemEngine);
    }

    public void setFlowSelectedDetails(String flowName)
    {
        mainFlowDefinitionController.setFlowSelectedDetails(flowName);
    }

    public SimpleBooleanProperty isAccordionClickedProperty() {
        return isAccordionClicked;
    }


}
