package stepper.flow.execution;

public enum FlowExecutionResult {

    SUCCESS, WARNING, FAILURE;
    public String getDescription()
    {
        switch (this) {
            case SUCCESS:
                return "SUCCESS";
            case FAILURE:
                return "FAILURE";
            case WARNING:
                return "WARNING";
            default:
                throw new AssertionError("Unknown op: " + this);
        }
    }
}

