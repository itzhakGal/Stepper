package stepper.step.impl;

import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.StepExecutionDataImpl;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

public class FileDumperStep extends AbstractStepDefinition
{

    public FileDumperStep() {
        super("File Dumper", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("CONTENT", DataNecessity.MANDATORY, "Content", DataDefinitionRegistry.STRING, false,""));
        addInput(new DataDefinitionDeclarationImpl("FILE_NAME", DataNecessity.MANDATORY, "Target file path", DataDefinitionRegistry.STRING, true,""));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "File Creation Result", DataDefinitionRegistry.STRING, false,""));

    }

    @Override
    public StepResult invoke(ExecutionContextInterface context)
    {
        String CONTENT = context.getDataValue("CONTENT", String.class,this);
        String FILE_NAME = context.getDataValue("FILE_NAME", String.class,this);
        StepResult result;
        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();


        try {
            File file = new File(FILE_NAME);
            String fileName = file.getName();
            // if (!file.exists())
             //   throw new IOException();
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
            context.storeLogsData("About to create file named " + fileName);
            if (CONTENT.isEmpty()) {
                context.updateLogDataAndSummeryLine("The given sting has no content ");
                result = StepResult.WARNING;
            }
            else {
                writer.write(CONTENT);
                context.updateLogDataAndSummeryLine("File created successfully.");
                result = StepResult.SUCCESS;
            }
            writer.close();
        } catch (IOException e) {
            context.updateLogDataAndSummeryLine("Error creating file: ");
            result = StepResult.FAILURE;
        }
        StepExecutionDataImpl dataStep = context.convertFromNameStepExecutionData();
        String RESULT = dataStep.getSummaryLine();
        DataInFlowExecution outputExecution = createDataInFlowExecution(context,"RESULT", RESULT);
        context.storeDataValue("RESULT", outputExecution);
        context.updateStatusStep(result);
        LocalTime localEndTime = LocalTime.now();
        context.storeTotalTimeStep(localStartTime, localEndTime, startTime);
        return result;

    }

    public DataInFlowExecution createDataInFlowExecution(ExecutionContextInterface context, String name, Object item)
    {
        DataInFlow outputDefinition = createDataInFlow(name);
        DataInFlowExecution outputExecution = new DataInFlowExecutionImp(outputDefinition);
        outputExecution.setItem(item);
        outputExecution.getDataDefinitionInFlow().setNecessity(DataNecessity.MANDATORY);
        context.updateOutputDataList(outputExecution);
        return outputExecution;
    }

}
