package utils;

import stepper.flow.definition.api.DataInFlow;

public class DTOOutputDefinition
{
    private String name;
    private String type;
    private String stepName;


    public DTOOutputDefinition(DataInFlow data, String stepName) {
        this.name = data.getFinalName();
        this.type = data.getDataDefinition().getName();
        this.stepName= stepName;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStepName() {
        return stepName;
    }
}



