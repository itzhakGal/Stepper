package subComponents.screenThree.topScreen;

import java.util.UUID;

public class ExecutionData {

    private String flowName;
    private String resultExecutions;
    private String startDate;
    private UUID flowId;

    public ExecutionData(String flowName, String resultExecutions, String startDate, UUID flowId)
    {
        this.flowName = flowName;
        this.resultExecutions = resultExecutions;
        this.startDate = startDate;
        this.flowId = flowId;
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
}
