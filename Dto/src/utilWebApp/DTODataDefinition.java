package utilWebApp;


import stepper.dataDefinition.api.DataDefinition;

public class DTODataDefinition
{
    private final String name;
    private final boolean userFriendly;
    private final String type;
    public DTODataDefinition(DataDefinition dataDefinition) {
        this.name = dataDefinition.getName();
        this.userFriendly = dataDefinition.isUserFriendly();
        this.type = dataDefinition.getType().getTypeName();
    }

    public String getName() {
        return name;
    }


    public boolean isUserFriendly() {
        return userFriendly;
    }


    public String getType() {
        return type;
    }
}
