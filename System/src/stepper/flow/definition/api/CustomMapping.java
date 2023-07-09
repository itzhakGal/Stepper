package stepper.flow.definition.api;

import java.io.Serializable;

public interface CustomMapping extends Serializable {

    String getSourceData();

    String getSourceStep();

    String getTargetData();

    String getTargetStep();

}
