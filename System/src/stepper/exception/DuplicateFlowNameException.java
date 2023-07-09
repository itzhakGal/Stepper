package stepper.exception;

public class DuplicateFlowNameException extends RuntimeException{
    public DuplicateFlowNameException() {
        super("The xml file is invalid, there are duplicate names in the list of flow");
    }
}