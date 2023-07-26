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

public class PropertiesExporterStep extends AbstractStepDefinition {

    public PropertiesExporterStep() {
        super("Properties Exporter", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, "Source data", DataDefinitionRegistry.RELATION, false,""));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Properties export result", DataDefinitionRegistry.STRING, false,""));
    }


    @Override
    public StepResult invoke(ExecutionContextInterface context)
    {
        String str = "";
        RelationData SOURCE= context.getDataValue("SOURCE", RelationData.class, this);
        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();

        boolean flag =true;
        StepResult result = null;
        context.storeLogsData("About to process "+ (SOURCE.getNumberOfLines()-1) + " lines of data");

        if(SOURCE.getNumberOfLines() == 1)
        {
            context.updateLogDataAndSummeryLine("The given table is empty");
            result = StepResult.WARNING;
            flag =false;
        }

        str = createFileProperties(SOURCE);
        String[] lines = str.split("\n");
        int numLines = lines.length/3;
        context.storeLogsData("Extracted total of " + numLines);

        if(flag)
        {
            context.updateLogDataAndSummeryLine("The Properties Exporter operation was performed successfully");
            result = StepResult.SUCCESS;
        }
        LocalTime localEndTime = LocalTime.now();
        context.storeTotalTimeStep(localStartTime, localEndTime, startTime);
        DataInFlowExecution outputExecution = createDataInFlowExecution(context, "RESULT", str);
        context.storeDataValue("RESULT", outputExecution);
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
  

    public String createFileProperties(RelationData SOURCE)
    {
        String str = "";
        List<String> headers = SOURCE.getTable().get(0);

        for (int i = 0; i < SOURCE.getTable().size(); i++) {
            List<String> row = SOURCE.getTable().get(i);
            for (int j = 0; j < row.size(); j++) {
                String key = "Row-" + (i+1) + ". " + headers.get(j);
                String value = row.get(j);
                str += key + " = " + value + "\n";
            }
        }
        return str;
    }
}
