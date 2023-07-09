package stepper.systemEngine;

import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.execution.FlowExecution;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.flows.definition.FlowsDefinition;
import utilWebApp.DTOFullDetailsPastRunWeb;
import utilWebApp.DTOUser;
import utils.*;
import utilsDesktopApp.DTOListContinuationFlowName;
import utilsDesktopApp.DTOListFlowsDetails;
import xmlReader.schema.generated.STStepper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface SystemEngineInterface extends Serializable {

    DTOString readingSystemInformationFromFile(File file);

    DTOFlowsNames readFlowName();

    DTOFlowsPastInformation readFlowsPast();

    DTOFlowExecution readInputs(int choice);

    DTOFlowDefinition introducingFlowDefinition(int choice);

    DTOEndOFlowExecution flowActivationAndExecution(int choice);

    DTOFullDetailsPastRun displayFullDetailsOfPastActivation(int choice);

    DTOStatistics readStatistics();

    void checkValidXMLFile(STStepper stepper, FlowsDefinition optionalFlowsDefinition);

    void logOffFromSystem();

    DTOMandatoryInputs checkMandatoryExist();

    void initialOptionalFlowExecution(int selectionFlow);

    void updateMandatoryInput(String selectionInput, Object value);

    void checkIfTheStepNamesExist(STStepper stepper);

    void checkDuplicateNames(STStepper stepper);

    void initialDataValuesFlowExecution(ExecutionContextInterface context);

    void transferringDataFromTheXMLClassesToOurClasses(STStepper stepperData, FlowsDefinition optionalFlowsDefinition);

    void saveToFile(String path) throws IOException;

    void loadFromFile(String path) throws IOException, ClassNotFoundException;

    DTOListFlowsDetails readFlowsDetails();

    DTOFlowDefinition introducingFlowDefinitionJavaFX(String flowName);

    DTOString readingSystemInformationFromFileJavaFX(File file);

    void transferringDataFromTheXMLClassesToOurClassesJavaFX(xmlReaderJavaFX.schema.generated.STStepper stepperData, FlowsDefinition optionalFlowsDefinition);

    void checkIfTheStepNamesExistJavaFX(xmlReaderJavaFX.schema.generated.STStepper stepper);

    void checkDuplicateNamesJavaFX(xmlReaderJavaFX.schema.generated.STStepper stepper);

    void checkValidXMLFileJavaFX(xmlReaderJavaFX.schema.generated.STStepper stepper, FlowsDefinition optionalFlowsDefinition);

    FlowDefinition getFlowDefinitionByFlowNameJavaFX(String flowName);

    DTOFlowExecution readInputsJavaFX(String flowName);

    UUID updateOptionalExecution(String flowName, boolean isContinuation);

    DTOFullDetailsPastRun flowActivationAndExecutionJavaFX(String flowName);

    FlowExecution getFlowExecutionByFlowNameJavaFX(String flowName);

    DTOFullDetailsPastRun displayFullDetailsOfPastActivationJavaFX(String flowName);

    void validContinuationFlowNameExist(xmlReaderJavaFX.schema.generated.STStepper stepper);

    public void continuationToOtherFlow(UUID currFlowId, String sourceFlowName, String targetFlowName);

    FlowDefinition getFlowDefinitionByFlowNameAndOptionalFlowsJavaFX(FlowsDefinition optionalFlowsDefinition, String flowName);

    DTOListContinuationFlowName setListContinuationFlowName(String flowName);

    void updateMandatoryInputEnumerator(String labelValue, String textAreaValue);

    //Object findValueOfFreeInput(String flowName, String inputName, boolean checkIfRunButtonClick);

    DataInFlowExecution findValueOfFreeInput(String flowName, String inputName);

    boolean checkIfInputIsInitialValue(String inputName);

    DTOFullDetailsPastRun getFlowExecutedDataDTO(UUID flowId);

    void removeFreeInputFromDTO(List<DTOInputExecution> inputsExecution, String inputName);

    void removeInitialFreeInputFromDTO(DTOFlowExecution dtoFlowExecution);

    List<DTOFullDetailsPastRun> getFlowsExecutedDataDTOHistory();

    public Map<UUID, FlowExecution> getExecutedFlowsMap();

    DTOString readingSystemInformationFromFileWeb(InputStream file, boolean isFirstUpload);
    void continuationToOtherFlowWeb(String currFlowId, String sourceFlowName, String targetFlowName);
    UUID updateOptionalExecutionWeb(String flowName, String isContinuation);
    DTOFlowExecution removeInitialFreeInputFromDTOWeb(DTOFlowExecution dtoFlowExecution);
    List<DTOFullDetailsPastRunWeb> getFlowsExecutedDataDTOHistoryWeb();
    DTOFullDetailsPastRunWeb getFlowExecutedDataDTOWeb(UUID flowId);
    DTOFullDetailsPastRunWeb flowActivationAndExecutionWeb(String flowName);
    void validContinuationFlowNameExistWeb(xmlReaderJavaFX.schema.generated.STStepper stepper);
    //Set<DTOUser> getUsers();

}

