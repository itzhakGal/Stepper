package clientComponents.screenTwo.screenTwoDetails.flowTree;

import javafx.application.Platform;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utils.DTOFullDetailsPastRun;

import java.util.function.Consumer;

public class UIAdapter {
    private final Consumer<DTOFullDetailsPastRunWeb> updateFlowResult;
    public UIAdapter(Consumer<DTOFullDetailsPastRunWeb> updateFlowResult) {
        this.updateFlowResult = updateFlowResult;
    }


    public void update(DTOFullDetailsPastRunWeb flowExecutedDataDTO) {
        Platform.runLater(
                () -> {
                    updateFlowResult.accept(flowExecutedDataDTO);
                }
        );
    }

}
