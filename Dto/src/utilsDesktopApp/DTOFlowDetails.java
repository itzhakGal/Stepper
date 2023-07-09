package utilsDesktopApp;


import stepper.flow.definition.api.FlowDefinition;

public class DTOFlowDetails {

    private String name;
    private String description;
    private int amountOfSteps;
    private int amountOfInput;
    private int amountOfContinuation;

    public DTOFlowDetails(FlowDefinition flowDefinition)
    {
        this.name =  flowDefinition.getName();
        this.amountOfSteps = flowDefinition.getSteps().size();
        this.description = flowDefinition.getDescription();
        this.amountOfInput = flowDefinition.getFreeInputs().size();
        this.amountOfContinuation= flowDefinition.getContinuationsList().size();
    }

    public String getName() {
        return name;
    }

    public int getAmountOfContinuation() {
        return amountOfContinuation;
    }

    public int getAmountOfSteps() {
        return amountOfSteps;
    }

    public int getAmountOfInput() {
        return amountOfInput;
    }

    public String getDescription() {
        return description;
    }
}

