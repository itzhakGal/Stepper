package utilWebApp;

import stepper.flow.execution.context.DataInFlowExecution;
import stepper.step.api.DataNecessity;

public class DTOInputFlowPastWeb {

    private String finalName;
    private String type;

    private String typePresentation;
    private Object value;
    private DataNecessity necessity;
    private  String classValue;

    public DTOInputFlowPastWeb(DataInFlowExecution dataExecution) {
        this.finalName = dataExecution.getDataDefinitionInFlow().getFinalName();
        this.type = dataExecution.getDataDefinitionInFlow().getDataDefinition().getName();
        this.value = dataExecution.getItem();
        this.necessity = dataExecution.getDataDefinitionInFlow().getNecessity();
        this.classValue= dataExecution.getDataDefinitionInFlow().getDataDefinition().getType().getName();
        this.typePresentation = dataExecution.getDataDefinitionInFlow().getDataType();
    }

    // Getter methods
    public String getFinalName() {
        return finalName;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public DataNecessity getNecessity() {
        return necessity;
    }

    public String getClassValue() {
        return classValue;
    }

    public String getTypePresentation() {
        return typePresentation;
    }
}
