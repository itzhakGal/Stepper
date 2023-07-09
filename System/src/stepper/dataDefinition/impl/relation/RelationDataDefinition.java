package stepper.dataDefinition.impl.relation;

import stepper.dataDefinition.api.AbstractDataDefinition;

public class RelationDataDefinition extends AbstractDataDefinition {
    public RelationDataDefinition() {
        super("Relation", false, RelationData.class);
    }
}
