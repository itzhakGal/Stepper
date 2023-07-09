package stepper.exception;

public class FlowLevelAliasException extends RuntimeException {
    public FlowLevelAliasException(String flowName, String name) {
        super("The xml file is invalid. Flow " + flowName + " failed: " + name + " does not exist within the flow");
    }
}
