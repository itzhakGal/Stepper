package stepper.exception;

public class ContinuationFlowNameException extends RuntimeException {
    public ContinuationFlowNameException(String flowName, String targetFlowName) {
        super("The xml file is invalid. Flow " + flowName + " failed. The " + targetFlowName + " flow is not in the list of flows");
    }
}