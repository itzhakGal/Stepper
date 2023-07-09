package stepper.exception;

public class InvalidDataInContinuation extends RuntimeException {
    public InvalidDataInContinuation(String flowName, String data) {
        super("The xml file is invalid. Flow "+ flowName +" failed: it means information " + data + " that does not exist. ");
    }
}