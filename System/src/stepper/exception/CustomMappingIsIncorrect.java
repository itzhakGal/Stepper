package stepper.exception;

public class CustomMappingIsIncorrect extends RuntimeException{
    public CustomMappingIsIncorrect(String flowName, String name) {
    super("The xml file is invalid. Flow " + flowName + " failed: " + name + " does not exist within the flow");
    }
}