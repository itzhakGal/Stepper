package exception;

public class InvalidSelectingCommandOrderException extends RuntimeException {
    public InvalidSelectingCommandOrderException() {
        super("Incorrect option from choosing the order of commands that is needed.\n" +
                "Commands number 1 or 7 were not executed. File not provided\n");
    }
}