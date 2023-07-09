package subComponents.screenThree.lowerScreen.flowExecuteDetails.inputDetailsList;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenThree.lowerScreen.flowExecuteDetails.FlowExecuteDetailsController;
import subComponents.screenThree.lowerScreen.flowExecuteDetails.inputDetailsList.inputBody.InputBodyController;
import utils.DTOFullDetailsPastRun;
import utils.DTOInputFlowPast;

import java.io.IOException;

public class InputDetailsListController {

    private FlowExecuteDetailsController maimFlowExecuteDetailsController;
    private SystemEngineInterface systemEngine;
    @FXML
    private HBox inputDetailsComponent;
    @FXML
    private InputBodyController inputDetailsComponentController;
    @FXML
    private Accordion inputListAccordion;

    @FXML
    public void initialize() {
        if (inputDetailsComponentController != null) {
            inputDetailsComponentController.setMainController(this);
        }
    }

    public void setSystemEngine(SystemEngineInterface systemEngine) {
        this.systemEngine = systemEngine;
        inputDetailsComponentController.setSystemEngine(systemEngine);
    }

    public void setMainController(FlowExecuteDetailsController maimFlowExecuteDetailsController) {
        this.maimFlowExecuteDetailsController = maimFlowExecuteDetailsController;
    }

    public void updateDetailsFlowRun(DTOFullDetailsPastRun endOFlowExecution) {

        inputListAccordion.getPanes().clear();

        for (DTOInputFlowPast input : endOFlowExecution.getInputs()) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("inputBody/inputBody.fxml"));
                HBox contentPane = fxmlLoader.load();

                InputBodyController controller = fxmlLoader.getController();
                controller.setMainController(this);
                controller.setSystemEngine(systemEngine);

                TitledPane titledPane = new TitledPane(input.getFinalName(), contentPane);

                // Store the controller in the TitledPane's properties
                controller.setFlowData(input);

                inputListAccordion.getPanes().add(titledPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}