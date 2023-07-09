package stepper.flow.definition.api;

import stepper.step.api.DataDefinitionDeclaration;

import java.io.Serializable;

public interface DataUsageDeclaration extends Serializable {
    DataDefinitionDeclaration getDataDefinition();
    String getFinalName();
    DataKind getKind();
    void setFinalName(String finalName);
}
