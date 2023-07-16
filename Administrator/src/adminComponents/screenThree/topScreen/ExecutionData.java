package adminComponents.screenThree.topScreen;

import java.util.UUID;

public class ExecutionData {

    private String flowName;
    private String resultExecutions;
    private String startDate;
    private String userName;
    private UUID flowId;

    public ExecutionData(String flowName, String resultExecutions, String startDate, UUID flowId, String userName)
    {
        this.flowName = flowName;
        this.resultExecutions = resultExecutions;
        this.startDate = startDate;
        this.flowId = flowId;
        this.userName =userName;
    }

    public String getFlowName() {
        return flowName;
    }

    public String getResultExecutions() {
        return resultExecutions;
    }

    public String getStartDate() {
        return startDate;
    }
    public UUID getFlowId() {
        return flowId;
    }

    public String getUserName() {
        return userName;
    }
}
