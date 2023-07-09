package stepper.statistics;

import java.io.Serializable;
import java.util.Map;

public interface StatisticsCalculatorFlows extends Serializable {
     void updateStatisticsFlow(String name, long executionTime);
     Map<String, Integer> getExecutions();
     Map<String, Long> getTotalTimes();

}
