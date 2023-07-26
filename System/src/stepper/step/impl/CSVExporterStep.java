package stepper.step.impl;

import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.dataDefinition.impl.relation.RelationData;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.time.LocalTime;
import java.util.List;

public class CSVExporterStep extends AbstractStepDefinition {

    public CSVExporterStep() {
        super("CSV Exporter", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, "Source data", DataDefinitionRegistry.RELATION, false,""));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "CSV export result", DataDefinitionRegistry.STRING, false,""));
    }

    @Override
    public StepResult invoke(ExecutionContextInterface context)
    {
        String csv = "";
        boolean flag =true;
        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();

        StepResult result = null;

        RelationData SOURCE = context.getDataValue("SOURCE", RelationData.class, this);

        context.storeLogsData("About to process "+ (SOURCE.getNumberOfLines()-1) + " lines of data");
        if(SOURCE.getNumberOfLines() == 1)
        {
            context.updateLogDataAndSummeryLine("The given table is empty");
            result = StepResult.WARNING;
            flag =false;
        }

        csv = createCsvString(SOURCE);
        if(flag)
        {
            context.updateLogDataAndSummeryLine("The CSV Exporter operation was performed successfully");
            result= StepResult.SUCCESS;
        }

        DataInFlowExecution outputExecution = createDataInFlowExecution(context,"RESULT", csv);
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

    public String createCsvString(RelationData SOURCE)
    {
        String csv = "";
        // Add column names to CSV string
        List<String> headers = SOURCE.getTable().get(0);
        for (int i = 0; i < headers.size(); i++) {
            csv += headers.get(i);
            if (i < headers.size() - 1) {
                csv += ",";
            }
        }
        csv += "\n";
        // Add data rows to CSV string
        for (int i = 1; i < SOURCE.getTable().size(); i++) {
            List<String> row = SOURCE.getTable().get(i);
            for (int j = 0; j < row.size(); j++) {
                csv += row.get(j);
                if (j < row.size() - 1) {
                    csv += ",";
                }
            }
            csv += "\n";
        }
        return csv;
    }

}
