package stepper.flow.execution.context;

import stepper.flow.definition.api.DataInFlow;
import java.io.Serializable;

public interface DataInFlowExecution extends Serializable
{
    DataInFlow getDataDefinitionInFlow();
    Object getItem();
    void setItem(Object item);
    void setFinalName(String finalName);
}
