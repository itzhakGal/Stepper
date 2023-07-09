package stepper.flow.definition.api;

import xmlReader.schema.generated.STCustomMapping;

public class CustomMappingImpl implements CustomMapping{

    protected String targetStep;
    protected String targetData;
    protected String sourceStep;
    protected String sourceData;

    public CustomMappingImpl(STCustomMapping customMapping) {
        this.sourceData = customMapping.getSourceData();
        this.sourceStep = customMapping.getSourceStep();
        this.targetData = customMapping.getTargetData();
        this.targetStep = customMapping.getTargetStep();
    }

    public CustomMappingImpl(xmlReaderJavaFX.schema.generated.STCustomMapping customMapping) {
        this.sourceData = customMapping.getSourceData();
        this.sourceStep = customMapping.getSourceStep();
        this.targetData = customMapping.getTargetData();
        this.targetStep = customMapping.getTargetStep();
    }

    @Override
    public String getSourceData() {
        return sourceData;
    }

    @Override
    public String getSourceStep() {
        return sourceStep;
    }

    @Override
    public String getTargetData() {
        return targetData;
    }

    @Override
    public String getTargetStep() {
        return targetStep;
    }

}
