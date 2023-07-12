package stepper.systemEngine;

import stepper.dataDefinition.impl.Enumerator.EnumeratorData;
import stepper.exception.*;
import stepper.flow.definition.api.*;
import stepper.flow.execution.FlowExecution;
import stepper.flow.execution.FlowExecutionImpl;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.flow.execution.context.ExecutionContextImpl;
import stepper.flow.execution.runner.FLowExecutor;
import stepper.flow.execution.runner.FlowExecutorConsoleUI;
import stepper.flows.definition.FlowsDefinition;
import stepper.flows.definition.FlowsDefinitionImpl;
import stepper.flows.execution.FlowsExecution;
import stepper.flows.execution.FlowsExecutionImp;
import stepper.role.RolesManager;
import stepper.statistics.StatisticsDataImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepsNamesImpl;
import stepper.users.UserManager;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utils.*;
import utilsDesktopApp.DTOFlowDetails;
import utilsDesktopApp.DTOListContinuationFlowName;
import utilsDesktopApp.DTOListFlowsDetails;
import xmlReader.schema.SchemaBasedJAXBMain;
import xmlReader.schema.generated.STFlow;
import xmlReader.schema.generated.STStepInFlow;
import xmlReader.schema.generated.STStepper;
import xmlReaderJavaFX.schema.SchemaBasedJAXBMainJavaFX;
import xmlReaderJavaFX.schema.generated.STContinuation;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemEngine implements SystemEngineInterface {

    private static SystemEngine instance;
    public FlowsDefinition currentFlowsDefinition;
    public FlowsExecution listFlowsExecution;
    public StatisticsDataImpl statisticsData;
    public FlowExecutionImpl optionalFlowExecution;
    private ExecutorService threadPoolExecutor;
    private Map<UUID, FlowExecution> executedFlowsMap;
    private final RolesManager roles;
    private final UserManager userManager;


    public SystemEngine ()
    {
        this.listFlowsExecution = new FlowsExecutionImp();
        this.currentFlowsDefinition = new FlowsDefinitionImpl();
        this.statisticsData = new StatisticsDataImpl();
        this.optionalFlowExecution = null;
        this.executedFlowsMap = new HashMap<>();
        this.userManager = new UserManager();
        this.roles = new RolesManager();
    }

    public static SystemEngine getInstance() {
        return instance;
    }

    @Override
    public DTOString readingSystemInformationFromFile(File file)  {

        DTOString validResult;
        FlowsDefinition optionalFlowsDefinition = new FlowsDefinitionImpl();

        SchemaBasedJAXBMain schema = new SchemaBasedJAXBMain();
        STStepper stepper = schema.createStepperFromXMLFile(file);
        checkIfTheStepNamesExist(stepper);
        checkDuplicateNames(stepper);
        transferringDataFromTheXMLClassesToOurClasses(stepper, optionalFlowsDefinition);

        checkValidXMLFile(stepper, optionalFlowsDefinition);
        this.currentFlowsDefinition =  optionalFlowsDefinition;

        String result = "The file is found to be correct and fully loaded for the user.";
        validResult = new DTOString(result, true);
        return validResult;
    }
    @Override
    public void checkValidXMLFile(STStepper stepper, FlowsDefinition  optionalFlowsDefinition)
    {
         for(FlowDefinition oneFlow : optionalFlowsDefinition.getListFlowsDefinition()) {
              oneFlow.validateFlowStructure();
         }
    }
    @Override
    public void checkIfTheStepNamesExist( STStepper stepper)  {
        StepsNamesImpl stepName = new StepsNamesImpl();

        for(STFlow flow : stepper.getSTFlows().getSTFlow()) {
            for (STStepInFlow step : flow.getSTStepsInFlow().getSTStepInFlow()) {
                if (!(stepName.getNames().contains(step.getName()))) {
                    throw new StepNotExistException(flow.getName(), step.getName());
                }
            }
        }
    }
    @Override
    public void checkDuplicateNames(STStepper stepper){
        List<String> namesList = new ArrayList<>();

        for(STFlow flow : stepper.getSTFlows().getSTFlow())
            namesList.add(flow.getName());

        Set<String> namesSet = new HashSet<String>(namesList);
        if (namesSet.size() < namesList.size())
            throw new DuplicateFlowNameException();
    }
    @Override
    public DTOFlowDefinition introducingFlowDefinition(int choice) {

        DTOFlowDefinition flowDefinition=  new DTOFlowDefinition(currentFlowsDefinition.getListFlowsDefinition().get(choice-1));
        return flowDefinition;
    }

    @Override
    public DTOEndOFlowExecution flowActivationAndExecution(int choice)
    {
        FlowExecutorConsoleUI fLowExecutor = new FlowExecutorConsoleUI();
        ExecutionContextInterface context = new ExecutionContextImpl(this.optionalFlowExecution);
        initialDataValuesFlowExecution(context);

        fLowExecutor.executeFlow(context, statisticsData); // לחזור לזהההה

        listFlowsExecution.getFlowsExecutionList().add(this.optionalFlowExecution);
        DTOEndOFlowExecution endOFlowExecution =  new DTOEndOFlowExecution(this.optionalFlowExecution);
        return endOFlowExecution;
    }
    @Override
    public void initialDataValuesFlowExecution( ExecutionContextInterface context)
    {
        Set<Map.Entry<String, DataInFlowExecution>> entries = optionalFlowExecution.getFreeInputsExist().entrySet();
        for (Map.Entry<String, DataInFlowExecution> entry : entries) {
            if(entry.getValue().getItem() != null)
                context.storeDataValue(entry.getValue().getDataDefinitionInFlow().getName(), entry.getValue());
        }
    }
    @Override
    public DTOFullDetailsPastRun displayFullDetailsOfPastActivation(int choice)
    {
        int length = listFlowsExecution.getFlowsExecutionList().size();
        DTOFullDetailsPastRun fullDetailsPastRun= new DTOFullDetailsPastRun(listFlowsExecution.getFlowsExecutionList().get(length-choice));
        return fullDetailsPastRun;
    }
    @Override
    public DTOStatistics readStatistics(){
        DTOStatistics statistics= new DTOStatistics(statisticsData.getStatisticsStep(), statisticsData.getStatisticsFlow());
        return statistics;
    }
    @Override
    public void logOffFromSystem(){

    }
    @Override
    public DTOFlowsNames readFlowName()
    {
        DTOFlowsNames flowNames= new DTOFlowsNames();
        for(FlowDefinition flowName : currentFlowsDefinition.getListFlowsDefinition()) {
            flowNames.getFlowNames().add(flowName.getName());
        }
        return flowNames;
    }
    @Override
    public DTOListFlowsDetails readFlowsDetails()
    {
        DTOListFlowsDetails listFlowsDetails= new DTOListFlowsDetails();
        for(FlowDefinition flowDefinition : currentFlowsDefinition.getListFlowsDefinition()) {
            listFlowsDetails.getDtoFlowDetailsList().add(new DTOFlowDetails(flowDefinition));
        }
        return listFlowsDetails;
    }
    @Override
    public DTOFlowExecution readInputs(int choice)
    {
        DTOFlowExecution flowExecution=  new DTOFlowExecution(currentFlowsDefinition.getListFlowsDefinition().get(choice-1));
        return flowExecution;
    }
    @Override
    public DTOFlowsPastInformation readFlowsPast() {

        DTOFlowsPastInformation flowsPastInformation = new DTOFlowsPastInformation(listFlowsExecution);
        return flowsPastInformation;
    }
    @Override
    public void transferringDataFromTheXMLClassesToOurClasses(STStepper stepperData,  FlowsDefinition  optionalFlowsDefinition)  {
        for (STFlow flow : stepperData.getSTFlows().getSTFlow()) {
            optionalFlowsDefinition.getListFlowsDefinition().add(new FlowDefinitionImpl(flow));
        }
    }

    @Override
    public void initialOptionalFlowExecution(int selectionFlow) {
       this.optionalFlowExecution = new FlowExecutionImpl(currentFlowsDefinition.getListFlowsDefinition().get(selectionFlow-1));
    }

    @Override
    public DTOMandatoryInputs checkMandatoryExist()
    {
        DTOMandatoryInputs dtoMandatoryInputs;
        for (Map.Entry<String, DataInFlowExecution> entry : optionalFlowExecution.getFreeInputsExist().entrySet()) {
           if (entry.getValue().getItem() == null && entry.getValue().getDataDefinitionInFlow().getNecessity()== DataNecessity.MANDATORY ) {
               dtoMandatoryInputs = new DTOMandatoryInputs(false);
               return dtoMandatoryInputs;
           }
        }
        dtoMandatoryInputs = new DTOMandatoryInputs(true);
        return dtoMandatoryInputs;
    }
    @Override
    public void updateMandatoryInput(String selectionInput,Object value)
    {
        optionalFlowExecution.getFreeInputsExist().get(selectionInput).setItem(value);
    }

    @Override
    public void saveToFile(String path) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(path)))){
            out.writeObject(currentFlowsDefinition);
            out.writeObject(listFlowsExecution);
            out.writeObject(statisticsData);
        }
    }
    @Override
    public void loadFromFile(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(path)))){
            currentFlowsDefinition = (FlowsDefinition)in.readObject();
            listFlowsExecution = (FlowsExecution)in.readObject();
            statisticsData = (StatisticsDataImpl)in.readObject();
        }

    }

    @Override
    public DTOFlowDefinition introducingFlowDefinitionJavaFX(String flowName) {

        DTOFlowDefinition dtoFlowDefinition = null;
        for(FlowDefinition flowDefinition: currentFlowsDefinition.getListFlowsDefinition())
        {
            if(flowDefinition.getName().equals(flowName))
                dtoFlowDefinition=  new DTOFlowDefinition(flowDefinition);
        }

        return dtoFlowDefinition;
    }

    @Override
    public DTOString readingSystemInformationFromFileJavaFX(File file)  {

        clearData();
        DTOString validResult;
        FlowsDefinition optionalFlowsDefinition = new FlowsDefinitionImpl();

        SchemaBasedJAXBMainJavaFX schema = new SchemaBasedJAXBMainJavaFX();
        xmlReaderJavaFX.schema.generated.STStepper stepper = schema.createStepperFromXMLFileForJavaFX(file);
        checkIfTheStepNamesExistJavaFX(stepper);
        checkDuplicateNamesJavaFX(stepper);
        validContinuationFlowNameExist(stepper);

        transferringDataFromTheXMLClassesToOurClassesJavaFX(stepper, optionalFlowsDefinition);

        checkValidXMLFileJavaFX(stepper, optionalFlowsDefinition);
        validContinuationCorrectData(optionalFlowsDefinition);
        this.currentFlowsDefinition =  optionalFlowsDefinition;
        this.threadPoolExecutor = Executors.newFixedThreadPool(this.currentFlowsDefinition.getThreadPoolSize());

        String result = "The file is found to be correct and fully loaded for the user.";
        validResult = new DTOString(result, true);

        return validResult;
    }

    private void clearData() {
        executedFlowsMap.clear();
        listFlowsExecution.getFlowsExecutionList().clear();
        currentFlowsDefinition.getListFlowsDefinition().clear();
    }

    @Override
    public void transferringDataFromTheXMLClassesToOurClassesJavaFX(xmlReaderJavaFX.schema.generated.STStepper stepperData, FlowsDefinition  optionalFlowsDefinition)  {
        for (xmlReaderJavaFX.schema.generated.STFlow flow : stepperData.getSTFlows().getSTFlow()) {
            optionalFlowsDefinition.getListFlowsDefinition().add(new FlowDefinitionImpl(flow));
        }
        optionalFlowsDefinition.setThreadPoolSize(stepperData.getSTThreadPool());
    }
    @Override
    public void checkIfTheStepNamesExistJavaFX(xmlReaderJavaFX.schema.generated.STStepper stepper)  {
        StepsNamesImpl stepName = new StepsNamesImpl();

        for(xmlReaderJavaFX.schema.generated.STFlow flow : stepper.getSTFlows().getSTFlow()) {
            for (xmlReaderJavaFX.schema.generated.STStepInFlow step : flow.getSTStepsInFlow().getSTStepInFlow()) {
                if (!(stepName.getNames().contains(step.getName()))) {
                    throw new StepNotExistException(flow.getName(), step.getName());
                }
            }
        }
    }

    @Override
    public void validContinuationFlowNameExist(xmlReaderJavaFX.schema.generated.STStepper stepper)
    {
        List<String> namesList = new ArrayList<>();

        for(xmlReaderJavaFX.schema.generated.STFlow flow : stepper.getSTFlows().getSTFlow())
            namesList.add(flow.getName());

        for(xmlReaderJavaFX.schema.generated.STFlow flow : stepper.getSTFlows().getSTFlow())
        {
            if(flow.getSTContinuations() != null) {
                for (STContinuation stContinuation : flow.getSTContinuations().getSTContinuation()) {
                    if (!namesList.contains(stContinuation.getTargetFlow()))
                        throw new ContinuationFlowNameException(flow.getName(), stContinuation.getTargetFlow());
                }
            }
        }
    }


    @Override
    public void validContinuationFlowNameExistWeb(xmlReaderJavaFX.schema.generated.STStepper stepper)
    {
        List<String> namesList = new ArrayList<>();

        for(xmlReaderJavaFX.schema.generated.STFlow flow : stepper.getSTFlows().getSTFlow())
            namesList.add(flow.getName());

        for(xmlReaderJavaFX.schema.generated.STFlow flow : stepper.getSTFlows().getSTFlow())
        {
            if(flow.getSTContinuations() != null) {
                checkIfFlowExistsInlistNamesAndInCurrentFlowsExecution(namesList, flow);
            }
        }
    }

    public void checkIfFlowExistsInlistNamesAndInCurrentFlowsExecution(List<String> namesList, xmlReaderJavaFX.schema.generated.STFlow flow) {

        for (STContinuation stContinuation : flow.getSTContinuations().getSTContinuation()) {
            boolean isContinuationInCurr = false;
            boolean isContinuationInList = true;
            if (!namesList.contains(stContinuation.getTargetFlow()))
            {
                isContinuationInList = false;
            }
            for(FlowDefinition flowDefinition : currentFlowsDefinition.getListFlowsDefinition())
            {
                if(flowDefinition.getName().equals(stContinuation.getTargetFlow()))
                {
                    isContinuationInCurr = true;
                }
            }
            if(!(isContinuationInCurr || isContinuationInList))
                throw new ContinuationFlowNameException(flow.getName(), stContinuation.getTargetFlow());
        }
    }

    @Override
    public void checkDuplicateNamesJavaFX(xmlReaderJavaFX.schema.generated.STStepper stepper){
        List<String> namesList = new ArrayList<>();

        for(xmlReaderJavaFX.schema.generated.STFlow flow : stepper.getSTFlows().getSTFlow())
            namesList.add(flow.getName());

        Set<String> namesSet = new HashSet<String>(namesList);
        if (namesSet.size() < namesList.size())
            throw new DuplicateFlowNameException();
    }

    @Override
    public void checkValidXMLFileJavaFX(xmlReaderJavaFX.schema.generated.STStepper stepper, FlowsDefinition optionalFlowsDefinition)
    {
        for(FlowDefinition oneFlow : optionalFlowsDefinition.getListFlowsDefinition()) {
            oneFlow.validateFlowStructure();
        }
    }
    @Override
    public DTOFlowExecution readInputsJavaFX(String flowName)
    {
        FlowDefinition currFlowDefinition = getFlowDefinitionByFlowNameJavaFX(flowName);
        DTOFlowExecution flowExecution=  new DTOFlowExecution(currFlowDefinition);
        return flowExecution;
    }

    @Override
    public FlowDefinition getFlowDefinitionByFlowNameJavaFX(String flowName) {

        for(FlowDefinition flowDefinition: currentFlowsDefinition.getListFlowsDefinition())
        {
            if(flowDefinition.getName().equals(flowName))
                return flowDefinition;
        }

        return null;
    }

    @Override
    public UUID updateOptionalExecution(String flowName, boolean isContinuation) {
        if(!isContinuation) {
            FlowDefinition currFlowDefinition = getFlowDefinitionByFlowNameJavaFX(flowName);
            this.optionalFlowExecution = new FlowExecutionImpl(currFlowDefinition);
        }
        return optionalFlowExecution.getUniqueId();
    }
    @Override
    public void removeInitialFreeInputFromDTO(DTOFlowExecution dtoFlowExecution)
    {
        for(InitialInputValue inputValue :optionalFlowExecution.getFlowDefinition().getInitialInputValuesList())
        {
            removeFreeInputFromDTO(dtoFlowExecution.getInputsExecution(), inputValue.getInputName());
        }
    }
    @Override
    public void removeFreeInputFromDTO(List<DTOInputExecution> inputsExecution, String inputName) {
        for(DTOInputExecution inputExecution : inputsExecution)
        {
            if(inputExecution.getFinalName().equals(inputName)) {
                inputsExecution.remove(inputExecution);
                return;
            }
        }
    }

    @Override
    public boolean checkIfInputIsInitialValue(String inputName)
    {
        boolean flag = false;
        for(InitialInputValue inputValue :optionalFlowExecution.getFlowDefinition().getInitialInputValuesList())
        {
            if(inputValue.getInputName().equals(inputName))
                flag = true;
        }
        return flag;
    }

    @Override
    public DTOFullDetailsPastRun flowActivationAndExecutionJavaFX(String flowName)
    {
        ExecutionContextInterface context = new ExecutionContextImpl(this.optionalFlowExecution);
        initialDataValuesFlowExecution(context);

        executedFlowsMap.put(optionalFlowExecution.getUniqueId(), optionalFlowExecution);
        threadPoolExecutor.execute(new FLowExecutor(context, statisticsData));

        listFlowsExecution.getFlowsExecutionList().add(context.getFlowExecution());
        DTOFullDetailsPastRun fullDetails  =  new DTOFullDetailsPastRun(context.getFlowExecution());

        return fullDetails;
    }

    @Override
    public DTOFullDetailsPastRun displayFullDetailsOfPastActivationJavaFX(String flowName)
    {
        FlowExecutionImpl currFlowExecution = getFlowExecutionByFlowNameJavaFX(flowName);
        DTOFullDetailsPastRun fullDetailsPastRun= new DTOFullDetailsPastRun(currFlowExecution);
        return fullDetailsPastRun;
    }

    @Override
    public FlowExecutionImpl getFlowExecutionByFlowNameJavaFX(String flowName) {

        for(FlowExecutionImpl flowsExecution: listFlowsExecution.getFlowsExecutionList())
        {
            if(flowsExecution.getFlowDefinition().getName().equals(flowName))
                return flowsExecution;
        }

        return null;
    }
    @Override
    public void continuationToOtherFlow(UUID currFlowId, String sourceFlowName, String targetFlowName)
    {
        FlowExecution currFlowExecution = this.executedFlowsMap.get(currFlowId);
        Continuation currContinuation = currFlowExecution.getFlowDefinition().getContinuationByTargetFlowName(targetFlowName);

        updateOptionalExecution(targetFlowName, false);

            for (Map.Entry<String, DataInFlowExecution> freeInputData : currFlowExecution.getFreeInputsExist().entrySet()) {
                String sourceDataName = freeInputData.getValue().getDataDefinitionInFlow().getFinalName();
                if (this.optionalFlowExecution.getFreeInputsExist().get(sourceDataName) != null) {
                    if (this.optionalFlowExecution.getFreeInputsExist().get(sourceDataName).getItem() == null) {
                        this.optionalFlowExecution.getFreeInputsExist().get(sourceDataName).setItem(freeInputData.getValue().getItem());
                    }
                }
            }
            for (ContinuationMapping continuationMapping : currContinuation.getContinuationMapping()) {
                DataInFlowExecution freeInputDetails = findFreeInputDetails(currFlowExecution, continuationMapping.getSourceData());
                if (this.optionalFlowExecution.getFreeInputsExist().get(continuationMapping.getTargetData()).getItem() == null) {
                    this.optionalFlowExecution.getFreeInputsExist().get(continuationMapping.getTargetData()).setItem(freeInputDetails.getItem());
                }
            }

    }

    public DataInFlowExecution findFreeInputDetails (FlowExecution currFlowExecution, String sourceData)
    {
        for(Map.Entry<String, DataInFlowExecution> freeInputData : currFlowExecution.getDataValues().entrySet())
        {
            if(freeInputData.getValue().getDataDefinitionInFlow().getFinalName().equals(sourceData))
                return freeInputData.getValue();
        }
        return null;
    }

    public void validContinuationCorrectData(FlowsDefinition optionalFlowsDefinition)
    {
        for(FlowDefinition flowDefinition : optionalFlowsDefinition.getListFlowsDefinition()) {
            if (flowDefinition.getContinuationsList() != null) {
                for (Continuation continuation : flowDefinition.getContinuationsList()) {
                    FlowDefinition targetFlowDefinition = getFlowDefinitionByFlowNameAndOptionalFlowsJavaFX(optionalFlowsDefinition, continuation.getTargetFlow());
                    for (ContinuationMapping continuationMapping : continuation.getContinuationMapping()) {
                        checkIfContinuationDataCorrect(continuationMapping.getSourceData(), flowDefinition);
                        checkIfContinuationDataCorrect(continuationMapping.getTargetData(), targetFlowDefinition);
                    }
                }
            }
        }
    }

    private void checkIfContinuationDataCorrect(String data, FlowDefinition flowDefinition) {
        boolean flag = false;
        for (StepUsageDeclaration oneStep : flowDefinition.getSteps()) {
            for (Map.Entry<String, DataInFlow> input : flowDefinition.getStepInputs().get(oneStep.getFinalStepName()).entrySet()) {
                if (input.getValue().getFinalName().equals(data))
                    flag = true;
            }
        }
        for (StepUsageDeclaration oneStep : flowDefinition.getSteps()) {
            for (Map.Entry<String, DataInFlow> input : flowDefinition.getStepOutput().get(oneStep.getFinalStepName()).entrySet()) {
                if (input.getValue().getFinalName().equals(data))
                    flag = true;
            }
        }
        if(!flag)
            throw new InvalidDataInContinuation(flowDefinition.getName(), data);
    }

    @Override
    public FlowDefinition getFlowDefinitionByFlowNameAndOptionalFlowsJavaFX(FlowsDefinition optionalFlowsDefinition, String flowName) {

        for(FlowDefinition flowDefinition: optionalFlowsDefinition.getListFlowsDefinition())
        {
            if(flowDefinition.getName().equals(flowName))
                return flowDefinition;
        }

        return null;
    }

    @Override
    public DTOListContinuationFlowName setListContinuationFlowName(String flowName)
    {
        DTOListContinuationFlowName listContinuationFlowName = new DTOListContinuationFlowName();
        FlowDefinition currFlowDefinition = getFlowDefinitionByFlowNameJavaFX(flowName);
        for(Continuation continuation :currFlowDefinition.getContinuationsList())
        {
            listContinuationFlowName.getListContinuationFlowName().add(continuation.getTargetFlow());
        }
        return listContinuationFlowName;
    }
    @Override
    public void updateMandatoryInputEnumerator(String labelValue, String textAreaValue)
    {
        EnumeratorData enumeratorData = new EnumeratorData();
        enumeratorData.add(textAreaValue);
        optionalFlowExecution.getFreeInputsExist().get(labelValue).setItem(enumeratorData);
    }

    @Override
    public DataInFlowExecution findValueOfFreeInput(String flowName, String inputName)
    {
        for (Map.Entry<String, DataInFlowExecution> freeInput : this.optionalFlowExecution.getFreeInputsExist().entrySet()) {
            if (freeInput.getValue().getDataDefinitionInFlow().getFinalName().equals(inputName))
                return freeInput.getValue();
        }
        return null;
    }

    @Override
    public DTOFullDetailsPastRun getFlowExecutedDataDTO(UUID flowId) {
        FlowExecution executedFlow = executedFlowsMap.get(flowId);
        String flowName = executedFlow.getFlowDefinition().getName();
        return new DTOFullDetailsPastRun(executedFlow);
    }


    @Override
    public List<DTOFullDetailsPastRun> getFlowsExecutedDataDTOHistory() {
        List<DTOFullDetailsPastRun> flowsExecutedList = new ArrayList<>();

        for(FlowExecutionImpl flow: listFlowsExecution.getFlowsExecutionList())
        {
            flowsExecutedList.add(new DTOFullDetailsPastRun(flow));
        }
        return flowsExecutedList;
    }
    @Override
    public Map<UUID, FlowExecution> getExecutedFlowsMap() {
        return executedFlowsMap;
    }


    @Override
    public DTOString readingSystemInformationFromFileWeb(InputStream InputStreamFile,boolean isFirstUpload)  {

        DTOString validResult;
        FlowsDefinition optionalFlowsDefinition = new FlowsDefinitionImpl();

        SchemaBasedJAXBMainJavaFX schema = new SchemaBasedJAXBMainJavaFX();
        xmlReaderJavaFX.schema.generated.STStepper stepper = schema.createStepperFromXMLFileForWeb(InputStreamFile);

        checkIfTheStepNamesExistJavaFX(stepper);
        checkDuplicateNamesJavaFX(stepper);
        validContinuationFlowNameExistWeb(stepper);
        transferringDataFromTheXMLClassesToOurClassesJavaFX(stepper, optionalFlowsDefinition);

        checkValidXMLFileJavaFX(stepper, optionalFlowsDefinition);
        validContinuationCorrectData(optionalFlowsDefinition);

        if(isFirstUpload) {
            this.currentFlowsDefinition =  optionalFlowsDefinition;
            this.threadPoolExecutor = Executors.newFixedThreadPool(this.currentFlowsDefinition.getThreadPoolSize());
        }
        else{
            insertMoreFlowsFromXML(optionalFlowsDefinition);
        }

        String result = "The file is found to be correct and fully loaded for the user.";
        validResult = new DTOString(result, true);

        return validResult;
    }

    public void insertMoreFlowsFromXML(FlowsDefinition optionalFlowsDefinition)
    {
        for(FlowDefinition optionalflowDefinition : optionalFlowsDefinition.getListFlowsDefinition())
        {
            boolean isExists = isFlowNameExists(optionalflowDefinition.getName());
            if(!isExists)
            {
                currentFlowsDefinition.getListFlowsDefinition().add(optionalflowDefinition);
            }
        }
    }

    private boolean isFlowNameExists(String name) {

        for(FlowDefinition flowDefinition : currentFlowsDefinition.getListFlowsDefinition())
        {
            if(flowDefinition.getName().equals(name))
                return true;
        }
        return false;
    }

    @Override
    public void continuationToOtherFlowWeb(String currFlowId, String sourceFlowName, String targetFlowName)
    {
        UUID uuidCurrFlowId = UUID.fromString(currFlowId);
        FlowExecution currFlowExecution = this.executedFlowsMap.get(uuidCurrFlowId);
        Continuation currContinuation = currFlowExecution.getFlowDefinition().getContinuationByTargetFlowName(targetFlowName);

        updateOptionalExecution(targetFlowName, false);

        for (Map.Entry<String, DataInFlowExecution> freeInputData : currFlowExecution.getFreeInputsExist().entrySet()) {
            String sourceDataName = freeInputData.getValue().getDataDefinitionInFlow().getFinalName();
            if (this.optionalFlowExecution.getFreeInputsExist().get(sourceDataName) != null) {
                if (this.optionalFlowExecution.getFreeInputsExist().get(sourceDataName).getItem() == null) {
                    this.optionalFlowExecution.getFreeInputsExist().get(sourceDataName).setItem(freeInputData.getValue().getItem());
                }
            }
        }
        for (ContinuationMapping continuationMapping : currContinuation.getContinuationMapping()) {
            DataInFlowExecution freeInputDetails = findFreeInputDetails(currFlowExecution, continuationMapping.getSourceData());
            if (this.optionalFlowExecution.getFreeInputsExist().get(continuationMapping.getTargetData()).getItem() == null) {
                this.optionalFlowExecution.getFreeInputsExist().get(continuationMapping.getTargetData()).setItem(freeInputDetails.getItem());
            }
        }
    }

    @Override
    public UUID updateOptionalExecutionWeb(String flowName, String isContinuation) {
        if(isContinuation.equals("false")) {
            FlowDefinition currFlowDefinition = getFlowDefinitionByFlowNameJavaFX(flowName);
            this.optionalFlowExecution = new FlowExecutionImpl(currFlowDefinition);
        }
        return optionalFlowExecution.getUniqueId();
    }

    @Override
    public DTOFlowExecution removeInitialFreeInputFromDTOWeb(DTOFlowExecution dtoFlowExecution)
    {
        for(InitialInputValue inputValue :optionalFlowExecution.getFlowDefinition().getInitialInputValuesList())
        {
            removeFreeInputFromDTO(dtoFlowExecution.getInputsExecution(), inputValue.getInputName());
        }
        return dtoFlowExecution;
    }

    @Override
    public List<DTOFullDetailsPastRunWeb> getFlowsExecutedDataDTOHistoryWeb() {
        List<DTOFullDetailsPastRunWeb> flowsExecutedList = new ArrayList<>();

        for(FlowExecutionImpl flow: listFlowsExecution.getFlowsExecutionList())
        {
            flowsExecutedList.add(new DTOFullDetailsPastRunWeb(flow));
        }
        return flowsExecutedList;
    }

    @Override
    public DTOFullDetailsPastRunWeb getFlowExecutedDataDTOWeb(UUID flowId) {
        FlowExecution executedFlow = executedFlowsMap.get(flowId);
        String flowName = executedFlow.getFlowDefinition().getName();
        return new DTOFullDetailsPastRunWeb(executedFlow);
    }

    @Override
    public DTOFullDetailsPastRunWeb flowActivationAndExecutionWeb(String flowName)
    {
        ExecutionContextInterface context = new ExecutionContextImpl(this.optionalFlowExecution);
        initialDataValuesFlowExecution(context);

        executedFlowsMap.put(optionalFlowExecution.getUniqueId(), optionalFlowExecution);
        threadPoolExecutor.execute(new FLowExecutor(context, statisticsData));

        listFlowsExecution.getFlowsExecutionList().add(context.getFlowExecution());
        DTOFullDetailsPastRunWeb fullDetails  =  new DTOFullDetailsPastRunWeb(context.getFlowExecution());

        return fullDetails;
    }

    public RolesManager getRolesManager() {
        return roles;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public List<String> getListOfFlowsAvailable()
    {
        List<String> listOfFlowsAvailable = new ArrayList<>();
        for(FlowDefinition flowDefinition : currentFlowsDefinition.getListFlowsDefinition()) {
            listOfFlowsAvailable.add(flowDefinition.getName());
        }
        return listOfFlowsAvailable;
    }




    /*@Override
    public Set<DTOUser> getUsers() {
        Set<DTOUser> users = new HashSet<>();

        for (Map.Entry<String, User> userEntry : this.userManager.getUsers().entrySet()) {
            if (!userEntry.getKey().equals("Admin"))
                users.add(new DTOUser(userEntry.getKey(), userEntry.getValue().getIsManager()));
        }
        return users;
    }*/




}
