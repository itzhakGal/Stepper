package stepper.exception;

public class CustomMappingException extends RuntimeException {
    public CustomMappingException(String flowName, String sourceData, String targetData) {
        super("The xml file is invalid. Flow "+ flowName +" failed: trying to connect different types of information: " + sourceData + ", "+ targetData);
    }
}