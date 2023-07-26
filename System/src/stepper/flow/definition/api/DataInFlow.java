package stepper.flow.definition.api;

import stepper.dataDefinition.api.DataDefinition;
import stepper.step.api.DataNecessity;
import java.io.Serializable;
import java.util.List;

public interface DataInFlow extends Serializable {

    String getFinalName();
    String getName();
    String getUserString();
    DataNecessity getNecessity();
    DataKind getDataKind();
    DataDefinition getDataDefinition();
    String getDataType();
    List<String> getRelatedSteps();
    void setDataType(String dataType);
    void setFinalName(String finalName);
    void setNecessity(DataNecessity necessity);
    boolean isFile();

    String getEnumeratorType();
}
