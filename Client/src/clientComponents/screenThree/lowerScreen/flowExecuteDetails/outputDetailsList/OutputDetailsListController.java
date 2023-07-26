package clientComponents.screenThree.lowerScreen.flowExecuteDetails.outputDetailsList;

import clientComponents.screenThree.lowerScreen.flowExecuteDetails.FlowExecuteDetailsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenThree.lowerScreen.flowExecuteDetails.outputDetailsList.outputBody.OutputBodyController;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOOutPutFlowPastWeb;
import utils.DTOFullDetailsPastRun;
import utils.DTOOutPutFlowPast;

import java.io.IOException;

public class OutputDetailsListController {

    private clientComponents.screenThree.lowerScreen.flowExecuteDetails.FlowExecuteDetailsController maimFlowExecuteDetailsController;
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

    public void setMainController(FlowExecuteDetailsController maimFlowExecuteDetailsController) {
        this.maimFlowExecuteDetailsController = maimFlowExecuteDetailsController;
    }

    public void updateDetailsFlowRun(DTOFullDetailsPastRunWeb endOFlowExecution) {

        outputListAccordion.getPanes().clear();

        for (DTOOutPutFlowPastWeb output : endOFlowExecution.getOutputs()) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("outputBody/outputBody.fxml"));
                HBox contentPane = fxmlLoader.load();

                OutputBodyController controller = fxmlLoader.getController();
                controller.setMainController(this);
                //controller.setSystemEngine(systemEngine);

                TitledPane titledPane = new TitledPane(output.getFinalName(), contentPane);

                // Store the controller in the TitledPane's properties
                controller.setFlowData(output);

                outputListAccordion.getPanes().add(titledPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
