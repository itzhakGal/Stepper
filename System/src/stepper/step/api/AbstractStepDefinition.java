package stepper.step.api;

import stepper.dataDefinition.api.DataDefinition;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.definition.api.DataInFlowImp;
import stepper.flow.definition.api.DataKind;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractStepDefinition implements StepDefinition {

    private final String stepName;
    private final boolean readonly;
    private final Map<String, DataDefinitionDeclaration> inputs;
    private final Map<String, DataDefinitionDeclaration> outputs;

    public AbstractStepDefinition(String stepName, boolean readonly) {
        this.stepName = stepName;
        this.readonly = readonly;
        this.inputs = new HashMap<>();
        this.outputs = new HashMap<>();
    }

    protected void addInput(DataDefinitionDeclaration dataDefinitionDeclaration) {
        this.inputs.put(dataDefinitionDeclaration.getName(), dataDefinitionDeclaration);
    }

    protected void addOutput(DataDefinitionDeclaration dataDefinitionDeclaration) {
        this.outputs.put(dataDefinitionDeclaration.getName(), dataDefinitionDeclaration);
    }

    public DataInFlow createDataInFlow(String originalName)
    {
        DataInFlow data = new DataInFlowImp(originalName, outputs.get(originalName), DataKind.OUTPUT);
        return data;
    }

    @Override
    public String getStepName() {
        return this.stepName;
    }

    @Override
    public boolean isReadonly() {
        return this.readonly;
    }

    @Override
    public Map<String, DataDefinitionDeclaration> getInputs() {
        return this.inputs;
    }

    @Override
    public Map<String, DataDefinitionDeclaration> getOutputs() {
        return this.outputs;
    }

    @Override
    public boolean isInputExistInStep(String name)
    {
        return  inputs.containsKey(name);
    }

    @Override
    public DataDefinition getDataDefinitionByName(String dataName)
    {
        return  inputs.get(dataName).dataDefinition();
    }
}
