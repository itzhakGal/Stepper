package stepper.step.impl;

import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.dataDefinition.impl.json.JsonData;
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
        addInput(new DataDefinitionDeclarationImpl("JSON", DataNecessity.MANDATORY, "Json source", DataDefinitionRegistry.JSON, false));
        addInput(new DataDefinitionDeclarationImpl("JSON_PATH", DataNecessity.MANDATORY, "data", DataDefinitionRegistry.STRING, false));

        //step outputs
        addOutput(new DataDefinitionDeclarationImpl("VALUE", DataNecessity.NA, "Data value", DataDefinitionRegistry.STRING, false));
    }

    @Override
    public StepResult invoke(ExecutionContextInterface context) {

        JsonData JSON = context.getDataValue("JSON", JsonData.class, this);
        String JSON_PATH = context.getDataValue("JSON_PATH", String.class, this);

        String VALUE = "";

        StepResult stepResult;
        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();


        try {
            //  להוריד מהמאמא קבצי JAR

            //Extract the information from the JSON using JSON path
            //stepResult = JsonPath.read(json.getJsonString(), jsonPath).toString();
            context.storeLogsData("Extracting data " + JSON_PATH + ". Value: VALUE");
            context.storeLogsData("No value found for json path <json path>"); // להבין איפה לשים

            // Set the result to success
            stepResult = StepResult.SUCCESS;
        } catch (Exception e) {
            // Error occurred during JSON path extraction
            String errorMessage = "Error extracting data using JSON path: " + e.getMessage();

            // Store the error message in the step result
            stepResult = StepResult.FAILURE;

            // Store the error message in the context logs
            context.updateLogDataAndSummeryLine(errorMessage);
        }



        LocalTime localEndTime = LocalTime.now();
        context.storeTotalTimeStep(localStartTime, localEndTime, startTime);

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

    /*public static String extractInformation(String json, String jsonPath) {
        StringBuilder extractedValues = new StringBuilder();
        String[] expressions = jsonPath.split("\\|");

        for (String expression : expressions) {
            expression = expression.trim();
            if (!expression.startsWith("$")) {
                // Invalid expression, skip to the next one
                continue;
            }

            try {
                Object result = JsonPath.read(json, expression);
                if (result != null) {
                    if (extractedValues.length() > 0) {
                        extractedValues.append(", ");
                    }
                    extractedValues.append(result.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return extractedValues.toString();
    }*/

}
