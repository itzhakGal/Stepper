package stepper.flow.definition.api;

import stepper.step.api.DataDefinitionDeclaration;

public class DataUsageDeclarationImpl implements DataUsageDeclaration {
    private String finalName;
    private DataDefinitionDeclaration dataDefinition;
    private DataKind kind;

    public DataUsageDeclarationImpl(String finalName, DataDefinitionDeclaration dataDefinition,  DataKind kind)
    {
        this.finalName=finalName;
        this.dataDefinition =  dataDefinition;
        this.kind = kind;
    }
    @Override
    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    @Override
    public DataDefinitionDeclaration getDataDefinition() {
        return dataDefinition;
    }
    @Override
    public String getFinalName() {
        return finalName;
    }
    @Override
    public DataKind getKind() {
        return kind;
    }
}
