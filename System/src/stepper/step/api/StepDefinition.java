package stepper.step.api;

import stepper.dataDefinition.api.DataDefinition;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.context.ExecutionContextInterface;

import java.io.Serializable;
import java.util.Map;

public interface StepDefinition extends Serializable
{
    String getStepName();
    boolean isReadonly();
    Map<String, DataDefinitionDeclaration> getInputs();
    Map<String, DataDefinitionDeclaration> getOutputs();

    DataDefinition getDataDefinitionByName(String dataName);
    StepResult invoke(ExecutionContextInterface context);

    boolean isInputExistInStep(String name);

    DataInFlow createDataInFlow(String originalName);
}
