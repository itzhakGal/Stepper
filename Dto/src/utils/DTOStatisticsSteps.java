package utils;

import stepper.statistics.StatisticsCalculatorStepImpl;

import java.util.HashMap;
import java.util.Map;

public class DTOStatisticsSteps
{
    private Map<String, Integer> executions = new HashMap<>();
    private Map<String, Long> totalTimes = new HashMap<>();

    public DTOStatisticsSteps(StatisticsCalculatorStepImpl statisticsStep)
    {
        this.executions= statisticsStep.getExecutions();
        this.totalTimes = statisticsStep.getTotalTimes();
    }

    public Map<String, Long> getTotalTimes() {
        return totalTimes;
    }

    public Map<String, Integer> getExecutions() {
        return executions;
    }
}
