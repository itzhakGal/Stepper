package stepper.flow.definition.api;

import xmlReaderJavaFX.schema.generated.STContinuation;
import xmlReaderJavaFX.schema.generated.STContinuationMapping;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;
import java.util.List;

public class ContinuationImp implements Continuation{

    protected List<ContinuationMapping> continuationMapping;

    protected String targetFlow;

    public ContinuationImp(STContinuation continuation)
    {
        this.continuationMapping = new ArrayList<>();
        this.targetFlow = continuation.getTargetFlow();
        initialContinuationMapping(continuation);
    }
    @Override
    public void initialContinuationMapping(STContinuation continuation)
    {
        for(STContinuationMapping stContinuationMapping :continuation.getSTContinuationMapping())
        {
            this.continuationMapping.add(new ContinuationMappingImp(stContinuationMapping));
        }
    }
    @Override
    public List<ContinuationMapping> getContinuationMapping() {
        if (continuationMapping == null) {
            continuationMapping = new ArrayList<ContinuationMapping>();
        }
        return this.continuationMapping;
    }
    @Override
    public String getTargetFlow() {
        return targetFlow;
    }

}
