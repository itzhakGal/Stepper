package utilsDesktopApp;

import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.step.api.DataDefinitionDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DTOStepDefinitionJavaFX
{
    private String finalName;
    private boolean readOnly;

    List<DTOInputDetailsConnectedToStep> listDTOInputDetailsConnectedToStep;

    List<DTOOutputDetailsConnectedToStep> listDTOOutputDetailsConnectedToStep;

    public DTOStepDefinitionJavaFX(FlowDefinition flowDefinition, StepUsageDeclaration step) {
        this.finalName = step.getStepName();
        this.readOnly = step.getStepDefinition().isReadonly();
        this.listDTOInputDetailsConnectedToStep = new ArrayList<>();
        this.listDTOOutputDetailsConnectedToStep = new ArrayList<>();
        initialLists(flowDefinition, step);
    }

    public void initialLists (FlowDefinition flowDefinition, StepUsageDeclaration step)
    {
        for(Map.Entry<String, DataDefinitionDeclaration> input: step.getStepDefinition().getInputs().entrySet())
        {
            listDTOInputDetailsConnectedToStep.add(new DTOInputDetailsConnectedToStep(input, flowDefinition));
        }

        for(Map.Entry<String, DataDefinitionDeclaration> output: step.getStepDefinition().getOutputs().entrySet())
        {
            listDTOOutputDetailsConnectedToStep.add(new DTOOutputDetailsConnectedToStep(output, flowDefinition));
        }

    }
    public String getFinalName() {
        return finalName;
    }


    public boolean isReadOnly() {
        return readOnly;
    }

    public List<DTOInputDetailsConnectedToStep> getListDTOInputDetailsConnectedToStep() {
        return listDTOInputDetailsConnectedToStep;
    }

    public List<DTOOutputDetailsConnectedToStep> getListDTOOutputDetailsConnectedToStep() {
        return listDTOOutputDetailsConnectedToStep;
    }
}