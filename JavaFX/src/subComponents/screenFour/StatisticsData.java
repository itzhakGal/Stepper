package subComponents.screenFour;

public class StatisticsData {

    private String name;
    private int executions;
    private double averageExecutionTime;

    public StatisticsData(String flowName, int executions, double averageExecutionTime)
    {
        this.name = flowName;
        this.executions = executions;
        this.averageExecutionTime = averageExecutionTime;
    }
    public String getName() {
        return name;
    }

    public double getAverageExecutionTime() {
        return averageExecutionTime;
    }

    public int getExecutions() {
        return executions;
    }
}
