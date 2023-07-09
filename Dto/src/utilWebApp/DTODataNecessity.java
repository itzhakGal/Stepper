package utilWebApp;

public enum DTODataNecessity {
    NA, MANDATORY, OPTIONAL;

    public String getDescription() {
        switch (this) {
            case MANDATORY:
                return "Mandatory";
            case OPTIONAL:
                return "Optional";
            case NA:
                return "Na";
            default:
                throw new AssertionError("Unknown op: " + this);

        }
    }

}