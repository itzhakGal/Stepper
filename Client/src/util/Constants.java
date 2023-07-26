package util;


import com.google.gson.Gson;

public class Constants {
    // Server resources locations
    public final static int REFRESH_RATE1 = 1000;
    public final static int REFRESH_RATE = 2000;
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Tomcat_Web_exploded";;
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String LIST_FLOWS_DETAILS = FULL_SERVER_PATH + "/available_flows";
    public final static String INTRODUCING_FLOW_DEFINITION = FULL_SERVER_PATH + "/introducing_Flow_Definition";
    public final static String CONTINUATIONS = FULL_SERVER_PATH + "/continuation";
    public final static String DETAILS_FREE_INPUTS = FULL_SERVER_PATH + "/detailsFreeInputs";
    public final static String FIND_VALUE_OF_FREE_INPUT = FULL_SERVER_PATH + "/findValueOfFreeInput";
    public final static String UPDATE_MANDATORY_INPUT = FULL_SERVER_PATH + "/updateMandatoryInput";
    public final static String FLOW_EXECUTION = FULL_SERVER_PATH + "/flowExecution";
    public final static String LIST_FLOWS_EXECUTION = FULL_SERVER_PATH + "/listFlowsExecution";
    public final static String LIST_FLOWS_EXECUTION_CLIENT = FULL_SERVER_PATH + "/listFlowsExecutionClient";
    public final static String RERUN_EXECUTION = FULL_SERVER_PATH + "/inputForRerunExecution";

    public final static String FLOW_EXECUTION_TASK = FULL_SERVER_PATH + "/flowExecutionTask";
    public final static String LIST_CONTINUATION_FLOW_NAME = FULL_SERVER_PATH + "/listContinuationFlowName";
    public final static String FLOW_EXECUTION_RESULT = FULL_SERVER_PATH + "/flowExecutionResult";
    public final static String FLOW_DEFINITION_REFRESHER = FULL_SERVER_PATH + "/flowDefinitionRefresher";
    public final static String USER_DATA_REFRESHER = FULL_SERVER_PATH + "/userDataRefresher";
    public final static String UPDATE_MANDATORY = FULL_SERVER_PATH + "/updateMandatory";
    public final static String LOGIN_OUT_CLIENT = FULL_SERVER_PATH + "/logOutClient";
    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
