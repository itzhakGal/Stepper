package stepper.step.impl;

import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.dataDefinition.impl.string.StringWrapper;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineStep extends AbstractStepDefinition {
    public CommandLineStep() {
        super("Command Line", false);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl("COMMAND", DataNecessity.MANDATORY, "Command", DataDefinitionRegistry.STRING, false,""));
        addInput(new DataDefinitionDeclarationImpl("ARGUMENTS", DataNecessity.OPTIONAL, "Command arguments", DataDefinitionRegistry.STRING, false, ""));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Command output", DataDefinitionRegistry.STRING, false,""));
    }

    @Override
    public StepResult invoke(ExecutionContextInterface context) {
        String COMMAND = context.getDataValue("COMMAND", String.class, this);
        String ARGUMENTS = context.getDataValue("ARGUMENTS", String.class, this);
        List<String> parameters;
        StringWrapper RESULT= new StringWrapper("");
        StepResult stepResult;
        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();

        if (ARGUMENTS != null)
        {
            context.storeLogsData("About to invoke " + COMMAND + " " + ARGUMENTS);
             parameters = convertStringToList(ARGUMENTS);
        }else
        {
            context.storeLogsData("About to invoke " + COMMAND );
             parameters = new ArrayList<>();
        }

        executeCommand(COMMAND, parameters, RESULT, context);
        stepResult = StepResult.SUCCESS;
        LocalTime localEndTime = LocalTime.now();
        context.storeTotalTimeStep(localStartTime, localEndTime, startTime);
        DataInFlowExecution outputExecution = createDataInFlowExecution(context,"RESULT", RESULT.getValue());
        context.storeDataValue("RESULT", outputExecution);
        context.updateStatusStep(stepResult);
        return stepResult;
    }

    public DataInFlowExecution createDataInFlowExecution(ExecutionContextInterface context, String name, Object item)
    {
        DataInFlow outputDefinition = createDataInFlow(name);
        DataInFlowExecution outputExecution = new DataInFlowExecutionImp(outputDefinition);
        outputExecution.setItem(item);
        outputExecution.getDataDefinitionInFlow().setNecessity(DataNecessity.MANDATORY);
        //פלסטררר
        context.updateOutputDataList(outputExecution);
        return outputExecution;
    }

    public static void executeCommand(String command, List<String> parameters, StringWrapper RESULT, ExecutionContextInterface context) {
        try {
            List<String> commandWithParameters = new ArrayList<>();
            commandWithParameters.add("cmd.exe");
            commandWithParameters.add("/c");
            commandWithParameters.add(command);
            if(!parameters.isEmpty())
                commandWithParameters.addAll(parameters);

            ProcessBuilder processBuilder = new ProcessBuilder(commandWithParameters);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                RESULT.setValue(output.toString().trim());
                context.updateLogDataAndSummeryLine(output.toString().trim());
            } else {
                RESULT.setValue("Command execution failed with exit code: " + exitCode);
                context.updateLogDataAndSummeryLine("Command execution failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            RESULT.setValue("Exception occurred: " + e.getMessage());
            context.updateLogDataAndSummeryLine("Exception occurred: " + e.getMessage());
        }
    }

        public static List<String> convertStringToList(String data) {
            String[] dataArray = data.split(" ");
            return Arrays.asList(dataArray);
        }


}