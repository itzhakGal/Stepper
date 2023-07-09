package stepper.dataDefinition.impl.number;


import stepper.dataDefinition.api.AbstractDataDefinition;

public class DoubleDataDefinition extends AbstractDataDefinition {
    public DoubleDataDefinition() {
        super("Double", true, Double.class);
    }
}
