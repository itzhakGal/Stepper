package subComponents.screenThree.lowerScreen.flowExecuteDetails.outputDetailsList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenThree.lowerScreen.flowExecuteDetails.FlowExecuteDetailsController;
import subComponents.screenThree.lowerScreen.flowExecuteDetails.outputDetailsList.outputBody.OutputBodyController;
import utils.DTOFullDetailsPastRun;
import utils.DTOOutPutFlowPast;

import java.io.IOException;

public class OutputDetailsListController {

    private FlowExecuteDetailsController maimFlowExecuteDetailsController;
    private SystemEngineInterface systemEngine;
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

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
        outputDetailsComponentController.setSystemEngine(systemEngine);
    }

    public void setMainController(FlowExecuteDetailsController maimFlowExecuteDetailsController) {
        this.maimFlowExecuteDetailsController = maimFlowExecuteDetailsController;
    }

    public void updateDetailsFlowRun(DTOFullDetailsPastRun endOFlowExecution) {

        outputListAccordion.getPanes().clear();

        for (DTOOutPutFlowPast output : endOFlowExecution.getOutputs()) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("outputBody/outputBody.fxml"));
                HBox contentPane = fxmlLoader.load();

                OutputBodyController controller = fxmlLoader.getController();
                controller.setMainController(this);
                controller.setSystemEngine(systemEngine);

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
