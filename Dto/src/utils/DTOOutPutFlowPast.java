package utils;

import stepper.flow.execution.context.DataInFlowExecution;
import stepper.step.api.DataNecessity;

public class DTOOutPutFlowPast
{
    private String finalName;
    private String type;
    private Object content;
    private  Class<?> classValue;

    private DataNecessity necessity;

    private String typePresentation;

    public DTOOutPutFlowPast(DataInFlowExecution dataExecution) {
        this.finalName=dataExecution.getDataDefinitionInFlow().getFinalName();
        this.type = dataExecution.getDataDefinitionInFlow().getDataDefinition().getName();
        this.content = dataExecution.getItem();
        this.classValue= dataExecution.getItem().getClass();
        this.necessity = dataExecution.getDataDefinitionInFlow().getNecessity();
        if(this.content.equals("Not created due to failure in flow"))
            this.typePresentation = "String";
        else
            this.typePresentation = dataExecution.getDataDefinitionInFlow().getDataType();
    }

    public String getFinalName() {
        return finalName;
    }

    public String getType() {
        return type;
    }

    public Object getContent() {
        return content;
    }

    public Class<?> getClassValue() {
        return classValue;
    }

    public DataNecessity getNecessity() {
        return necessity;
    }

    public String getTypePresentation() {
        return typePresentation;
    }
}
