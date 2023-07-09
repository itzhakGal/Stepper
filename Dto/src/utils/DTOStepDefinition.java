package utils;

import stepper.flow.definition.api.StepUsageDeclaration;

public class DTOStepDefinition
{
    private String name;
    private String alias;
    private boolean readOnly;

    

    public DTOStepDefinition(StepUsageDeclaration step) {
        this.name = step.getStepName();
        this.alias = step.getFinalStepName();
        this.readOnly = step.getStepDefinition().isReadonly();
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

}