package stepper.exception;

public class MandatoryInputDuplicateNameException extends RuntimeException {
    public MandatoryInputDuplicateNameException(String flowName, String inputName, String seenType, String type) {
        super("The xml file is invalid. Flow " + flowName + " failed: found duplicate name " + inputName + " with different types: " + seenType + " and " + type );
    }
}