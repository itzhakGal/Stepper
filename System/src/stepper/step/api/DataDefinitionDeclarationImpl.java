package stepper.step.api;

import stepper.dataDefinition.api.DataDefinition;

public class DataDefinitionDeclarationImpl implements DataDefinitionDeclaration {
    private final String name;
    private final DataNecessity necessity;
    private final String userString;
    private final DataDefinition dataDefinition;
    private boolean isFile;

    private String enumeratorType;
    public DataDefinitionDeclarationImpl(String name, DataNecessity necessity, String userString, DataDefinition dataDefinition, boolean isFile,String EnumeratorType) {
        this.name = name;
        this.necessity = necessity;
        this.userString = userString;
        this.dataDefinition = dataDefinition;
        this.isFile = isFile;
        this.enumeratorType = EnumeratorType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DataNecessity necessity() {
        return necessity;
    }

    @Override
    public String userString() {
        return userString;
    }

    @Override
    public DataDefinition dataDefinition() {
        return dataDefinition;
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
