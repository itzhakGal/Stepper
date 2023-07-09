package utils;

import java.util.List;

public class DTOString {

    private String  validResult;
    private boolean flag;

    public DTOString(String  validResult, boolean flag)
    {
        this.validResult = validResult;
        this.flag = flag;
    }

    public String getValidResult() {
        return validResult;
    }

    public boolean isFlag() {
        return flag;
    }
}
