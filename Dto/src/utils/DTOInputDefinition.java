package utils;

import stepper.flow.definition.api.DataInFlow;
import stepper.step.api.DataDefinitionDeclaration;
import stepper.step.api.DataNecessity;

import java.util.ArrayList;
import java.util.List;

public class DTOInputDefinition {
    private String name;
    private List<String> relatedSteps;
    private String type;
    private boolean mandatory;

    public DTOInputDefinition(DataInFlow data , List<String> stepsNames)
    {
        boolean necessity= false;
        this.name = data.getFinalName();
        this.relatedSteps = data.getRelatedSteps();
        this.type = data.getDataDefinition().getName();
        if(data.getNecessity() == DataNecessity.MANDATORY)
            necessity=true;
        this.mandatory = necessity;
    }
    public String getName() {
        return name;
    }
    public List<String> getRelatedSteps() {
        return relatedSteps;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public String getType() {
        return type;
    }
}