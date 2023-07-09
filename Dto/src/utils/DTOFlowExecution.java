package utils;

import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.DataInFlow;
import stepper.step.api.DataNecessity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DTOFlowExecution
{
    private List<DTOInputExecution> inputsExecution;
    private int amountOfMandatoryInputs = 0;
    private UUID flowIdRerun;
    private String flowName;


    public DTOFlowExecution(FlowDefinition flowDefinition)
    {
        this.flowName = flowDefinition.getName();
        this.inputsExecution = new ArrayList<>();
        this.flowIdRerun = null;


        for (Map.Entry<String, DataInFlow> data: flowDefinition.getFreeInputs().entrySet())
        {
            inputsExecution.add(new DTOInputExecution(data.getValue()));
            if(data.getValue().getNecessity() == DataNecessity.MANDATORY)
                setAmountOfMandatoryInputs(amountOfMandatoryInputs+1);
        }
    }

    public UUID getFlowIdRerun() {
        return flowIdRerun;
    }

    public void setFlowIdRerun(UUID flowIdRerun) {
        this.flowIdRerun = flowIdRerun;
    }

    public List<DTOInputExecution> getInputsExecution() {
        return inputsExecution;
    }

    public DTOInputExecution getInputByFinalName(String finalName)
    {
        for (DTOInputExecution input: inputsExecution)
        {
            if(input.getFinalName().equals(finalName))
                return input;
        }
        return null;
    }

    public String getFlowName() {
        return flowName;
    }

    public int getAmountOfMandatoryInputs() {
        return amountOfMandatoryInputs;
    }

    public void setAmountOfMandatoryInputs(int amountOfMandatoryInputs) {
        this.amountOfMandatoryInputs = amountOfMandatoryInputs;
    }
}
