package stepper.step.impl;

import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.dataDefinition.impl.file.FileData;
import stepper.dataDefinition.impl.fileList.FileListData;
import stepper.dataDefinition.impl.list.ListData;
import stepper.dataDefinition.impl.mapping.MappingData;
import stepper.dataDefinition.impl.number.IntWrapper;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.io.File;
import java.time.LocalTime;

public class FilesDeleterStep extends AbstractStepDefinition {
    public FilesDeleterStep() {
        super("Files Deleter", false);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.MANDATORY, "Files to delete", DataDefinitionRegistry.FILE_LIST, false,""));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("DELETED_LIST", DataNecessity.NA, "Files failed to be deleted", DataDefinitionRegistry.LIST, false,""));
        addOutput(new DataDefinitionDeclarationImpl("DELETION_STATS", DataNecessity.NA, "Deletion summary results", DataDefinitionRegistry.MAPPING, false,""));
    }

    @Override
    public StepResult invoke(ExecutionContextInterface context)
    {
        FileListData filesList = context.getDataValue("FILES_LIST", FileListData.class, this);
        ListData<String> DELETED_LIST = new ListData();
        MappingData<Integer, Integer> DELETION_STATS = new MappingData<>();
        StepResult result;
        LocalTime localStartTime = LocalTime.now();

        long startTime = System.nanoTime();
        IntWrapper successCount = new IntWrapper(0);
        IntWrapper failureCount =new IntWrapper(0);

        int amountOfFile = filesList.getItem().size();
        context.storeLogsData("About to start delete " + amountOfFile + " files.");

        deleteFile(context, filesList,DELETED_LIST, successCount, failureCount);

        DELETION_STATS.getPairs().put(successCount.getValue(), failureCount.getValue());

        DataInFlowExecution outputExecution1 = createDataInFlowExecution(context,"DELETED_LIST", DELETED_LIST);
        DataInFlowExecution outputExecution2 =createDataInFlowExecution(context,"DELETION_STATS",DELETION_STATS);

        context.storeDataValue("DELETED_LIST", outputExecution1);
        context.storeDataValue("DELETION_STATS", outputExecution2);

        result = updateStatus(context, DELETED_LIST, amountOfFile, failureCount);
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

    public void deleteFile(ExecutionContextInterface context, FileListData FILES_LIST, ListData<String> DELETED_LIST, IntWrapper successCount, IntWrapper failureCount)
    {
        for (FileData file : FILES_LIST.getItem()) {
            file.setItem(new File(file.getFileName()));
            if(file.getItem().isFile()) {
                if (file.getItem().exists()) {
                    if (file.getItem().delete()) {
                        successCount.setValue(successCount.getValue() + 1);
                    } else {
                        String fileName = file.getItem().getAbsolutePath();
                        DELETED_LIST.getItem().add(fileName);
                        failureCount.setValue(failureCount.getValue() + 1);
                        context.storeLogsData("Failed to delete file " + file.getItem().getName());
                    }
                }
            }

        }
    }

    public StepResult updateStatus(ExecutionContextInterface context, ListData<String> DELETED_LIST, int amountOfFile, IntWrapper failureCount) {
        StepResult result;
        if (amountOfFile==0)
        {
            context.updateLogDataAndSummeryLine("There are no files in the given folder, the step ended successfully.");
            result = StepResult.SUCCESS;
        }
        else if (DELETED_LIST.getItem().isEmpty()) {
            context.updateLogDataAndSummeryLine("The Files Deleter operation was performed successfully");
            result = StepResult.SUCCESS;
        } else if (amountOfFile == failureCount.getValue()) {
            context.updateLogDataAndSummeryLine("All files in the list could not be deleted");
            result = StepResult.FAILURE;
        }
         else {
            context.updateLogDataAndSummeryLine("Only some files were successfully deleted");
            result = StepResult.WARNING;
        }
        return result;
    }


}
