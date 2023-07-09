package subComponents.screenTwo.screenTwoDetails.flowTree;

import javafx.application.Platform;
import utils.DTOFullDetailsPastRun;

import java.util.function.Consumer;

public class UIAdapter {
    private final Consumer<DTOFullDetailsPastRun> updateFlowResult;

  //  private final Runnable hideSpinner;
    public UIAdapter(Consumer<DTOFullDetailsPastRun> updateFlowResult) {
        this.updateFlowResult = updateFlowResult;
        //     this.hideSpinner = hideSpinner;
    }


    public void update(DTOFullDetailsPastRun flowExecutedDataDTO) {
        Platform.runLater(
                () -> {
                    updateFlowResult.accept(flowExecutedDataDTO);
                }
        );
    }

}
