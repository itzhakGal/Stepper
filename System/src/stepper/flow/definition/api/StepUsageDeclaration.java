package stepper.flow.definition.api;

import stepper.step.api.StepDefinition;

import java.io.Serializable;

public interface StepUsageDeclaration extends Serializable {
    String getFinalStepName();
    StepDefinition getStepDefinition();
    boolean skipIfFail();
    String getStepName();
    DataUsageDeclaration getDataUsageDeclarationByOriginalName(String originalName);
    DataUsageDeclaration getDataUsageDeclarationByFinalName(String finalName);
}
