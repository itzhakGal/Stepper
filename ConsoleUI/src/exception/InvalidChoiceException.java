package exception;

public class InvalidChoiceException extends RuntimeException {
    public InvalidChoiceException(String errorMessage) {
        super(errorMessage +"\n");
    }
}
