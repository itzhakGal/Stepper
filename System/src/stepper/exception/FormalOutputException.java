package stepper.exception;

public class FormalOutputException extends RuntimeException {
    public FormalOutputException(String flowName, String outputName) {
        super("The xml file is invalid. Flow " + flowName + " failed: flow output contains an information detail that does not exist called " + outputName);
    }
}