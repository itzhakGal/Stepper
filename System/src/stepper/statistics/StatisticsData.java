package stepper.statistics;

import java.io.Serializable;

public interface StatisticsData extends Serializable {
     StatisticsCalculatorFlowsImp getStatisticsFlow();
     StatisticsCalculatorStepImpl getStatisticsStep();
}
