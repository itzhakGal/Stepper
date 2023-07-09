package stepper.exception;

public class DifferentTypeException extends RuntimeException
{
    public DifferentTypeException() {
        super("The entered value does not match the type");
    }
}