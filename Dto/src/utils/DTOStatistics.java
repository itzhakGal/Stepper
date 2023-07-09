package utils;

import stepper.statistics.StatisticsCalculatorFlowsImp;
import stepper.statistics.StatisticsCalculatorStepImpl;

public class DTOStatistics {

    private DTOStatisticsFlows statisticsFlows;
    private DTOStatisticsSteps statisticsSteps;

    public DTOStatistics(StatisticsCalculatorStepImpl statisticsStep, StatisticsCalculatorFlowsImp statisticsFlows)
    {
        this.statisticsFlows = new DTOStatisticsFlows(statisticsFlows);
        this.statisticsSteps = new DTOStatisticsSteps(statisticsStep);
    }

    public DTOStatisticsFlows getStatisticsFlows() {
        return statisticsFlows;
    }

    public DTOStatisticsSteps getStatisticsSteps() {
        return statisticsSteps;
    }
}
