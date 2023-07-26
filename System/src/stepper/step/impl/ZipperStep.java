package stepper.step.impl;

import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.dataDefinition.impl.Enumerator.EnumeratorData;
import stepper.dataDefinition.impl.string.StringWrapper;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipperStep extends AbstractStepDefinition {
    public ZipperStep() {
        super("Zipper", false);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, "Source", DataDefinitionRegistry.STRING, true,""));
        addInput(new DataDefinitionDeclarationImpl("OPERATION", DataNecessity.MANDATORY, "Operation type", DataDefinitionRegistry.ENUMERATOR, false, "Zip"));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Zip operation result", DataDefinitionRegistry.STRING, false,""));
    }

    @Override
    public StepResult invoke(ExecutionContextInterface context) {

        String SOURCE = context.getDataValue("SOURCE", String.class, this);
        EnumeratorData OPERATION = context.getDataValue("OPERATION", EnumeratorData.class, this);
        StringWrapper RESULT= new StringWrapper("");
        StepResult stepResult;
        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();

        if(OPERATION.getEnumerator().contains("UNZIP")) {
            context.storeLogsData("About to perform operation UNZIP on source " + SOURCE);
            if (!SOURCE.toLowerCase().endsWith(".zip")) {
                context.updateLogDataAndSummeryLine("File path must have a .zip extension.");
                stepResult = StepResult.FAILURE;
            }else
                stepResult = unzipFile(SOURCE, context, RESULT);
        }
        else {
            context.storeLogsData("About to perform operation ZIP on source " + SOURCE);
            stepResult = zipFile(SOURCE, context, RESULT);
        }
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
        context.updateOutputDataList(outputExecution);
        return outputExecution;
    }


    public static StepResult unzipFile(String filePath, ExecutionContextInterface context, StringWrapper RESULT) {
        File file = new File(filePath);
        String destinationPath = file.getParent();
        StepResult stepResult;

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filePath))) {
            byte[] buffer = new byte[1024];
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String entryName = zipEntry.getName();
                File entryFile = new File(destinationPath + File.separator + entryName);

                if (zipEntry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    new File(entryFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    int length;

                    while ((length = zipInputStream.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }

                    fos.close();
                }

                zipEntry = zipInputStream.getNextEntry();
            }
            context.updateLogDataAndSummeryLine("Unzip operation completed successfully.");
            RESULT.setValue("Unzip operation completed successfully.");
            stepResult = StepResult.SUCCESS;
        } catch (IOException e) {
            context.updateLogDataAndSummeryLine("The UNZIP operation could not be completed successfully");
            RESULT.setValue("The UNZIP operation could not be completed successfully");
            stepResult = StepResult.FAILURE;
        }

        return stepResult;
    }

    public static StepResult zipFile(String sourcePath, ExecutionContextInterface context, StringWrapper RESULT) {
        File file = new File(sourcePath);
        String originalName = file.getName();
        int extensionIndex = originalName.lastIndexOf(".");
        String nameWithoutExtension;
        String zipFileName;
        if (extensionIndex != -1) {
            nameWithoutExtension = originalName.substring(0, extensionIndex);
        } else {
            nameWithoutExtension = originalName;
        }
        zipFileName = nameWithoutExtension + ".zip";
        String destinationPath = file.getParent();
        StepResult stepResult;

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(destinationPath + File.separator + zipFileName))) {
            if (file.isDirectory()) {
                zipDirectory(file.getPath(), file.getName(), zipOutputStream);
            } else {
                zipSingleFile(file, zipOutputStream);
            }
            zipOutputStream.close();
            context.updateLogDataAndSummeryLine("Zip operation completed successfully.");
            RESULT.setValue("Zip operation completed successfully.");
            stepResult = StepResult.SUCCESS;
        } catch (IOException e) {
            context.updateLogDataAndSummeryLine("The ZIP operation could not be completed successfully");
            RESULT.setValue("The ZIP operation could not be completed successfully");
            stepResult = StepResult.FAILURE;
        }
        return stepResult;
    }

    private static void zipDirectory(String directoryPath, String baseName, ZipOutputStream zipOutputStream) throws IOException {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        byte[] buffer = new byte[1024];

        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipDirectory(file.getPath(), baseName + File.separator + file.getName(), zipOutputStream);
                } else {
                    FileInputStream fis = new FileInputStream(file);
                    zipOutputStream.putNextEntry(new ZipEntry(baseName + File.separator + file.getName()));

                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }
                    fis.close();
                }
            }
        }
    }

    private static void zipSingleFile(File file, ZipOutputStream zipOutputStream) throws IOException {
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(file);

        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
        int length;

        while ((length = fis.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length);
        }
        fis.close();
    }

}
