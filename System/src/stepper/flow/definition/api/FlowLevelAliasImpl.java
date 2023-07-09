package stepper.flow.definition.api;

import xmlReader.schema.generated.STFlowLevelAlias;

public class FlowLevelAliasImpl implements FlowLevelAlias{

    protected String stepName;
    protected String sourceDataName;
    protected String alias;

    public FlowLevelAliasImpl(STFlowLevelAlias flowLevelAlias) {
        this.alias = flowLevelAlias.getAlias();
        this.stepName = flowLevelAlias.getStep();
        this.sourceDataName = flowLevelAlias.getSourceDataName();
    }

    public FlowLevelAliasImpl(xmlReaderJavaFX.schema.generated.STFlowLevelAlias flowLevelAlias) {
        this.alias = flowLevelAlias.getAlias();
        this.stepName = flowLevelAlias.getStep();
        this.sourceDataName = flowLevelAlias.getSourceDataName();
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getSourceDataName() {
        return sourceDataName;
    }

    @Override
    public String getStepName() {
        return stepName;
    }
}
