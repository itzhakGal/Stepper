package stepper.flow.definition.api;

import xmlReaderJavaFX.schema.generated.STContinuationMapping;

import javax.xml.bind.annotation.XmlAttribute;

public class ContinuationMappingImp implements ContinuationMapping{

    protected String targetData;
    protected String sourceData;

    public ContinuationMappingImp(STContinuationMapping continuationMapping)
    {
        this.sourceData = continuationMapping.getSourceData();
        this.targetData = continuationMapping.getTargetData();
    }
    @Override
    public String getTargetData() {
        return targetData;
    }
    @Override
    public String getSourceData() {
        return sourceData;
    }
}
