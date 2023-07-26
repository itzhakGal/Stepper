package clientComponents.screenOne.screenOneRight.stepsList;

import clientComponents.screenOne.screenOneRight.SelectedFlowDetailsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenOne.screenOneRight.stepsList.stepDetails.StepDetailsController;
import utils.DTOFlowDefinition;
import utilsDesktopApp.DTOStepDefinitionJavaFX;

import java.io.IOException;

public class StepListController {

    private clientComponents.screenOne.screenOneRight.SelectedFlowDetailsController mainSelectedFlowDetailsController;
    @FXML
    private Accordion stepsListAccordion;
    @FXML
    private GridPane stepDetailsComponent;
    @FXML
    private StepDetailsController stepDetailsComponentController;

    @FXML
    public void initialize() {
        if (stepDetailsComponentController != null) {
            stepDetailsComponentController.setMainController(this);
            //this.stepDetailsComponentController.setSystemEngine(systemEngine);
        }
    }

    public void setMainController(SelectedFlowDetailsController mainSelectedFlowDetailsController) {
        this.mainSelectedFlowDetailsController = mainSelectedFlowDetailsController;
    }

    public void setFlowSelectedDetails(DTOFlowDefinition dtoFlowDefinition) {
        stepsListAccordion.getPanes().clear();
       for (DTOStepDefinitionJavaFX stepDefinitionJavaFX : dtoFlowDefinition.getStepDefinitionJavaFXList()) {
           try {
               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stepDetails/stepDetails.fxml"));
               GridPane content = fxmlLoader.load();

               StepDetailsController controller = fxmlLoader.getController();
               controller.setMainController(this);
               //controller.setSystemEngine(systemEngine);

               controller.initialDataInAllOfLabelOfTheContent(stepDefinitionJavaFX);


               TitledPane titledPane = new TitledPane(stepDefinitionJavaFX.getFinalName(), content);
               stepsListAccordion.getPanes().add(titledPane);
           } catch (IOException e) {
               e.printStackTrace();
           }

       }
    }

}
