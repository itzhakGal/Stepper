package utilWebApp;

import stepper.flow.definition.api.DataInFlow;
import stepper.flow.definition.api.DataKind;
import stepper.step.api.DataNecessity;

import java.util.ArrayList;
import java.util.List;

public class DTODataInFlow {

    private String finalName;
    private  String name;
    private DTODataNecessity necessity;
    private String userString;
    private DTODataDefinition dataDefinition;
    private String dataType;
    private DTODataKind dataKind;
    private List<String> relatedSteps;

    private boolean isFile;

    public DTODataInFlow(DataInFlow dataInFlow)
    {
        this.finalName= dataInFlow.getFinalName();
        this.name = dataInFlow.getName();
        this.userString = dataInFlow.getUserString();

        if(dataInFlow.getNecessity() == DataNecessity.MANDATORY)
            this.necessity = DTODataNecessity.MANDATORY;
        else if(dataInFlow.getNecessity() == DataNecessity.OPTIONAL)
            this.necessity = DTODataNecessity.OPTIONAL;
        else
            this.necessity = DTODataNecessity.NA;

        if(dataInFlow.getDataKind() == DataKind.INPUT)
            this.dataKind = DTODataKind.INPUT;
        else
            this.dataKind = DTODataKind.OUTPUT;


        this.dataDefinition = new DTODataDefinition(dataInFlow.getDataDefinition());
        this.dataType= dataInFlow.getDataType();
        this.relatedSteps = new ArrayList<>();
        this.isFile = dataInFlow.isFile();
    }

    public String getFinalName() {
        return finalName;
    }

    public String getName() {
        return name;
    }
    public List<String> getRelatedSteps() {
        return relatedSteps;
    }
    public String getUserString() {
        return userString;
    }
    public DTODataKind getDataKind() {
        return dataKind;
    }

    public DTODataNecessity getNecessity() {
        return necessity;
    }

    public DTODataDefinition getDataDefinition() {
        return dataDefinition;
    }

    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public void setNecessity(DTODataNecessity necessity) {
        this.necessity = necessity;
    }

    public boolean isFile() {
        return isFile;
    }
}
