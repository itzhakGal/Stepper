package exception;

public class InvalidSelectingCommandException extends RuntimeException{
    public InvalidSelectingCommandException() {
        super("Incorrect option from choosing the order of commands that is needed\n");
    }
}
