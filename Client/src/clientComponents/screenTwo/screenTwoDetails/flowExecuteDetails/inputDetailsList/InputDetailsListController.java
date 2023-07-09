package clientComponents.screenTwo.screenTwoDetails.flowExecuteDetails.inputDetailsList;


import clientComponents.screenTwo.screenTwoDetails.flowExecuteDetails.FlowExecuteDetailsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import stepper.systemEngine.SystemEngineInterface;
import clientComponents.screenTwo.screenTwoDetails.flowExecuteDetails.inputDetailsList.inputBody.InputBodyController;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOInputFlowPastWeb;
import utils.DTOFullDetailsPastRun;
import utils.DTOInputFlowPast;

import java.io.IOException;

public class InputDetailsListController {

    private clientComponents.screenTwo.screenTwoDetails.flowExecuteDetails.FlowExecuteDetailsController maimFlowExecuteDetailsController;
    //private SystemEngineInterface systemEngine;
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
        //this.systemEngine = systemEngine;
        //inputDetailsComponentController.setSystemEngine(systemEngine);
    }

    public void setMainController(FlowExecuteDetailsController maimFlowExecuteDetailsController) {
        this.maimFlowExecuteDetailsController = maimFlowExecuteDetailsController;
    }

    public void updateDetailsFlowRun(DTOFullDetailsPastRunWeb endOFlowExecution) {

        inputListAccordion.getPanes().clear();

        for (DTOInputFlowPastWeb input : endOFlowExecution.getInputs()) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("inputBody/inputBody.fxml"));
                HBox contentPane = fxmlLoader.load();

                InputBodyController controller = fxmlLoader.getController();
                controller.setMainController(this);
                //controller.setSystemEngine(systemEngine);

                TitledPane titledPane = new TitledPane(input.getFinalName(), contentPane);

                controller.setFlowData(input);

                inputListAccordion.getPanes().add(titledPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}