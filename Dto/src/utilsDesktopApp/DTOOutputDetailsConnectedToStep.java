package utilsDesktopApp;

import stepper.flow.definition.api.DataInFlow;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.step.api.DataDefinitionDeclaration;
import stepper.step.api.DataNecessity;

import java.util.Map;

public class DTOOutputDetailsConnectedToStep {
    private String finalName;
    private boolean isMandatory;
    private String connectedToInput;

    private String NameOfTheConnectedStep;

    public DTOOutputDetailsConnectedToStep(Map.Entry<String, DataDefinitionDeclaration> output, FlowDefinition flowDefinition)
    {
        this.finalName = output.getKey();
        if(output.getValue().necessity().equals(DataNecessity.MANDATORY))
            this.isMandatory = true;
        else
            this.isMandatory = false;

        this.connectedToInput = "No output connected";
        this.NameOfTheConnectedStep = "No step connected";

        for (StepUsageDeclaration step : flowDefinition.getFlowSteps()) {
            for (Map.Entry<String, DataInFlow> input : flowDefinition.getStepInputs().get(step.getFinalStepName()).entrySet()) {
                if(input.getValue().getFinalName().equals(this.finalName))
                {
                    setConnectedToInput(input.getKey());
                    setNameOfTheConnectedStep(step.getFinalStepName());
                }
            }
        }

    }

    public void setNameOfTheConnectedStep(String nameOfTheConnectedStep) {
        NameOfTheConnectedStep = nameOfTheConnectedStep;
    }

    public void setConnectedToInput(String connectedToInput) {
        this.connectedToInput = connectedToInput;
    }

    public String getFinalName() {
        return finalName;
    }

    public String getConnectedToInput() {
        return connectedToInput;
    }
    public boolean isMandatory() {
        return isMandatory;
    }

    public String getNameOfTheConnectedStep() {
        return NameOfTheConnectedStep;
    }
}
