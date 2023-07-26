package stepper.flow.definition.api;

import stepper.dataDefinition.api.DataDefinition;
import stepper.step.api.DataDefinitionDeclaration;
import stepper.step.api.DataNecessity;

import java.util.ArrayList;
import java.util.List;

public class DataInFlowImp implements DataInFlow {

    private String finalName;
    private  String name;
    private DataNecessity necessity;
    private String userString;
    private DataDefinition dataDefinition;
    private String dataType;
    private DataKind dataKind;
    private List<String> relatedSteps;
    private boolean isFile;
    private String enumeratorType;

    public DataInFlowImp(String finalName, DataDefinitionDeclaration dataDefinitionDeclaration,DataKind dataKind)
    {
        this.finalName= finalName;
        this.name = dataDefinitionDeclaration.getName();
        this.necessity = dataDefinitionDeclaration.necessity();
        this.userString = dataDefinitionDeclaration.userString();
        this.dataDefinition = dataDefinitionDeclaration.dataDefinition();
        this.dataType= dataDefinitionDeclaration.dataDefinition().getType().getSimpleName();
        this.dataKind = dataKind;
        this.relatedSteps = new ArrayList<>();
        this.isFile = dataDefinitionDeclaration.isFile();
        this.enumeratorType = dataDefinitionDeclaration.getEnumeratorType();
    }
    @Override
    public String getFinalName() {
        return finalName;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public List<String> getRelatedSteps() {
        return relatedSteps;
    }

    @Override
    public String getUserString() {
        return userString;
    }
    @Override
    public DataKind getDataKind() {
        return dataKind;
    }

    @Override
    public DataNecessity getNecessity() {
        return necessity;
    }

    @Override
    public DataDefinition getDataDefinition() {
        return dataDefinition;
    }

    @Override
    public String getDataType() {
        return dataType;
    }

    @Override
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    @Override
    public void setNecessity(DataNecessity necessity) {
        this.necessity = necessity;
    }
    @Override
    public boolean isFile() {
        return isFile;
    }
    @Override
    public String getEnumeratorType() {
        return enumeratorType;
    }
}
