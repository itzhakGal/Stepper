package utilsDesktopApp;

import stepper.flow.definition.api.DataInFlow;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.step.api.DataDefinitionDeclaration;
import stepper.step.api.DataNecessity;

import java.util.Map;

public class DTOInputDetailsConnectedToStep {
    private String finalName;
    private boolean isMandatory;
    private String connectedToOutput;

    private String NameOfTheConnectedStep;

    public DTOInputDetailsConnectedToStep(Map.Entry<String, DataDefinitionDeclaration> input, FlowDefinition flowDefinition)
    {
        this.finalName = input.getKey();
        if(input.getValue().necessity().equals(DataNecessity.MANDATORY))
            this.isMandatory = true;
        else
            this.isMandatory = false;

        this.connectedToOutput = "No output connected";
        this.NameOfTheConnectedStep = "No step connected";

        for (StepUsageDeclaration step : flowDefinition.getFlowSteps()) {
            for (Map.Entry<String, DataInFlow> output : flowDefinition.getStepOutput().get(step.getFinalStepName()).entrySet()) {
                if(output.getValue().getFinalName().equals(this.finalName))
                {
                    setConnectedToOutput(output.getKey());
                    setNameOfTheConnectedStep(step.getFinalStepName());
                }
            }
        }

    }

    public void setConnectedToOutput(String connectedToOutput) {
        this.connectedToOutput = connectedToOutput;
    }

    public void setNameOfTheConnectedStep(String nameOfTheConnectedStep) {
        NameOfTheConnectedStep = nameOfTheConnectedStep;
    }

    public String getFinalName() {
        return finalName;
    }

    public String getConnectedToOutput() {
        return connectedToOutput;
    }
    public boolean isMandatory() {
        return isMandatory;
    }

    public String getNameOfTheConnectedStep() {
        return NameOfTheConnectedStep;
    }
}
