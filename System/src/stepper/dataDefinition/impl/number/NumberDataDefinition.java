package stepper.dataDefinition.impl.number;


import stepper.dataDefinition.api.AbstractDataDefinition;

public class NumberDataDefinition extends AbstractDataDefinition {
    public NumberDataDefinition() {
        super("Number", true, Integer.class);
    }
}
