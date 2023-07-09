package utils;

import stepper.flow.execution.context.DataInFlowExecution;
import stepper.step.api.DataNecessity;

public class DTOInputFlowPast {

    private String finalName;
    private String type;

    private String typePresentation;
    private Object value;
    private DataNecessity necessity;
    private  Class<?> classValue;

    public DTOInputFlowPast(DataInFlowExecution dataExecution) {
        this.finalName = dataExecution.getDataDefinitionInFlow().getFinalName();
        this.type = dataExecution.getDataDefinitionInFlow().getDataDefinition().getName();
        this.value = dataExecution.getItem();
        this.necessity = dataExecution.getDataDefinitionInFlow().getNecessity();
        this.classValue= dataExecution.getDataDefinitionInFlow().getDataDefinition().getType();
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

    public Class<?> getClassValue() {
        return classValue;
    }

    public String getTypePresentation() {
        return typePresentation;
    }
}
