package consoleUI;

public enum eMainMenuOption {
    READ_FILE_XML("Reading System Information From File"),
    FLOW_DEFINITION("Introducing Flow Definition"),
    FLOW_ACTION("Flow Activation And Execution"),
    PAST_ACTION("Display Full Details Of Past Activation"),
    STATISTICS("statistics"),
    LOAD_DATA("Load data from file"),
    STORE_DATA("Store data to file"),
    LOG_OFF("Log Off From System");
    public String message;
    private eMainMenuOption(String message) {
        this.message = message;
    }

    public String toString() {
        return this.message;
    }

}
