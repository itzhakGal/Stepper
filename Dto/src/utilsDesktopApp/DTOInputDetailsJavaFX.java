package utilsDesktopApp;

import stepper.flow.execution.context.DataInFlowExecution;
import stepper.step.api.DataNecessity;

public class DTOInputDetailsJavaFX {

    private String finalName;
    private String type;
    private Object value;
    private DataNecessity necessity;
    private  Class<?> classValue;
    private String typePresentation;

    public DTOInputDetailsJavaFX(DataInFlowExecution dataExecution) {
        this.finalName = dataExecution.getDataDefinitionInFlow().getFinalName();
        this.type = dataExecution.getDataDefinitionInFlow().getDataDefinition().getName();
        this.value = dataExecution.getItem();
        this.necessity = dataExecution.getDataDefinitionInFlow().getNecessity();
        this.classValue= dataExecution.getDataDefinitionInFlow().getDataDefinition().getType();
        this.typePresentation = dataExecution.getDataDefinitionInFlow().getDataType();
    }

    public DataNecessity getNecessity() {
        return necessity;
    }

    public String getFinalName() {
        return finalName;
    }

    public String getType() {
        return type;
    }

    public Class<?> getClassValue() {
        return classValue;
    }

    public Object getValue() {
        return value;
    }

    public String getTypePresentation() {
        return typePresentation;
    }
}
