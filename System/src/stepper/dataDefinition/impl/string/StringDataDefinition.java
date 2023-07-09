package stepper.dataDefinition.impl.string;


import stepper.dataDefinition.api.AbstractDataDefinition;

public class StringDataDefinition extends AbstractDataDefinition {
    public StringDataDefinition(Boolean isFile) {
        super("String", true, String.class);
    }

}
