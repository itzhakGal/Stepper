package clientComponents.screenThree.topScreen;

import java.util.UUID;

public class ExecutionData {

    private String flowName;
    private String resultExecutions;
    private String startDate;
    private UUID flowId;
    private String userName;
    private String isManager;

    public ExecutionData(String flowName, String resultExecutions, String startDate, UUID flowId, String userName,String isManager)
    {
        this.flowName = flowName;
        this.resultExecutions = resultExecutions;
        this.startDate = startDate;
        this.flowId = flowId;
        this.userName = userName;
        this.isManager = isManager;
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
    public void setResultExecutions(String resultExecutions) {
        this.resultExecutions = resultExecutions;
    }
    public String getIsManager() {
        return isManager;
    }
    public void setIsManager(String isManager) {
        this.isManager = isManager;
    }
}
