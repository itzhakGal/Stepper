package stepper.flow.definition.api;

import java.io.Serializable;

public interface FlowLevelAlias extends Serializable {

    String getAlias();
    String getSourceDataName();
    String getStepName();

}
