package utilWebApp;

public class DTOMandatoryInputsWeb {

    private String labelValue;
    private String value;
    private String type;

    public DTOMandatoryInputsWeb(String type, String labelValue, String value)
    {
        this.labelValue = labelValue;
        this.type = type;
        this.value = value;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
