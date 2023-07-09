package stepper.exception;

public class CustomMappingIsIncorrectOrderException extends RuntimeException {
    public CustomMappingIsIncorrectOrderException(String flowName, String sourceStepName, String targetStepName) {
        super("The xml file is invalid. Flow " + flowName + " failed: The step " + sourceStepName + " is a step later to the step " + targetStepName);
    }
}
