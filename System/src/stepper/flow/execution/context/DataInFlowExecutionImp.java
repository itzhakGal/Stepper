package stepper.flow.execution.context;

import stepper.flow.definition.api.DataInFlow;

public class DataInFlowExecutionImp implements DataInFlowExecution
{
    private DataInFlow dataDefinitionInFlow;
    private Object item;

    public DataInFlowExecutionImp (DataInFlow dataDefinitionInFlow)
    {
        this.dataDefinitionInFlow= dataDefinitionInFlow;
        this.item = null;
    }
    @Override
    public DataInFlow getDataDefinitionInFlow() {
        return dataDefinitionInFlow;
    }
    @Override
    public Object getItem() {
        return item;
    }
    @Override
    public void setItem(Object item) {
        this.item = item;
    }

    @Override
    public void setFinalName(String finalName)
    {
        this.dataDefinitionInFlow.setFinalName(finalName);
    }


}
