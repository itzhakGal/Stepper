package stepper.step.impl;

import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.dataDefinition.impl.file.FileData;
import stepper.dataDefinition.impl.fileList.FileListData;
import stepper.dataDefinition.impl.relation.RelationData;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.StepExecutionDataImpl;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.io.File;
import java.time.LocalTime;

public class FilesRenamerStep extends AbstractStepDefinition {

    public FilesRenamerStep() {
        super("Files Renamer", false);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FILES_TO_RENAME", DataNecessity.MANDATORY, "Files to rename", DataDefinitionRegistry.FILE_LIST, false,""));
        addInput(new DataDefinitionDeclarationImpl("PREFIX", DataNecessity.OPTIONAL, "Add this prefix", DataDefinitionRegistry.STRING, false,""));
        addInput(new DataDefinitionDeclarationImpl("SUFFIX", DataNecessity.OPTIONAL, "Append this suffix", DataDefinitionRegistry.STRING, false,""));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RENAME_RESULT", DataNecessity.NA, "Rename operation summary", DataDefinitionRegistry.RELATION, false,""));
    }


    @Override
    public StepResult invoke(ExecutionContextInterface context)
    {
        FileListData filesList = context.getDataValue("FILES_TO_RENAME", FileListData.class, this);
        String prefix = context.getDataValue("PREFIX", String.class, this);
        String suffix = context.getDataValue("SUFFIX", String.class, this);
        StepResult result;
        RelationData RENAME_RESULT = new RelationData("Serial Number", "The name of the original file", "The name of the new file");

        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();

        int amountOfFile = filesList.getItem().size();

        context.storeLogsData("About to start rename " + amountOfFile + " files. Adding prefix: " + prefix + " Adding suffix: " + suffix);
        boolean flag = true;
        if (amountOfFile == 0)
        {
            context.updateLogDataAndSummeryLine("The Files Renamer operation was performed successfully. There are no files in the list");
            result = StepResult.SUCCESS;
        } else {
            for (FileData file : filesList.getItem()) {
                file.setItem(new File(file.getFileName()));
                if (file.getItem().exists()) {
                    FileData newNameFile;
                    String newName = createNewName(file, prefix, suffix);
                    if(prefix == null && suffix ==null) {
                        newNameFile = new FileData(newName);
                        newNameFile.setItem(new File(newNameFile.getFileName()));
                    }
                    else
                       newNameFile = new FileData(file.getItem().getParent(), newName);
                    flag = renameFile(context,file, newNameFile, RENAME_RESULT, newName);
                }
                else {
                    if (flag) {
                        StepExecutionDataImpl dataStep = context.convertFromNameStepExecutionData();
                        String summaryLine = dataStep.getSummaryLine();
                        dataStep.setSummaryLine(summaryLine += "The file " + file.getItem().getName() + " does not exist\n");
                        flag = false;
                    }
                }
            }
            result = updateStatus(context, flag);
        }
        DataInFlowExecution outputExecution = createDataInFlowExecution(context,"RENAME_RESULT", RENAME_RESULT);
        context.storeDataValue("RENAME_RESULT", outputExecution);
        LocalTime localEndTime = LocalTime.now();
        context.storeTotalTimeStep(localStartTime, localEndTime, startTime);
        context.updateStatusStep(result);
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

    public String createNewName(FileData file, String PREFIX, String SUFFIX) {
        String basename = file.getItem().getName().substring(0, file.getItem().getName().lastIndexOf('.'));
        String extension = file.getItem().getName().substring(file.getItem().getName().lastIndexOf('.'));
        String newName;
        if (PREFIX != null && SUFFIX!= null) {
            newName = PREFIX + basename + SUFFIX + extension;
        }
        else if (PREFIX != null) {
            newName = PREFIX + basename + extension;
        } else if (SUFFIX != null) {
            newName = basename + SUFFIX + extension;
        } else {
            newName = file.getFileName();
        }
        return newName;
    }

    public StepResult updateStatus(ExecutionContextInterface context, boolean flag) {
        StepResult result;
        if (!flag) {
            result = StepResult.WARNING;
            context.storeLogsData("Not all required files were successfully renamed");
        } else {
            context.updateLogDataAndSummeryLine("All required files were successfully renamed");
            result = StepResult.SUCCESS;
        }
        return result;
    }

    public boolean renameFile(ExecutionContextInterface context, FileData file, FileData newNameFile, RelationData RENAME_RESULT, String newName  )
    {
        boolean success = file.getItem().renameTo(newNameFile.getItem());
        String serialNumber = RENAME_RESULT.getNumberOfLines() + "";
        String currFileName = file.getItem().getName();
        if (success) {
            file.setFileName(newName);
            RENAME_RESULT.addRow(serialNumber, currFileName, newNameFile.getItem().getName());
        } else {
            context.storeLogsData("Problem renaming file " + currFileName);
            StepExecutionDataImpl dataStep = context.convertFromNameStepExecutionData();
            String summaryLine = dataStep.getSummaryLine();
            summaryLine += "Problem renaming file " + currFileName +"\n";
            dataStep.setSummaryLine(summaryLine);
            RENAME_RESULT.addRow(serialNumber, currFileName, currFileName);
        }
        return success;
    }
}
