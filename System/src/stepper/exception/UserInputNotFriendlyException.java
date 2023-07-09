package stepper.exception;

public class UserInputNotFriendlyException extends RuntimeException {
    public UserInputNotFriendlyException(String flowName, String inputName) {
        super("The xml file is invalid.  Flow " + flowName + " failed: mandatory input " + inputName + " must be user friendly");
    }
}