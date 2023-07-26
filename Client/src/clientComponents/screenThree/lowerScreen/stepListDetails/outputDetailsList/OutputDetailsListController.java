package clientComponents.screenThree.lowerScreen.stepListDetails.outputDetailsList;

import clientComponents.screenThree.lowerScreen.stepListDetails.StepListDetailsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenThree.lowerScreen.stepListDetails.outputDetailsList.outputBody.OutputBodyController;
import utilWebApp.DTOOutputDetailsWeb;
import utilsDesktopApp.DTOOutputDetailsJAVAFX;

import java.io.IOException;
import java.util.List;

public class OutputDetailsListController {

    private clientComponents.screenThree.lowerScreen.stepListDetails.StepListDetailsController maimStepListDetailsController;
    @FXML
    private HBox outputDetailsComponent;
    @FXML
    private OutputBodyController outputDetailsComponentController;
    @FXML
    private Accordion outputListAccordion;

    @FXML
    public void initialize() {
        if (outputDetailsComponentController != null) {
            outputDetailsComponentController.setMainController(this);
        }
    }

    public void setMainController(StepListDetailsController maimStepListDetailsController) {
        this.maimStepListDetailsController = maimStepListDetailsController;
    }

    public void updateOutputsStepDetails(List<DTOOutputDetailsWeb> dtoOutputDetails)
    {
        outputListAccordion.getPanes().clear();

        for (DTOOutputDetailsWeb output : dtoOutputDetails) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("outputBody/outputBody.fxml"));
                HBox contentPane = fxmlLoader.load();

                OutputBodyController controller = fxmlLoader.getController();
                controller.setMainController(this);
                //controller.setSystemEngine(systemEngine);

                TitledPane titledPane = new TitledPane(output.getFinalName(), contentPane);

                // Store the controller in the TitledPane's properties
                controller.setOutputData(output);

                outputListAccordion.getPanes().add(titledPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
