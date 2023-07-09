package utilWebApp;

import stepper.flow.execution.context.DataInFlowExecution;

public class DTODataInFlowExecution
{
    private DTODataInFlow dataDefinitionInFlow;
    private Object item;

    public DTODataInFlowExecution(DataInFlowExecution freeInputDataInFlow)
    {
        this.dataDefinitionInFlow= new DTODataInFlow(freeInputDataInFlow.getDataDefinitionInFlow());
        this.item = freeInputDataInFlow.getItem();
    }

    public void setDataDefinitionInFlow(DTODataInFlow dataDefinitionInFlow) {
        this.dataDefinitionInFlow = dataDefinitionInFlow;
    }

    public DTODataInFlow getDataDefinitionInFlow() {
        return dataDefinitionInFlow;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }



}
