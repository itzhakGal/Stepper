package stepper.flow.definition.api;

import xmlReaderJavaFX.schema.generated.STContinuation;

import java.util.List;

public interface Continuation {
    String getTargetFlow();

    List<ContinuationMapping> getContinuationMapping();
    void initialContinuationMapping(STContinuation continuation);
}
