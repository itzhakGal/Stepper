package stepper.exception;


public class NameAndTypeDoNotMatchException extends RuntimeException {
    public NameAndTypeDoNotMatchException(String flowName, String inputName, String dataName) {
        super("Flow " + flowName + " failed. The value already exists with the same name " + inputName + " but with a different type " + dataName);
    }
}