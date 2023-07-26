package stepper.step.api;

import stepper.dataDefinition.api.DataDefinition;

import java.io.Serializable;

public interface DataDefinitionDeclaration extends Serializable
{
    String getName();
    DataNecessity necessity();
    String userString();
    DataDefinition dataDefinition();
    boolean isFile();
    String getEnumeratorType();
}
