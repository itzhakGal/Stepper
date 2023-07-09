package utilsDesktopApp;


import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.context.DataInFlowExecution;

public class DTOFreeInputDetails {

    private DataInFlow dataDefinitionInFlow;
    private Object item;

    public DTOFreeInputDetails (DataInFlowExecution dataInFlowExecution)
    {
        this.dataDefinitionInFlow= dataInFlowExecution.getDataDefinitionInFlow();
        this.item = dataInFlowExecution.getItem();
    }
    public DataInFlow getDataDefinitionInFlow() {
        return dataDefinitionInFlow;
    }
    public Object getItem() {
        return item;
    }

}
