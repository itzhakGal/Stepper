package stepper.statistics;

import java.util.HashMap;
import java.util.Map;

public class StatisticsCalculatorFlowsImp implements StatisticsCalculatorFlows
{
    // Map to store the number of times a flow or step is executed
    private Map<String, Integer> executions ;

    // Map to store the total time taken for each flow or step
    private Map<String, Long> totalTimes ;


    public StatisticsCalculatorFlowsImp()
    {
        this.executions= new HashMap<>();
        this.totalTimes = new HashMap<>();
    }

    // Function to update statistics for flows and steps
    @Override
    public void updateStatisticsFlow(String name, long executionTime)
    {
        // Update executions count
        executions.put(name, executions.getOrDefault(name, 0) + 1);

        // Update total time taken
        totalTimes.put(name, totalTimes.getOrDefault(name, 0L) + executionTime);
    }

    @Override
    public Map<String, Integer> getExecutions() {
        return executions;
    }

    @Override
    public Map<String, Long> getTotalTimes() {
        return totalTimes;
    }

}
