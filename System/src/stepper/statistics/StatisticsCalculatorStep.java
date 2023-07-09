package stepper.statistics;

import java.io.Serializable;
import java.util.Map;

public interface StatisticsCalculatorStep extends Serializable {
     void updateStatisticsStep(String name, long executionTime);
     Map<String, Integer> getExecutions();
     Map<String, Long> getTotalTimes();
}
