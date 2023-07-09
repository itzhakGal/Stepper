package stepper.statistics;

public class StatisticsDataImpl implements StatisticsData{
    private StatisticsCalculatorFlowsImp statisticsFlow;
    private StatisticsCalculatorStepImpl statisticsStep;

    public StatisticsDataImpl() {
        this.statisticsFlow = new StatisticsCalculatorFlowsImp();
        this.statisticsStep = new StatisticsCalculatorStepImpl();
    }

    @Override
    public StatisticsCalculatorFlowsImp getStatisticsFlow() {
        return statisticsFlow;
    }
    @Override
    public StatisticsCalculatorStepImpl getStatisticsStep() {
        return statisticsStep;
    }
}
