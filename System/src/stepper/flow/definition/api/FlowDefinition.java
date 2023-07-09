package stepper.flow.definition.api;

import stepper.exception.CustomMappingIsIncorrect;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface FlowDefinition extends Serializable {
    String getName();
    String getDescription();
    List<StepUsageDeclaration> getFlowSteps();
    List<String> getFlowFormalOutputs();

    void validateFlowStructure() ;

    void validateMandatoryInputWithUserFriendly();
    void validateMandatoryInputDuplicateName();
    void validFormalOutput();
    void validateDuplicateOutput();
    void validFlowAliasingData();
     boolean isReadonly();
    void initializationOfListsAccordingToXMLData(xmlReader.schema.generated.STFlow flow);
     void getDataAlias();
    void customMapping();
    String getOriginalName(String stepName, String data);
    void  CheckTheCorrectnessOfCustomMapping(CustomMapping customMapping, String originalNameSourceData, String originalNameTargetData);
     List<CustomMapping> getCustomMapping();
    Map<String, DataInFlow> getFreeInputs();
     List<FlowLevelAlias> getFlowLevelAlias();
    List<String> getFlowOutputs();
    List<StepUsageDeclaration> getSteps();
    StepUsageDeclaration getStepUsageDeclaration(String finalStepName);
    void createMapInputsData();
    void createMapOutputData();
    void extractUserInputs();
    DataInFlow getDataByFinalOutputName(String finalOutputName);
    String getStepByFinalOutputName(String finalOutputName);
    void getUserInputs() throws CustomMappingIsIncorrect;
    Map<String, Map<String, DataInFlow>> getStepInputs();
    Map<String, Map<String, DataInFlow>> getStepOutput();

    void initializationOfListsInitialInputAndContinuation(xmlReaderJavaFX.schema.generated.STFlow flow);

    List<Continuation> getContinuationsList();
    List<InitialInputValue> getInitialInputValuesList();
    Continuation getContinuationByTargetFlowName(String targetFlowName);

    DataInFlow getDataByFinalInputName(String finalOutputName);
}
