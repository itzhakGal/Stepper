package consoleUI;

import exception.InvalidChoiceException;
import exception.InvalidFileException;
import exception.InvalidSelectingCommandException;
import exception.InvalidSelectingCommandOrderException;
import stepper.step.api.LoggerImpl;
import stepper.systemEngine.SystemEngine;
import stepper.systemEngine.SystemEngineInterface;
import utils.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {
    private boolean runSystem;
    private SystemEngineInterface systemEngine;

    public ConsoleUI() {
        this.runSystem = true;
        this.systemEngine = new SystemEngine();
    }
    public static void main(String[] args) {
        ConsoleUI console = new ConsoleUI();
        BooleanWrapper flagCommandOne = new BooleanWrapper(false);
        BooleanWrapper flagCommandThree = new BooleanWrapper(false);
        BooleanWrapper flagCommandSix = new BooleanWrapper(false);
        System.out.println("Hello and welcome to Stepper!");
        while (console.runSystem) {
            printMenu();
            Scanner scanner = new Scanner(System.in);
            int choice ;
            String choiceString;
            try {
                choiceString = scanner.nextLine();
                choice = Integer.parseInt(choiceString);
                while (!(choice >= 1 && choice <= 8)) {
                    System.out.println("Invalid choice. Please enter a number from 1 to 8: ");
                    printMenu();
                    choiceString = scanner.nextLine();
                    choice = Integer.parseInt(choiceString);
                }
                console.optionalChoices(choice, flagCommandOne, flagCommandThree, flagCommandSix);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number from 1 to 8: ");
                scanner.nextLine(); // consume the invalid input
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number from 1 to 8: ");
            }
        }
    }
    public static void printMenu() {
        System.out.println("Please select the number of the action you would like to perform from the following menu:");
        System.out.println("-----------------------------------------------------------");
        for (eMainMenuOption option : eMainMenuOption.values()) {
            System.out.println((option.ordinal() + 1) + ". " + option);
        }
        System.out.println("-----------------------------------------------------------");
    }

    public void optionalChoices(int userChoice,  BooleanWrapper flagCommandOne, BooleanWrapper flagCommandThree, BooleanWrapper flagCommandSix) {
        boolean isValidData = false;
        int choice;
        String choiceString;
        Scanner scanner = new Scanner(System.in);
        while (!isValidData) {
            try {
                switch (userChoice) {
                    case 1:
                        loadingDataFromAnXMLaFile();
                        flagCommandOne.setValue(true);
                        break;
                    case 2:
                        if(flagCommandOne.isValue() || flagCommandSix.isValue())
                            showFlowDefinition();
                        else
                            throw new InvalidSelectingCommandOrderException();
                        break;
                    case 3:
                        if(flagCommandOne.isValue() || flagCommandSix.isValue()) {
                            boolean flag = executionFlow();
                            flagCommandThree.setValue(flag);
                        }
                        else
                            throw new InvalidSelectingCommandOrderException();
                        break;
                    case 4:
                        if((flagCommandOne.isValue() && flagCommandThree.isValue()) || flagCommandSix.isValue())
                             DisplayingFullDetailsOfPastActivation();
                        else
                            throw new InvalidSelectingCommandException();
                        break;
                    case 5:
                        if((flagCommandOne.isValue() && flagCommandThree.isValue()) || flagCommandSix.isValue())
                            showStatistics();
                        else
                            throw new InvalidSelectingCommandException();
                        break;
                    case 6:
                        loadDataFromFile();
                        flagCommandSix.setValue(true);
                        break;
                    case 7:
                        if((flagCommandOne.isValue() || flagCommandSix.isValue()))
                            storeDataToFile();
                        else
                            throw new InvalidSelectingCommandOrderException();
                        break;
                    case 8:
                        System.out.println("You are logged out, goodbye.");
                        runSystem = false;
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
                isValidData = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice: " + e.getMessage());
            }
            catch (InvalidChoiceException e) {
                System.out.println("Invalid choice: " + e.getMessage());
            } catch (InvalidSelectingCommandOrderException | InvalidSelectingCommandException e) {
                System.out.println(e.getMessage());
                  return;
             }
            catch (RuntimeException | IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }

            if (!isValidData) {
                try {
                    System.out.println("Type 1 if you want to keep trying or type 0 if you want to go back to the menu: ");
                    choiceString = scanner.nextLine();
                    choice = Integer.parseInt(choiceString);
                    if (choice == 0)
                        return;
                    else if (choice != 1 && choice != 0)
                        throw new InvalidChoiceException("Invalid choice: " + choice);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice: " + e.getMessage());
                } catch (InvalidChoiceException e) {
                    System.out.println("Invalid choice: " + e.getMessage());
                }
            }
        }
    }

    private void storeDataToFile() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the path of a file to which you would like to save the information");
        String path = scanner.nextLine();
        systemEngine.saveToFile(path);
        System.out.println("File saved successfully");
    }
    private void loadDataFromFile() throws IOException, ClassNotFoundException {
        System.out.println("Please enter the path of a file that you would like to upload to the system");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        File file = new File(path);
        if (!file.exists()) {
            throw new InvalidFileException("Invalid path. file does not exist.");
        }
        systemEngine.loadFromFile(path);
    }
    private void showStatistics() {
        DTOStatistics statistics = systemEngine.readStatistics();
        displayFlowStatistics(statistics.getStatisticsFlows());
        displayStepStatistics(statistics.getStatisticsSteps());
    }
    private void DisplayingFullDetailsOfPastActivation() {
        int selection;
        DTOFlowsPastInformation flowsPastInformation = systemEngine.readFlowsPast();
        showFlowsPastInformation(flowsPastInformation);
        while (true) {
            try {
                selection = selectionNumber("Choose the number of the run you want to get full details on or 0 to return to the menu");
                if (selection == 0) {
                    return;
                } else if (selection < 0 || selection > flowsPastInformation.getFlows().size()) {
                    throw new InvalidChoiceException("Invalid choice, you have to enter a number between 1 to " + flowsPastInformation.getFlows().size());
                } else {
                    DTOFullDetailsPastRun fullDetailsPastRun = systemEngine.displayFullDetailsOfPastActivation(selection);
                    showFullDetailsInformationPast(fullDetailsPastRun);
                    break; // exit the loop if no exception is thrown
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, you have to enter a number: ");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, you have to enter a number: ");
            } catch (InvalidChoiceException e) {
                System.out.println( e.getMessage());
            }
        }
    }
    private boolean executionFlow() throws InvalidChoiceException {
        DTOFlowsNames flowsNames = systemEngine.readFlowName();
        showFlowsNames(flowsNames);
        boolean successExecutionFlow = false;
        int selection;
        while (true) {
            try {
        selection = selectionNumber("Please select a flow number or 0 to return to the main menu");
        if (selection == 0)
            return successExecutionFlow;
        else if (selection < 0 || selection > flowsNames.getFlowNames().size()) {
            throw new InvalidChoiceException("Invalid choice, you have to enter a number between 1 to " + flowsNames.getFlowNames().size());
        } else {
            systemEngine.initialOptionalFlowExecution(selection);
            int selectionInput = freeInputUpdateProcess(selection);
            if(selectionInput==0)
                return successExecutionFlow;
            System.out.println("Starting execution of flow " + flowsNames.getFlowNames().get(selection-1));
            DTOEndOFlowExecution endOFlowExecution =  systemEngine.flowActivationAndExecution(selection);
            displayInformationFlowRun(endOFlowExecution);
            successExecutionFlow = true;
            break; // exit the loop if no exception is thrown
        }
            }catch (NumberFormatException e) {
                System.out.println("Invalid input, you have to enter a number: ");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, you have to enter a number: ");
            } catch (InvalidChoiceException e) {
                System.out.println( e.getMessage());
            }
        }
        return successExecutionFlow;
    }
    private int freeInputUpdateProcess(int selectionFlow) {
        Scanner scanner = new Scanner(System.in);
        int selectionInput = -1;

        // Keep track of whether all mandatory inputs have been received
        boolean allMandatoryInputsReceived = false;
        boolean continueUpdatingInputs = true;

        // Loop until all mandatory inputs have been received and the user chooses to stop updating inputs
        while (!allMandatoryInputsReceived || continueUpdatingInputs) {
            // Display all inputs to the user
            DTOFlowExecution dtoFlowExecution = systemEngine.readInputs(selectionFlow); // Fill in the input data
            showAllInputs(dtoFlowExecution);

            // Ask user to select input to update
            while (true) {
                try {
                    selectionInput = selectionInputNumber("Please enter the number of the input to update or 0 to return to the main menu:");

                    // Validate user input
                    while (selectionInput == 0 || selectionInput > dtoFlowExecution.getInputsExecution().size()) {
                        if (selectionInput == 0) {
                            // User chose to return to the menu, exit function
                            return selectionInput;
                        } else {
                            // Invalid input, ask user to enter valid input
                            selectionInput = selectionNumber("Invalid selection. Please enter the number of the input to update or 0 to return to the menu:");
                        }
                    }
                    allMandatoryInputsReceived = isAllMandatoryInputsReceived(scanner, selectionInput, dtoFlowExecution);

                    // Ask user if they want to continue updating inputs
                    continueUpdatingInputs = isContinueUpdatingInputs(scanner, allMandatoryInputsReceived, continueUpdatingInputs);

                    // Exit the inner while loop as a valid input has been received
                    break;
                }catch (NumberFormatException e) {
                    System.out.println("Invalid input, you have to enter a number: ");
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input, you have to enter a number: ");
                }catch (InvalidChoiceException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return selectionInput;
    }
    private boolean isAllMandatoryInputsReceived(Scanner scanner, int selectionInput, DTOFlowExecution dtoFlowExecution) {
        boolean allMandatoryInputsReceived;
        // Get name of selected input and ask user to enter new value
        String finalInputName = dtoFlowExecution.getInputsExecution().get(selectionInput -1).getFinalName();
        String type= dtoFlowExecution.getInputsExecution().get(selectionInput -1).getType();
        System.out.println("Please enter the value of your selection input:");
        String inputString = scanner.nextLine();

        if(type.equals("Integer")) {
            try {
                // Try to parse the value as an integer
                int intValue = Integer.parseInt(inputString);
                systemEngine.updateMandatoryInput(finalInputName, intValue);
            } catch (NumberFormatException e) {
                // Throw a NumberFormatException with an appropriate error message if the value is not an integer
                throw new NumberFormatException("The input value is not an int.");
            }
        }else {
            systemEngine.updateMandatoryInput(finalInputName, inputString);
        }

        // Check if all mandatory inputs have been received
        DTOMandatoryInputs dtoMandatoryInputs = systemEngine.checkMandatoryExist();
        allMandatoryInputsReceived = dtoMandatoryInputs.isMandatoryFlag();
        return allMandatoryInputsReceived;
    }
    private static boolean isContinueUpdatingInputs(Scanner scanner, boolean allMandatoryInputsReceived, boolean continueUpdatingInputs) {

        if (allMandatoryInputsReceived) {
            System.out.println("All mandatory inputs have been received. If you want to continue updating inputs enter 1 or if you want to finish enter 2.");
            String continueOption  = scanner.nextLine();
            if(continueOption.equals("2"))
                continueUpdatingInputs = false;
            else if(continueOption.equals("1"))
                continueUpdatingInputs = true;
            else
                throw new InvalidChoiceException("Invalid choice");
        }
        return continueUpdatingInputs;
    }
    public int selectionInputNumber( String str)
    {
        Scanner input = new Scanner(System.in);
        int choice;
        String choiceString;
        System.out.println(str);
        choiceString = input.nextLine();
        choice = Integer.parseInt(choiceString);

        return  choice;
    }
    private void showFlowDefinition()  {
        int selection;
        DTOFlowsNames flowsNames = systemEngine.readFlowName();
        showFlowsNames(flowsNames);
        while (true) {
            try {
        selection = selectionNumber("Please select a flow number or 0 to return to the main menu");
        if (selection == 0)
            return;
        else if (selection < 0 || selection > flowsNames.getFlowNames().size()) {
            throw new InvalidChoiceException("Invalid choice, you have to enter a number between 1 to " + flowsNames.getFlowNames().size());
        } else {
            DTOFlowDefinition flowDefinition = systemEngine.introducingFlowDefinition(selection);
            showFlowDefinitionDetails(flowDefinition);
            break; // exit the loop if no exception is thrown
        }
            }catch (NumberFormatException e) {
                System.out.println("Invalid input, you have to enter a number: ");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, you have to enter a number: ");
            } catch (InvalidChoiceException e) {
                System.out.println( e.getMessage());
            }
        }
    }
    public void loadingDataFromAnXMLaFile() {
        boolean flag;
        StringBuilder path = new StringBuilder("");

        flag = validationXMLFile(path);
        String pathString = path.toString();
        if(flag)
        {
            File file = new File(pathString);
            if (!file.canRead()) {
                throw  new InvalidFileException ("The file is unreadable");
            } else {
                DTOString result = systemEngine.readingSystemInformationFromFile(file);
                System.out.println(result.getValidResult() + "\n");
            }
        }
        else
            throw  new InvalidChoiceException("The number entered is not one of the possible options");
    }
    public boolean validationXMLFile(StringBuilder pathBuilder)  {
        //validation of enter XML file from user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the full path to the XML file you want to load:");
        String path = scanner.nextLine();
        pathBuilder.append(path);

        // Create a File object with the path and check if it exists
        File file = new File(path);
        if (!file.exists()) {
            throw new InvalidFileException("Invalid path. file does not exist.");
        }

        // Check if the file is an XML file
        if (!file.getName().endsWith(".xml")) {
            throw new InvalidFileException("Invalid file type. The file must be an XML file.");
        }
        return true;
    }
    public void showFlowsNames(DTOFlowsNames flowsNames)
    {
        System.out.println( "-----------------------------------------------------------" );
        System.out.println("List of flows names:");
        int i=1;
        for (String name : flowsNames.getFlowNames()) {
            System.out.println(i + "." + name);
            i++;
        }
        System.out.println( "-----------------------------------------------------------" );
    }
    public int selectionNumber(String str)
    {
        Scanner input = new Scanner(System.in);
        int choice;
        String choiceString;
        System.out.println(str);
        choiceString = input.nextLine();
        choice = Integer.parseInt(choiceString);
        return  choice;
    }
    public void showFlowDefinitionDetails(DTOFlowDefinition flowDefinition)
    {
        System.out.println( "-----------------------------------------------------------" );
        System.out.println("Flow Name: " + flowDefinition.getName());
        System.out.println("Flow Description: " + flowDefinition.getDescription());
        System.out.println("Formal Output List: " + String.join(", ", flowDefinition.getFormalOutputList()));
        System.out.println("Is the flow read-only? " + (flowDefinition.isReadOnly() ? "Yes" : "No")+ "\n");

        System.out.println("Steps included in the flow:");
        System.out.println( "-----------------------------" );
        for (DTOStepDefinition step : flowDefinition.getSteps()) {
            if(step.getName().equals(step.getAlias()))
                System.out.println("Step Name: " + step.getName());
            else
                System.out.println("Step Name: " + step.getName() + " (Alias: " + step.getAlias()+ ")");
            System.out.println("Is Step Read-only? " + (step.isReadOnly() ? "Yes" : "No")+ "\n");
        }

        System.out.println("Free Inputs:");
        System.out.println( "---------------" );
        for (DTOInputDefinition input : flowDefinition.getInputs()) {
            System.out.println("Final Name: " + input.getName() );
            System.out.println("Type: " + input.getType());
            System.out.println("Related Steps: " + input.getRelatedSteps());
            System.out.println("Is Mandatory? " + (input.isMandatory() ? "Yes" : "No")+ "\n");
        }

        System.out.println("Outputs produced within the flow:");
        System.out.println( "---------------------------------" );
        for (DTOOutputDefinition output : flowDefinition.getOutputs()) {
            System.out.println("Final Name: " + output.getName());
            System.out.println("Type: " + output.getType());
            System.out.println("Producing Step: " + output.getStepName()+ "\n");
        }
        System.out.println( "-----------------------------------------------------------" );
    }
    public void showAllInputs(DTOFlowExecution flowExecution)
    {
        System.out.println( "-----------------------------------------------------------" );
        System.out.println("Mandatory inputs:");
        System.out.println("-----------------");
        int i=1;
        for (DTOInputExecution input : flowExecution.getInputsExecution()) {
            System.out.println("Input number " + i + ": ");
            System.out.println("Input Name: " + input.getUserString() + " (" + input.getFinalName() +")" );
            System.out.println("Input Type: " + input.getNecessity()+ "\n");
            i++;
        }
        System.out.println( "-----------------------------------------------------------" );
    }
    public void showFlowsPastInformation(DTOFlowsPastInformation flowsPastInformation)
    {
        System.out.println( "-----------------------------------------------------------" );
        int i=1;
        for(DTOFlowPastRun flow: flowsPastInformation.getFlows()) {
            System.out.println("Flow number:" + i);
            System.out.println("Flow Name: " + flow.getFlowName());
            System.out.println("Unique ID: " + flow.getUniqueId());
            System.out.println("Execution Time: " + flow.getActivationDate() + "\n");
            i++;
        }
        System.out.println( "-----------------------------------------------------------" );
    }
    public void showFullDetailsInformationPast(DTOFullDetailsPastRun fullDetailsPastRun) {
        System.out.println( "-----------------------------------------------------------" );
        System.out.println("Flow Details:");
        System.out.println("Flow Name: " + fullDetailsPastRun.getFlowName());
        System.out.println("Unique Identity: " + fullDetailsPastRun.getUniqueId());
        System.out.println("Flow Result: " + fullDetailsPastRun.getFinalResult().getDescription());
        System.out.println("Total Running Time (ms): " + fullDetailsPastRun.getTotalTime().toMillis()); // לבדוק שזה בms

        System.out.println( "-----------------------------------------------------------" );
        System.out.println("Inputs:");
        System.out.println( "-------");
        for (DTOInputFlowPast input : fullDetailsPastRun.getInputs()) //לסדר לפי הסדר המבוקש
        {
            System.out.println("Final name: " + input.getFinalName());
            System.out.println("Type: " + input.getType());
            Class<?> typeClass=  input.getClassValue();
            if(input.getValue()!=null) {
                System.out.println("Content: " + ((typeClass).cast(input.getValue())).toString());
            }else
                System.out.println("Content: " + "An optional uninitialized value");
            System.out.println("The input is: " + input.getNecessity().getDescription() + "\n");
        }

        System.out.println( "-----------------------------------------------------------" );
        System.out.println("Outputs:");
        System.out.println( "-------");
        for (DTOOutPutFlowPast output : fullDetailsPastRun.getOutputs()) {
            System.out.println("Final name: " + output.getFinalName());
            System.out.println("Type: " + output.getType());
            Class<?> typeClass=  output.getClassValue();
            System.out.println("Content: \n" + ((typeClass).cast(output.getContent())).toString()+ "\n");
        }

        System.out.println( "-----------------------------------------------------------" );
        System.out.println("Steps:");
        System.out.println( "-------");
        for (DTOStepFlowPast step : fullDetailsPastRun.getSteps()) {
            System.out.println("Step Name: " + step.getFinalStepName());
            System.out.println("Duration (ms): " + step.getDuration().toMillis());
            System.out.println("Step Result: " + step.getCompletionResult().getDescription());
            System.out.println("Step Summary line: " + step.getSummaryLine());
            System.out.println("Logs:");
            System.out.println( "------");
            int i=1;
            for (LoggerImpl log : step.getLogs()) {
                System.out.println(i + ") " + "Timestamp: " + log.getLogTimeAsString());
                System.out.println("Contents: " + log.getLog());
                i++;
            }
            System.out.println("\n");
        }
        System.out.println( "-----------------------------------------------------------" );
    }
    public void displayInformationFlowRun(DTOEndOFlowExecution flowRun) {
        System.out.println("\nIdentity: " + flowRun.getIdentity());
        System.out.println("Name: " + flowRun.getName());
        System.out.println("Result: " + flowRun.getResult().getDescription() + "\n");
        System.out.println("Formal outputs: ");
        int i=1;
        System.out.println("-----------------");
        for (Map.Entry<String, Data> entry : flowRun.getOutputs().entrySet()) {
            System.out.println(i+") "+ entry.getValue().getUserString());
            System.out.println(((entry.getValue().getItemType()).cast(entry.getValue().getItem())).toString() + "\n");
            i++;
        }
    }
    public void displayFlowStatistics(DTOStatisticsFlows statistics)
    {
        System.out.println("Flow Statistics:");
        System.out.println("-----------------");
        for (String flow : statistics.getExecutions().keySet()) {
            int countFlowExecutions = statistics.getExecutions().get(flow);
            long flowTotalTime = statistics.getTotalTimes().getOrDefault(flow, 0L);
            double flowAvgTime = countFlowExecutions > 0 ? (double) flowTotalTime / countFlowExecutions : 0;

            System.out.println("Flow: " + flow);
            System.out.println("Executions: " + countFlowExecutions);
            System.out.println("Average Execution Time: " + flowAvgTime + " ms\n");
        }
    }
    public void displayStepStatistics(DTOStatisticsSteps statisticsSteps)
    {
        System.out.println("Step Statistics:");
        System.out.println("-----------------");
        for (String step : statisticsSteps.getExecutions().keySet()) {
            int countStepExecutions = statisticsSteps.getExecutions().get(step);
            long stepTotalTime = statisticsSteps.getTotalTimes().getOrDefault(step, 0L);
            double stepAvgTime = countStepExecutions > 0 ? (double) stepTotalTime / countStepExecutions : 0;

            System.out.println("Step: " + step);
            System.out.println("Executions: " + countStepExecutions);
            System.out.println("Average Execution Time: " + stepAvgTime + " ms\n");
        }
    }
}



