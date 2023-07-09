package stepper.step.api;

public enum StepResult {
    SUCCESS, WARNING, FAILURE;

    public String getDescription()
    {
        switch (this) {
            case SUCCESS:
                return "SUCCESS";
            case WARNING:
                return "WARNING";
            case FAILURE:
                return "FAILURE";
            default:
                return "";
        }
    }
}
