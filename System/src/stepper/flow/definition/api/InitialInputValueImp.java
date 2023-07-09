package stepper.flow.definition.api;


import xmlReaderJavaFX.schema.generated.STInitialInputValue;

public class InitialInputValueImp implements InitialInputValue{

    protected String inputName;

    protected String initialValue;


    public InitialInputValueImp(STInitialInputValue initialInputValue)
    {
        this.initialValue = initialInputValue.getInitialValue();
        this.inputName = initialInputValue.getInputName();
    }
    @Override
    public String getInitialValue() {
        return initialValue;
    }
    @Override
    public String getInputName() {
        return inputName;
    }
}
