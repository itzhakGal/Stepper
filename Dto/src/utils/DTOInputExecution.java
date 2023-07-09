package utils;

import stepper.flow.definition.api.DataInFlow;
import stepper.step.api.DataNecessity;

public class DTOInputExecution {

    private String userString; //user string
    private String finalName;
    private DataNecessity necessity; //mandatory or optional
    private String originalName;
    private String type;
    private boolean isFile;

    DTOInputExecution (DataInFlow data)
    {
        this.userString = data.getUserString();
        this.finalName= data.getFinalName();
        this.necessity = data.getNecessity();
        this.originalName = data.getName();
        this.isFile = data.isFile();
        this.type= data.getDataType();
    }
    public String getOriginalName() {
        return originalName;
    }
    public String getUserString() {
        return userString;
    }
    public DataNecessity getNecessity() {
        return necessity;
    }
    public String getFinalName() {
        return finalName;
    }
    public String getType() {
        return type;
    }
    public boolean isFile() {
        return isFile;
    }
}
