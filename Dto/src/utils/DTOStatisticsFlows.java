package utils;

import stepper.statistics.StatisticsCalculatorFlowsImp;

import java.util.HashMap;
import java.util.Map;

public class DTOStatisticsFlows
{
    private Map<String, Integer> executions = new HashMap<>();
    private Map<String, Long> totalTimes = new HashMap<>();

    public DTOStatisticsFlows(StatisticsCalculatorFlowsImp statisticsCalculator)
    {
        this.executions= statisticsCalculator.getExecutions();
        this.totalTimes = statisticsCalculator.getTotalTimes();
    }

    public Map<String, Long> getTotalTimes() {
        return totalTimes;
    }

    public Map<String, Integer> getExecutions() {
        return executions;
    }
}
