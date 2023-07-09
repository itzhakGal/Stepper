package stepper.dataDefinition.impl.json;

import stepper.dataDefinition.api.AbstractDataDefinition;

public class JsonDataDefinition extends AbstractDataDefinition {
    public JsonDataDefinition() {
        super("Json", true, JsonData.class);
    }

}