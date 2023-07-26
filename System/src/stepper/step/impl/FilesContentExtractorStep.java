package stepper.step.impl;

import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.dataDefinition.impl.file.FileData;
import stepper.dataDefinition.impl.fileList.FileListData;
import stepper.dataDefinition.impl.relation.RelationData;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;

public class FilesContentExtractorStep extends AbstractStepDefinition {

    public FilesContentExtractorStep() {
        super("Files Content Extractor", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", DataNecessity.MANDATORY, "Files to extract", DataDefinitionRegistry.FILE_LIST, false,""));
        addInput(new DataDefinitionDeclarationImpl("LINE", DataNecessity.MANDATORY, "Line number to extract", DataDefinitionRegistry.NUMBER, false,""));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("DATA", DataNecessity.NA, "Data extraction", DataDefinitionRegistry.RELATION, false,""));
    }

    @Override
    public StepResult invoke(ExecutionContextInterface context)
    {
        FileListData filesList = context.getDataValue("FILES_LIST", FileListData.class,this);
        int line = context.getDataValue("LINE", Integer.class, this);
        RelationData DATA = new RelationData("Serial Number", "The name of the original file","The textual information retrieved from the file in the relevant line");
        int serialNumber = 1;
        boolean flag = true;
        StepResult result;
        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();

        if(filesList.getItem().isEmpty())
        {
            result= StepResult.SUCCESS;
            context.updateLogDataAndSummeryLine("The Files Content Extractor operation was performed successfully, the list of files is empty.");
        }
        else {
            for (FileData file : filesList.getItem()) {
                file.setItem(new File(file.getFileName()));
                String fileName = file.getItem().getName();
                context.storeLogsData("About to start work on file " + fileName);
                int linesInFile = countLines(file.getFileName());
                try
                {
                    if (!file.getItem().exists())
                        throw new IOException();
                    BufferedReader br = new BufferedReader(new FileReader(file.getFileName()));
                    ContentExtractor(br, line, DATA, serialNumber, fileName, linesInFile);

                } catch (IOException e) {
                    flag = false;
                    DATA.addRow(Integer.toString(serialNumber), fileName, " File not found");
                    context.storeLogsData("Problem extracting line number " + line + " from file " + fileName);
                }
                serialNumber++;
            }
            result = updateStatus(context, flag);
        }
        LocalTime localEndTime = LocalTime.now();
        context.storeTotalTimeStep(localStartTime, localEndTime, startTime);
        DataInFlowExecution outputExecution = createDataInFlowExecution(context,"DATA", DATA);
        context.storeDataValue("DATA", outputExecution);
        context.updateStatusStep(result);
        return result;
    }

    public static int countLines(String filename)  {
       try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
           int count = 0;
           while (reader.readLine() != null) {
               count++;
           }
           reader.close();
           return count;
       }catch (IOException e) {
       }

        return 0;
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

    public void ContentExtractor(BufferedReader br, int LINE,RelationData resultTable, int serialNumber,String fileName, int linesInFile  ) throws IOException {
        String line;
        int currentLine = 1;
        if (linesInFile < LINE) {
            resultTable.addRow(Integer.toString(serialNumber), fileName, "Not such line");
        }else {
            while ((line = br.readLine()) != null) {
                if (currentLine == LINE) {
                    resultTable.addRow(Integer.toString(serialNumber), fileName, line);
                    break;
                }
                currentLine++;
            }
        }
    }

    public StepResult updateStatus(ExecutionContextInterface context, boolean flag) {
        StepResult result;
        if (!flag) {
            context.updateLogDataAndSummeryLine("Not all required files have their content extracted successfully");
            result = StepResult.WARNING;
        } else {
            context.updateLogDataAndSummeryLine("All required files have their content extracted successfully");
            result = StepResult.SUCCESS;
        }
        return result;
    }

}
