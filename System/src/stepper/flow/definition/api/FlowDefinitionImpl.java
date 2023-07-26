package stepper.flow.definition.api;

import stepper.exception.*;
import stepper.step.api.DataDefinitionDeclaration;
import stepper.step.api.DataNecessity;
import xmlReaderJavaFX.schema.generated.STContinuation;
import xmlReaderJavaFX.schema.generated.STFlow;
import xmlReader.schema.generated.*;
import xmlReaderJavaFX.schema.generated.STInitialInputValue;

import java.util.*;

public class FlowDefinitionImpl implements FlowDefinition {
    private final String name;
    private final String description;
    private final List<String> flowOutputs;
    private final List<StepUsageDeclaration> steps;
    private final List<CustomMapping> customMapping;
    private final List<FlowLevelAlias> flowLevelAlias;
    private final Map<String, DataInFlow> freeInputs;
    private  final Map<String, Map<String, String>> dataAlias;
    private final Map<String, Map<String, DataInFlow>> stepInputs;
    private final Map<String, Map<String, DataInFlow>> stepOutput;
    private final List<String> stepsName;
    private final Map<String, String>  mapStepsName; // key=finalName, value= originalName
    private final List<InitialInputValue> initialInputValuesList;

    private final List<Continuation> continuationsList;

    public FlowDefinitionImpl(xmlReader.schema.generated.STFlow flow)  {
        this.name = flow.getName();
        this.description = flow.getSTFlowDescription();
        this.flowOutputs = new ArrayList<>();
        this.steps = new ArrayList<>();
        this.customMapping = new ArrayList<>();
        this.flowLevelAlias = new ArrayList<>();
        this.freeInputs = new HashMap<>();
        this.dataAlias = new HashMap<>();
        this.stepInputs = new HashMap<>();
        this.stepOutput = new HashMap<>();
        this.stepsName = new ArrayList<>();
        this.mapStepsName = new HashMap<>();

        this.initialInputValuesList = new ArrayList<>();
        this.continuationsList = new ArrayList<>();

        initializationOfListsAccordingToXMLData(flow);

        getDataAlias();
        getUserInputs();
    }

    public FlowDefinitionImpl(STFlow flow)  {
        this.name = flow.getName();
        this.description = flow.getSTFlowDescription();
        this.flowOutputs = new ArrayList<>();
        this.steps = new ArrayList<>();
        this.customMapping = new ArrayList<>();
        this.flowLevelAlias = new ArrayList<>();
        this.freeInputs = new HashMap<>();
        this.dataAlias = new HashMap<>();
        this.stepInputs = new HashMap<>();
        this.stepOutput = new HashMap<>();
        this.stepsName = new ArrayList<>();
        this.mapStepsName = new HashMap<>();

        this.initialInputValuesList = new ArrayList<>();
        this.continuationsList = new ArrayList<>();

        initializationOfListsInitialInputAndContinuation(flow);

        getDataAlias();
        getUserInputs();
    }
    @Override
    public void initializationOfListsAccordingToXMLData(xmlReader.schema.generated.STFlow flow) {
        if(flow.getSTFlowOutput() != null) {
            String[] words = flow.getSTFlowOutput().split(",");
            for (String word : words)
                this.flowOutputs.add(word);
        }

        if(flow.getSTCustomMappings() != null)
        {
            for (STCustomMapping customMapping : flow.getSTCustomMappings().getSTCustomMapping())
                this.customMapping.add(new CustomMappingImpl(customMapping));
        }
        if(flow.getSTFlowLevelAliasing() != null) {
            for (STFlowLevelAlias flowLevelAlias : flow.getSTFlowLevelAliasing().getSTFlowLevelAlias())
                this.flowLevelAlias.add(new FlowLevelAliasImpl(flowLevelAlias));
        }

        for (STStepInFlow stepInFlow : flow.getSTStepsInFlow().getSTStepInFlow()) {
            this.steps.add(new StepUsageDeclarationImpl(stepInFlow));
            if(stepInFlow.getAlias()!= null) {
                this.stepsName.add(stepInFlow.getAlias());
                this.mapStepsName.put( stepInFlow.getAlias(), stepInFlow.getName());
            }
            else {
                this.stepsName.add(stepInFlow.getName());
                this.mapStepsName.put(stepInFlow.getName(),stepInFlow.getName());
            }
        }
    }
    @Override
    public void initializationOfListsInitialInputAndContinuation(STFlow flow)
    {
        if(flow.getSTFlowOutput() != null) {
            String[] words = flow.getSTFlowOutput().split(",");
            for (String word : words)
                this.flowOutputs.add(word);
        }

        if(flow.getSTCustomMappings() != null)
        {
            for (xmlReaderJavaFX.schema.generated.STCustomMapping customMapping : flow.getSTCustomMappings().getSTCustomMapping())
                this.customMapping.add(new CustomMappingImpl(customMapping));
        }
        if(flow.getSTFlowLevelAliasing() != null) {
            for (xmlReaderJavaFX.schema.generated.STFlowLevelAlias flowLevelAlias : flow.getSTFlowLevelAliasing().getSTFlowLevelAlias())
                this.flowLevelAlias.add(new FlowLevelAliasImpl(flowLevelAlias));
        }

        for (xmlReaderJavaFX.schema.generated.STStepInFlow stepInFlow : flow.getSTStepsInFlow().getSTStepInFlow()) {
            this.steps.add(new StepUsageDeclarationImpl(stepInFlow));
            if(stepInFlow.getAlias()!= null) {
                this.stepsName.add(stepInFlow.getAlias());
                this.mapStepsName.put( stepInFlow.getAlias(), stepInFlow.getName());
            }
            else {
                this.stepsName.add(stepInFlow.getName());
                this.mapStepsName.put(stepInFlow.getName(),stepInFlow.getName());
            }
        }

        if(flow.getSTInitialInputValues() != null)
        {
            for (STInitialInputValue initialInputValue : flow.getSTInitialInputValues().getSTInitialInputValue())
                this.initialInputValuesList.add(new InitialInputValueImp(initialInputValue));
        }

        if(flow.getSTContinuations() != null)
        {
            for (STContinuation continuation : flow.getSTContinuations().getSTContinuation())
                this.continuationsList.add(new ContinuationImp(continuation));
        }

    }
    @Override
    public void getDataAlias() {
        for (FlowLevelAlias flowLevelAlias : this.getFlowLevelAlias()) {
            if (!dataAlias.containsKey(flowLevelAlias.getStepName())) {
                dataAlias.put(flowLevelAlias.getStepName(), new HashMap<String, String>() {{
                    put(flowLevelAlias.getSourceDataName(), flowLevelAlias.getAlias());
                }});
            }
            else {
                dataAlias.get(flowLevelAlias.getStepName()).
                        put(flowLevelAlias.getSourceDataName(), flowLevelAlias.getAlias());
            }
        }
    }
    public void addFlowOutput(String outputName) {
        flowOutputs.add(outputName);
    }
   @Override
    public void validateFlowStructure(){

        validateDuplicateOutput();

        validateMandatoryInputWithUserFriendly();

        validFlowAliasingData();

        validFormalOutput();

        validateMandatoryInputDuplicateName();

        validInitialInputData();
   }

    public void validInitialInputData() {
        for(InitialInputValue initialInputValue :this.initialInputValuesList)
            findInitialInputDataInStepsFlow(initialInputValue);
    }
    public void findInitialInputDataInStepsFlow(InitialInputValue initialInputValue)
    {
        boolean flag = false;
        for (StepUsageDeclaration oneStep : this.steps) {
            for (Map.Entry<String, DataInFlow> input : this.stepInputs.get(oneStep.getFinalStepName()).entrySet()) {
                if (input.getValue().getFinalName().equals(initialInputValue.getInputName()))
                    flag = true;
            }
        }
        if(!flag)
            throw new InvalidDataInInitialInputs(this.name, initialInputValue.getInputName());
    }

    @Override
    public void validateMandatoryInputDuplicateName() {
        Map<String, String> seenNamesAndTypes = new HashMap<>();

        for (Map<String, DataInFlow> innerMap : stepInputs.values()) {
            for (DataInFlow data : innerMap.values()) {
                String inputName = data.getFinalName();
                String type = data.getDataDefinition().getType().getName();
                String seenType = seenNamesAndTypes.get(inputName);
                if (seenType != null && !seenType.equals(type)) {
                    throw new MandatoryInputDuplicateNameException(this.name, inputName, seenType, type);
                } else {
                    seenNamesAndTypes.put(inputName, type);
                }
            }
        }
    }
    @Override
    public List<Continuation> getContinuationsList() {
        return continuationsList;
    }
    @Override
    public List<InitialInputValue> getInitialInputValuesList() {
        return initialInputValuesList;
    }




    public void validFormalOutput()
    {
        List <String> outputsAlias = new ArrayList<>();
        String outputAlias;
        for (StepUsageDeclaration step : this.steps) {
            for (Map.Entry<String, DataInFlow> entry : this.stepOutput.get(step.getFinalStepName()).entrySet()) {
                outputAlias = entry.getValue().getFinalName();
                outputsAlias.add(outputAlias);
            }
        }

        for (String formalOutput: flowOutputs)
        {
            if (!outputsAlias.contains(formalOutput)  && !formalOutput.isEmpty())
                throw new FormalOutputException(this.name, formalOutput);
        }
    }
    @Override
    public void validateDuplicateOutput() {
        Map<String, Integer> myMap = new HashMap<>();

        for (StepUsageDeclaration oneStep : steps) {
            for (Map.Entry<String, DataDefinitionDeclaration> output : oneStep.getStepDefinition().getOutputs().entrySet()) {
                String name = output.getKey();
                if (myMap.containsKey(name)) {
                    int count = myMap.get(name);
                    myMap.put(name, count + 1);
                } else {
                    myMap.put(name, 1);
                }
            }
            for (FlowLevelAlias levelAlias : flowLevelAlias) {
                if (myMap.containsKey(levelAlias.getSourceDataName())) {
                    int count = myMap.get(levelAlias.getSourceDataName());
                    myMap.put(levelAlias.getSourceDataName(), count - 1);
                }
            }
        }

        String outputName = findNameWithMultipleOccurrences(myMap);
        if(outputName != null)
            throw new DuplicateOutputNameException(this.name, outputName);
    }
    public static String findNameWithMultipleOccurrences(Map<String, Integer> myMap) {
        for (Map.Entry<String, Integer> entry : myMap.entrySet()) {
            if (entry.getValue() > 1) {
                return entry.getKey();
            }
        }
        return null;
    }
    @Override
    public void validateMandatoryInputWithUserFriendly() {
        for (DataInFlow data : this.freeInputs.values()) {
            if (data.getNecessity() == DataNecessity.MANDATORY && !data.getDataDefinition().isUserFriendly()) {
                throw new UserInputNotFriendlyException(this.name, data.getFinalName());
            }
        }
    }
    @Override
    public void validFlowAliasingData()
    {
        String stepName;
        for(FlowLevelAlias flowAlias :flowLevelAlias)
        {
            stepName= flowAlias.getStepName();

            if (!(stepsName.contains(stepName))) {
                throw new FlowLevelAliasException(this.name, stepName);
            }
            if((stepInputs.get(stepName).get(flowAlias.getSourceDataName()) ==null) && (stepOutput.get(stepName).get(flowAlias.getSourceDataName()) == null))
                throw new FlowLevelAliasException(this.name, flowAlias.getSourceDataName());
        }
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public List<StepUsageDeclaration> getFlowSteps() {
        return steps;
    }
    @Override
    public List<String> getFlowFormalOutputs() {
        return flowOutputs;
    }
    @Override
    public List<CustomMapping> getCustomMapping() {
        return customMapping;
    }
    @Override
    public List<FlowLevelAlias> getFlowLevelAlias() {
        return flowLevelAlias;
    }
    @Override
    public boolean isReadonly() {

        boolean readonly = true;
        for (StepUsageDeclaration step : steps) {
            if (!step.getStepDefinition().isReadonly()) {
                readonly = false;
                break;
            }
        }
        return readonly;
    }
    @Override
    public List<String> getFlowOutputs() {
        return flowOutputs;
    }
    @Override
    public List<StepUsageDeclaration> getSteps() {
        return steps;
    }
    @Override
    public Map<String, DataInFlow> getFreeInputs() {
        return freeInputs;
    }
    @Override
    public StepUsageDeclaration getStepUsageDeclaration(String finalStepName)
    {
        for(StepUsageDeclaration step: steps)
            if(step.getFinalStepName().equals(finalStepName))
                return step;

        return null;
    }
    @Override
    public Map<String, Map<String, DataInFlow>> getStepInputs() {
        return stepInputs;
    }
    @Override
    public Map<String, Map<String, DataInFlow>> getStepOutput() {
        return stepOutput;
    }
    @Override
    public void getUserInputs() throws CustomMappingIsIncorrect {
        createMapInputsData();
        createMapOutputData();

        customMapping();

        extractUserInputs();
    }
    @Override
    public void customMapping()  {
        for (CustomMapping customMapping : this.customMapping)
        {
            Map<String, DataInFlow> map = stepOutput.get(customMapping.getSourceStep());

            String originalNameSourceData = getOriginalName(customMapping.getSourceStep(), customMapping.getSourceData());
            String originalNameTargetData = getOriginalName(customMapping.getTargetStep(), customMapping.getTargetData());

            CheckTheCorrectnessOfCustomMapping(customMapping, originalNameSourceData, originalNameTargetData);
           // DataNecessity dataNecessityTargetData = stepInputs.get(customMapping.getTargetStep()).get(customMapping.getTargetData()).getNecessity();


            //this.stepInputs.get(customMapping.getTargetStep()).put(customMapping.getTargetData(), map.get(originalNameSourceData));   זה האחרון
            this.stepInputs.get(customMapping.getTargetStep()).put(originalNameTargetData, map.get(originalNameSourceData));


            //  this.stepInputs.get(customMapping.getTargetStep()).get(customMapping.getTargetData()).setNecessity(dataNecessityTargetData);
        }
    }
    @Override
    public String getOriginalName(String stepName, String data) {
        String originalNameData = "";
        Map<String, String> mapNames = dataAlias.get(stepName);
        if (mapNames!=null) {
            for (Map.Entry<String, String> entry : mapNames.entrySet()) {
                if (entry.getValue().equals(data)) {
                    originalNameData = entry.getKey();
                    break;
                }
            }
        }
        if(originalNameData.equals(""))
              originalNameData = data;

        return originalNameData;
    }
    @Override
    public void  CheckTheCorrectnessOfCustomMapping(CustomMapping customMapping, String originalNameSourceData, String originalNameTargetData)
    {
        StepUsageDeclaration targetStep=null , sourceStep= null;

        if (!(stepsName.contains(customMapping.getSourceStep()))) {
            throw new CustomMappingIsIncorrect(this.name, customMapping.getSourceStep());
        }
        if (!(stepsName.contains(customMapping.getTargetStep()))) {
            throw new CustomMappingIsIncorrect(this.name, customMapping.getTargetStep());
        }

        if((stepInputs.get(customMapping.getSourceStep()).get(originalNameSourceData) ==null) && (stepOutput.get(customMapping.getSourceStep()).get(originalNameSourceData) == null))
            throw new CustomMappingIsIncorrect(this.name, customMapping.getSourceData());

        if((stepInputs.get(customMapping.getTargetStep()).get(originalNameTargetData) ==null) && (stepOutput.get(customMapping.getTargetStep()).get(originalNameTargetData) == null))
            throw new CustomMappingIsIncorrect(this.name, customMapping.getTargetData());

        for (StepUsageDeclaration step :steps )
      {
          if(step.getFinalStepName().equals(customMapping.getSourceStep()))
              sourceStep=step;
          if(step.getFinalStepName().equals(customMapping.getTargetStep()))
              targetStep=step;
      }

      int index1 =  stepsName.indexOf(customMapping.getSourceStep());
      int index2=  stepsName.indexOf(customMapping.getTargetStep());
      if (index1 > index2)
          throw new CustomMappingIsIncorrectOrderException(this.name, customMapping.getSourceStep(),customMapping.getTargetStep());

      DataDefinitionDeclaration sourceData=  sourceStep.getStepDefinition().getOutputs().get(originalNameSourceData);
      DataDefinitionDeclaration targetData=  targetStep.getStepDefinition().getInputs().get(originalNameTargetData);

      if(sourceData.dataDefinition().getType() != targetData.dataDefinition().getType())
          throw new CustomMappingException(this.name, customMapping.getSourceData(),customMapping.getTargetData());
    }
   @Override
    public DataInFlow getDataByFinalOutputName(String finalOutputName) {
        for (StepUsageDeclaration step : this.steps) {
            for (Map.Entry<String, DataInFlow> entry : this.stepOutput.get(step.getFinalStepName()).entrySet()) {
                 if(entry.getValue().getFinalName().equals(finalOutputName))
                     return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public DataInFlow getDataByFinalInputName(String finalOutputName) {
        for (StepUsageDeclaration step : this.steps) {
            for (Map.Entry<String, DataInFlow> entry : this.stepInputs.get(step.getFinalStepName()).entrySet()) {
                if(entry.getValue().getFinalName().equals(finalOutputName))
                    return entry.getValue();
            }
        }
        return null;
    }
    @Override
    public String getStepByFinalOutputName(String finalOutputName) {
        for (StepUsageDeclaration step : this.steps) {
            for (Map.Entry<String, DataInFlow> entry : this.stepOutput.get(step.getFinalStepName()).entrySet()) {
                if(entry.getKey().equals(finalOutputName))
                    return step.getFinalStepName();
            }
        }
        return null;
    }
    @Override
    public void createMapInputsData()
    {
        Map<String, DataInFlow> inputs;
        Map<String, String> stepInputAlias = new HashMap<>();
        String alias;

        for (StepUsageDeclaration step : this.steps) {
            inputs = new HashMap<>();
            for (Map.Entry<String, DataDefinitionDeclaration> entry : step.getStepDefinition().getInputs().entrySet()) {
                stepInputAlias = this.dataAlias.get(step.getFinalStepName());
                if ((stepInputAlias != null) && (stepInputAlias.get(entry.getKey())!=null))
                {
                    alias = stepInputAlias.get(entry.getKey());  // check
                    DataInFlow dataInFlow = new DataInFlowImp(alias,entry.getValue(), DataKind.INPUT);
                    dataInFlow.getRelatedSteps().add(step.getFinalStepName());
                    inputs.put(entry.getKey(),dataInFlow);

                    step.getDataUsageDeclarationByOriginalName(entry.getKey()).setFinalName(alias);
                }
            }
            for (Map.Entry<String, DataDefinitionDeclaration> entry : step.getStepDefinition().getInputs().entrySet()) {
                if (!inputs.containsKey(entry.getKey()))
                {
                    String inputName = entry.getKey();
                    DataInFlow dataInFlow = new DataInFlowImp(inputName,entry.getValue(), DataKind.INPUT);
                    dataInFlow.getRelatedSteps().add(step.getFinalStepName());
                    inputs.put(inputName, dataInFlow);
                }
            }
            this.stepInputs.put(step.getFinalStepName(), inputs);
        }
    }
    @Override
    public void createMapOutputData() {
        Map<String, DataInFlow> outputs;
        Map<String, String> stepOutputAlias;
        String alias;

        for (StepUsageDeclaration step : this.steps) {
            outputs = new HashMap<>();
            for (Map.Entry<String, DataDefinitionDeclaration> entry : step.getStepDefinition().getOutputs().entrySet()) {
                stepOutputAlias = this.dataAlias.get(step.getFinalStepName());
                if (stepOutputAlias != null && (stepOutputAlias.get(entry.getKey())!=null)) {
                    alias = stepOutputAlias.get(entry.getKey());  // check
                    DataInFlow dataInFlow = new DataInFlowImp(alias,entry.getValue(), DataKind.OUTPUT);


                    dataInFlow.getRelatedSteps().add(step.getFinalStepName());
                    outputs.put(entry.getKey(),dataInFlow);

                    step.getDataUsageDeclarationByOriginalName(entry.getKey()).setFinalName(alias);
                }
            }
            for (Map.Entry<String, DataDefinitionDeclaration> entry : step.getStepDefinition().getOutputs().entrySet()) {
                if (!outputs.containsKey(entry.getKey())) {
                    String outputName = entry.getKey();
                    DataInFlow dataInFlow = new DataInFlowImp(outputName,entry.getValue(), DataKind.OUTPUT);

                   // dataInFlow.getNecessity();

                    dataInFlow.getRelatedSteps().add(step.getFinalStepName());
                    outputs.put(outputName, dataInFlow);
                }
            }
            this.stepOutput.put(step.getFinalStepName(), outputs);
        }
    }

   @Override
    public void extractUserInputs()
    {
        Map<String, String> foundOutputs = new HashMap<>();
        String inputAlias, inputType, outputAlias, outputType;
        DataDefinitionDeclaration inputDefinition, outputDefinition;

        for (StepUsageDeclaration step : this.steps) {
            for (Map.Entry<String, DataInFlow> entry : this.stepInputs.get(step.getFinalStepName()).entrySet()) {
                inputAlias = entry.getValue().getFinalName();
                inputDefinition = step.getStepDefinition().getInputs().get(entry.getKey());
                inputType = inputDefinition.dataDefinition().getType().getName();

                if (!foundOutputs.containsKey(inputAlias) ||
                        (foundOutputs.containsKey(inputAlias) && !foundOutputs.get(inputAlias).equals(inputType)))
                {
                    DataInFlow data =  new DataInFlowImp(inputAlias , inputDefinition, DataKind.INPUT);
                    data.getRelatedSteps().add(step.getFinalStepName());
                     if(freeInputs.containsKey(inputAlias))
                     {
                         if (freeInputs.get(inputAlias).getDataType().equals(data.getDataType())) {
                             if (freeInputs.get(inputAlias).getNecessity() == DataNecessity.OPTIONAL)
                                 freeInputs.put(inputAlias, data);
                         }
                         else
                             throw new NameAndTypeDoNotMatchException(this.name, inputAlias, freeInputs.get(inputAlias).getDataType());
                     }
                     else {
                        freeInputs.put(inputAlias, data);
                    }

                }
            }
            for (Map.Entry<String, DataInFlow> entry : this.stepOutput.get(step.getFinalStepName()).entrySet()) {
                outputAlias = entry.getValue().getFinalName();
                outputDefinition = step.getStepDefinition().getOutputs().get(entry.getKey());
                outputType = outputDefinition.dataDefinition().getType().getName();

                foundOutputs.put(outputAlias, outputType);
            }
        }
    }
    @Override
    public Continuation getContinuationByTargetFlowName(String targetFlowName)
    {
        for(Continuation continuation : continuationsList)
        {
            if(continuation.getTargetFlow().equals(targetFlowName))
                return continuation;
        }
        return null;
    }

}

