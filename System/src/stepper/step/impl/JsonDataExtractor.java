package stepper.step.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.dataDefinition.impl.json.JsonData;
import stepper.dataDefinition.impl.string.StringWrapper;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;
import java.time.LocalTime;


public class JsonDataExtractor extends AbstractStepDefinition {
    public JsonDataExtractor() {
        super("Json Data Extractor", true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl("JSON", DataNecessity.MANDATORY, "Json source", DataDefinitionRegistry.JSON, false,""));
        addInput(new DataDefinitionDeclarationImpl("JSON_PATH", DataNecessity.MANDATORY, "data", DataDefinitionRegistry.STRING, false,""));

        //step outputs
        addOutput(new DataDefinitionDeclarationImpl("VALUE", DataNecessity.NA, "Data value", DataDefinitionRegistry.STRING, false,""));
    }

    @Override
    public StepResult invoke(ExecutionContextInterface context) {

        JsonData JSON = context.getDataValue("JSON", JsonData.class, this);
        String JSON_PATH = context.getDataValue("JSON_PATH", String.class, this);

        String VALUE = "";
        //StringWrapper RESULT= new StringWrapper("");
        StepResult stepResult;
        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();

        //stepResult = extractInfoFromJson(context, RESULT, JSON.getJson() , JSON_PATH);
        try {
            String[] dataArray = JSON_PATH.split("\\|");
            StringBuilder value = new StringBuilder();
            //StringBuilder jsonPathStr = new StringBuilder();

            for (String path : dataArray) {

                value.append(JsonPath.read(JSON.getJson().toString(), path.trim()).toString()).append(", ");
            }
            context.updateLogDataAndSummeryLine("Extracting data " + JSON_PATH + ". Value: " + value);

            if (value.toString().equals(""))
                context.updateLogDataAndSummeryLine("No value found for json path jsonPath");

            String stringValue = value.toString();
            if (stringValue.endsWith(", ")) {
                // Remove the trailing comma
                stringValue = stringValue.substring(0, stringValue.length() - 2);
            }

            VALUE = stringValue;
            stepResult = StepResult.SUCCESS;
        } catch (Exception e) {
            context.updateLogDataAndSummeryLine(e.getMessage());
            stepResult = StepResult.FAILURE;
        }

        LocalTime localEndTime = LocalTime.now();
        context.storeTotalTimeStep(localStartTime, localEndTime, startTime);
        //VALUE = RESULT.getValue();

        DataInFlowExecution outputExecution = createDataInFlowExecution(context,"VALUE", VALUE);
        context.storeDataValue("VALUE", outputExecution);

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

    public static StepResult extractInfoFromJson(ExecutionContextInterface context, StringWrapper RESULT, JsonElement jsonElement, String JSON_PATH) {

        StringBuilder extractedInfo = new StringBuilder();
        String[] expressions = JSON_PATH.split("\\|");
        StepResult stepResult = null;
        for (String expression : expressions) {
            if (!expression.startsWith("$")) {
                stepResult =StepResult.FAILURE;
                context.updateLogDataAndSummeryLine("Invalid JSONPath expression: " + expression);
                continue;
            }

            try {
                Object result = JsonPath.read(jsonElement.toString(), expression);
                if (result != null) {

                    extractedInfo.append(result.toString()).append(", ");
                }
                else
                    context.storeLogsData("No value found for json path " + JSON_PATH);
                stepResult = StepResult.SUCCESS;
            } catch (PathNotFoundException e) {
                stepResult =StepResult.FAILURE;
                context.updateLogDataAndSummeryLine("Path not found for expression: " + expression);
            }
        }

        // Remove the trailing comma and space if there is any extracted information
        if (extractedInfo.length() > 0) {
            extractedInfo.setLength(extractedInfo.length() - 2);
            context.storeLogsData("Extracting data " + JSON_PATH + ". Value: " + extractedInfo.toString());
        }

        RESULT.setValue(extractedInfo.toString());
        return stepResult;
    }
}
