package stepper.step.impl;

import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.dataDefinition.impl.file.FileData;
import stepper.dataDefinition.impl.fileList.FileListData;
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

public class CollectFilesInFolderStep extends AbstractStepDefinition {

    public CollectFilesInFolderStep() {
        super("Collect Files In Folder", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME", DataNecessity.MANDATORY, "Folder name to scan", DataDefinitionRegistry.STRING, true, ""));

        addInput(new DataDefinitionDeclarationImpl("FILTER", DataNecessity.OPTIONAL, "Filter only these files", DataDefinitionRegistry.STRING, false, ""));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.NA, "Files list", DataDefinitionRegistry.FILE_LIST, false, ""));
        addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND", DataNecessity.NA, "Total files found", DataDefinitionRegistry.NUMBER, false,""));
    }

    @Override
    public StepResult invoke(ExecutionContextInterface context) {

        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();

        String folderName = context.getDataValue("FOLDER_NAME", String.class, this);
        String filter =  context.getDataValue("FILTER", String.class, this);


        StepResult result = collectFiles(context, folderName, filter);
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

    public StepResult collectFiles(ExecutionContextInterface context, String folderName, String filter) {

        FileListData FILES_LIST = new FileListData(); // רוצים להחזיר FILE DATA ולא דפינישן
        IntWrapper TOTAL_FOUND = new IntWrapper(0);
        StepResult result;

        File directory = new File(folderName);
        if (!directory.exists() || !directory.isDirectory())
        {
            context.updateLogDataAndSummeryLine("Folder does not exist, or the entered path is not a file");
            result = StepResult.FAILURE;
        }
        else {
            File[] files = directory.listFiles();
            context.storeLogsData("Reading folder: " + directory.getAbsolutePath() + " content with filter " + filter);
            createListOfFiles(files, TOTAL_FOUND, FILES_LIST, filter); //לבדוק אם הנתונים מתעדכנים
            context.storeLogsData("Found " + TOTAL_FOUND.getValue() + " files in folder matching the filter.");
            if (FILES_LIST.getItem().isEmpty()) {
                context.updateLogDataAndSummeryLine("There are no files in the given folder path");
                result = StepResult.WARNING;
            }
            else {
                context.updateLogDataAndSummeryLine("The Collect Files In Folder operation was performed successfully");
                result = StepResult.SUCCESS;
            }
        }

        DataInFlowExecution outputExecution1 = createDataInFlowExecution(context,"FILES_LIST", FILES_LIST);
        DataInFlowExecution outputExecution2 =createDataInFlowExecution(context,"TOTAL_FOUND", TOTAL_FOUND.getValue());
        context.storeDataValue("FILES_LIST", outputExecution1);
        context.storeDataValue("TOTAL_FOUND", outputExecution2);
        return result;
    }

    public void createListOfFiles(File[] files, IntWrapper TOTAL_FOUND, FileListData FILES_LIST, String filter) {
        if (files != null) {
            for (File file : files) {
                if (file.isFile()){ // is a file
                    if (filter != null) { //is a file and we have a filter
                        if (filter.equals(getFileExtension(file))) {
                            FileData newFile = new FileData(file.getPath());
                            FILES_LIST.getItem().add(newFile);
                        }
                    }else {
                        FILES_LIST.getItem().add(new FileData(file.getPath()));
                    }
                    TOTAL_FOUND.setValue(TOTAL_FOUND.getValue()+1);
                }
            }
        }
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }


}
