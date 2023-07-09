package stepper.dataDefinition.impl;


import stepper.dataDefinition.impl.Enumerator.EnumeratorDataDefinition;
import stepper.dataDefinition.impl.file.FileDataDefinition;
import stepper.dataDefinition.impl.json.JsonDataDefinition;
import stepper.dataDefinition.impl.list.ListDataDefinition;
import stepper.dataDefinition.impl.number.DoubleDataDefinition;
import stepper.dataDefinition.impl.number.NumberDataDefinition;
import stepper.dataDefinition.impl.string.StringDataDefinition;
import stepper.dataDefinition.api.DataDefinition;
import stepper.dataDefinition.impl.fileList.FileListDataDefinition;
import stepper.dataDefinition.impl.mapping.MappingDataDefinition;
import stepper.dataDefinition.impl.relation.RelationDataDefinition;

public enum DataDefinitionRegistry implements DataDefinition {
    STRING(new StringDataDefinition(false)),
    DOUBLE(new DoubleDataDefinition()),
    NUMBER(new NumberDataDefinition()),
    RELATION(new RelationDataDefinition()),
    LIST(new ListDataDefinition()),
    FILE(new FileDataDefinition()),
    FILE_LIST(new FileListDataDefinition()),
    MAPPING(new MappingDataDefinition()),
    ENUMERATOR(new EnumeratorDataDefinition()),
    JSON(new JsonDataDefinition());

    DataDefinitionRegistry(DataDefinition dataDefinition) {
        this.dataDefinition = dataDefinition;
    }

    private final DataDefinition dataDefinition;

    @Override
    public String getName() {
        return dataDefinition.getName();
    }

    @Override
    public boolean isUserFriendly() {
        return dataDefinition.isUserFriendly();
    }

    @Override
    public Class<?> getType() {
        return dataDefinition.getType();
    }
}
