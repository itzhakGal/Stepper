package stepper.exception;

public class InvalidDataInInitialInputs extends RuntimeException {
    public InvalidDataInInitialInputs(String flowName, String inputName) {
        super("The xml file is invalid. Flow "+ flowName +" failed: it means information " + inputName + " that does not exist. ");
    }
}