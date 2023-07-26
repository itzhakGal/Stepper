package stepper.step.impl;

import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.dataDefinition.impl.Enumerator.EnumeratorData;
import stepper.dataDefinition.impl.json.JsonData;
import stepper.dataDefinition.impl.number.IntWrapper;
import stepper.dataDefinition.impl.string.StringWrapper;
import stepper.flow.definition.api.DataInFlow;
import stepper.flow.execution.context.DataInFlowExecution;
import stepper.flow.execution.context.DataInFlowExecutionImp;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;

public class HTTPCall extends AbstractStepDefinition {
    public HTTPCall() {
        super("HTTP Call", false);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl("RESOURCE", DataNecessity.MANDATORY, "Resource Name (include query parameters)", DataDefinitionRegistry.STRING, false,""));
        addInput(new DataDefinitionDeclarationImpl("ADDRESS", DataNecessity.MANDATORY, "Domain:Port", DataDefinitionRegistry.STRING, false,""));
        addInput(new DataDefinitionDeclarationImpl("PROTOCOL", DataNecessity.MANDATORY, "protocol", DataDefinitionRegistry.ENUMERATOR, false,"Protocol"));
        addInput(new DataDefinitionDeclarationImpl("METHOD", DataNecessity.OPTIONAL, "Method", DataDefinitionRegistry.ENUMERATOR, false, "Method"));
        addInput(new DataDefinitionDeclarationImpl("BODY", DataNecessity.OPTIONAL, "Request Body", DataDefinitionRegistry.JSON, false, ""));

        //step outputs
        addOutput(new DataDefinitionDeclarationImpl("CODE", DataNecessity.NA, "Response code", DataDefinitionRegistry.NUMBER, false, ""));
        addOutput(new DataDefinitionDeclarationImpl("RESPONSE_BODY", DataNecessity.NA, "Response body", DataDefinitionRegistry.STRING, false, ""));
    }

    @Override
    public StepResult invoke(ExecutionContextInterface context) {

        String RESOURCE = context.getDataValue("RESOURCE", String.class, this);
        String ADDRESS = context.getDataValue("ADDRESS", String.class, this);
        EnumeratorData PROTOCOL = context.getDataValue("PROTOCOL", EnumeratorData.class, this);
        EnumeratorData METHOD = context.getDataValue("METHOD", EnumeratorData.class, this);
        JsonData BODY = context.getDataValue("BODY", JsonData.class, this);

        IntWrapper CODE = new IntWrapper(0);
        StringWrapper RESPONSE_BODY= new StringWrapper("");

        StepResult stepResult;
        long startTime = System.nanoTime();
        LocalTime localStartTime = LocalTime.now();

        stepResult = sendHttpRequest(context, RESOURCE, ADDRESS, PROTOCOL, METHOD, BODY, CODE, RESPONSE_BODY);

        context.storeLogsData("Received Response. Status code: \n" + CODE);

        LocalTime localEndTime = LocalTime.now();
        context.storeTotalTimeStep(localStartTime, localEndTime, startTime);

        DataInFlowExecution outputExecution1 = createDataInFlowExecution(context,"CODE", CODE.getValue());
        context.storeDataValue("CODE", outputExecution1);

        DataInFlowExecution outputExecution2 = createDataInFlowExecution(context,"RESPONSE_BODY", RESPONSE_BODY.getValue());
        context.storeDataValue("RESPONSE_BODY", outputExecution2);

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

    public static StepResult sendHttpRequest(ExecutionContextInterface context, String RESOURCE, String ADDRESS, EnumeratorData PROTOCOL, EnumeratorData METHOD, JsonData BODY, IntWrapper CODE, StringWrapper RESPONSE_BODY) {
        StepResult stepResult;

        try {
            String urlString;
            if(!RESOURCE.startsWith("/")){
                context.storeLogsData("About to invoke http request <request details: \n" +
                        PROTOCOL  + "://" + METHOD + "/" + ADDRESS + "/" + RESOURCE + "\n");
                urlString = PROTOCOL.getAllMembers() + "://" + ADDRESS + "/" + RESOURCE;
                }
            else {
                context.storeLogsData("About to invoke http request <request details: \n" +
                        PROTOCOL  + "://" + METHOD + "/" + ADDRESS + RESOURCE + "\n");
                urlString = PROTOCOL.getAllMembers() + "://" + ADDRESS + RESOURCE;
            }
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(METHOD.getAllMembers());

            if (METHOD.getAllMembers().equals("POST") || METHOD.getAllMembers().equals("PUT")) {
                connection.setDoOutput(true);
                // Convert JsonElement to JSON string
                String jsonBody = BODY.toString();
                connection.getOutputStream().write(jsonBody.getBytes("UTF-8"));
            }
            int responseCode = connection.getResponseCode();

            BufferedReader reader;
            if (responseCode >= 200 && responseCode < 400) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                stepResult = StepResult.SUCCESS;
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                stepResult = StepResult.FAILURE;
            }

            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
            reader.close();

            CODE.setValue(responseCode);
            RESPONSE_BODY.setValue(responseBody.toString());

        } catch (Exception e) {
            CODE.setValue(-1);
            RESPONSE_BODY.setValue(e.getMessage());
            stepResult = StepResult.FAILURE;
            context.updateLogDataAndSummeryLine("The request could not be sent.");
        }

        return stepResult;
    }

}
