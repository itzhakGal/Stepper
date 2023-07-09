package stepper.exception;

public class DuplicateOutputNameException extends RuntimeException {
    public DuplicateOutputNameException(String flowName, String name) {
        super("The xml file is invalid. Flow " + flowName + " failed: output name must be unique, there are duplicate name in output " +name);
    }
}