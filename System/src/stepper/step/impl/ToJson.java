package stepper.step.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.dataDefinition.impl.Enumerator.EnumeratorData;
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

public class ToJson extends AbstractStepDefinition {
    public ToJson() {
        super("To Json", true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl("CONTENT", DataNecessity.MANDATORY, "Content", DataDefinitionRegistry.STRING, false));

        //step outputs
        addOutput(new DataDefinitionDeclarationImpl("JSON", DataNecessity.NA, "Json representation", DataDefinitionRegistry.JSON, false));
    }

    public static JsonObject convertToJson(String text) {
        Gson gson = new Gson();
        JsonObject jsonObject = null;

        try {
            jsonObject = gson.fromJson(text, JsonObject.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public StepResult invoke(ExecutionContextInterface context) {

        String CONTENT = context.getDataValue("CONTENT", String.class, this);
        JsonData JSON = new JsonData();
        //לא החזרתי את הטיפוס המתאים לשאול את אביעד!!

        StepResult stepResult;
        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();


        context.storeLogsData("Content is JSON string. Converting it to json");

        JsonObject result = convertToJson(CONTENT);
        if (result != null) {
            stepResult = StepResult.SUCCESS;
            context.updateLogDataAndSummeryLine("The conversion was done successfully");
        } else {
            String errorMessage = "Content is not a valid JSON representation:";
            stepResult = StepResult.FAILURE;
            context.updateLogDataAndSummeryLine(errorMessage);
        }


        LocalTime localEndTime = LocalTime.now();
        context.storeTotalTimeStep(localStartTime, localEndTime, startTime);

        DataInFlowExecution outputExecution = createDataInFlowExecution(context,"JSON", JSON);
        context.storeDataValue("JSON", outputExecution);

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

}
