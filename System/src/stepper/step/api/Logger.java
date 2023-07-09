package stepper.step.api;

import java.io.Serializable;


public interface Logger extends Serializable {
     String getLogTimeAsString();
     String getLog();
}
