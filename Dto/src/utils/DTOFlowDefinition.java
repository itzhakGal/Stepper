package utils;
import stepper.flow.definition.api.DataUsageDeclaration;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.definition.api.DataInFlow;
import utilsDesktopApp.DTOStepDefinitionJavaFX;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DTOFlowDefinition
{
    private String name;
    private String description;
    private List<String> formalOutputList;
    private boolean readOnly;
    private List<DTOStepDefinition> steps;
    private List<DTOInputDefinition> inputs;
    private List<DTOOutputDefinition> outputs;

    private List<DTOStepDefinitionJavaFX> stepDefinitionJavaFXList;

    public DTOFlowDefinition(FlowDefinition flowDefinition) {
        this.name = flowDefinition.getName();
        this.description = flowDefinition.getDescription();
        this.formalOutputList = new ArrayList<>();
        this.readOnly = flowDefinition.isReadonly();
        this.steps = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.stepDefinitionJavaFXList = new ArrayList<>();
        initialLists(flowDefinition);
        initialListStepDefinitionJavaFX(flowDefinition);
    }

    public void initialLists(FlowDefinition flowDefinition) {
        for (String str : flowDefinition.getFlowFormalOutputs()) {
            formalOutputList.add(str);
        }

        for (Map.Entry<String, DataInFlow> data: flowDefinition.getFreeInputs().entrySet()) {
            List<String> stepsNames = getNamesStepsOfInput(data.getKey(), flowDefinition);
            inputs.add(new DTOInputDefinition(data.getValue(), stepsNames));
        }

        for (StepUsageDeclaration step : flowDefinition.getFlowSteps()) {
            for (Map.Entry<String, DataInFlow> data : flowDefinition.getStepOutput().get(step.getFinalStepName()).entrySet()) {
                outputs.add(new DTOOutputDefinition(data.getValue(), step.getFinalStepName()));
            }
        }
        for (StepUsageDeclaration step : flowDefinition.getFlowSteps()) {
            steps.add(new DTOStepDefinition(step));
        }
    }

    public void initialListStepDefinitionJavaFX (FlowDefinition flowDefinition)
    {
        for (StepUsageDeclaration step : flowDefinition.getFlowSteps()) {
            stepDefinitionJavaFXList.add(new DTOStepDefinitionJavaFX(flowDefinition, step));
        }
    }

    public List<DTOStepDefinitionJavaFX> getStepDefinitionJavaFXList() {
        return stepDefinitionJavaFXList;
    }

    public List<String> getNamesStepsOfInput(String finalName , FlowDefinition flowDefinition){
        List<String> stepsNames = new ArrayList<>();
        for (StepUsageDeclaration step : flowDefinition.getFlowSteps())
        {
            DataUsageDeclaration dataUsageDeclaration = step.getDataUsageDeclarationByOriginalName(finalName);

            if (dataUsageDeclaration != null) {
                if (step.getStepDefinition().isInputExistInStep(dataUsageDeclaration.getDataDefinition().getName()))
                    stepsNames.add(step.getFinalStepName());
            }
    }
        return  stepsNames;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getFormalOutputList() {
        return formalOutputList;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public List<DTOStepDefinition> getSteps() {
        return steps;
    }

    public List<DTOInputDefinition> getInputs() {
        return inputs;
    }

    public List<DTOOutputDefinition> getOutputs() {
        return outputs;
    }

    }




