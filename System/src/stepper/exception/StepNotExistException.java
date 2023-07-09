package stepper.exception;

public class StepNotExistException extends RuntimeException{
    public StepNotExistException(String flowName, String stepName) {
        super("The xml file is invalid. Flow " + flowName + " failed. The " + stepName + " step is not in the list of steps");
    }
}