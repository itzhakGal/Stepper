package utilWebApp;

import java.util.ArrayList;
import java.util.List;

public class ListDTOMandatoryInputsWeb {

    private List<DTOMandatoryInputsWeb> listDtoMandatoryInput;

    public ListDTOMandatoryInputsWeb(List<DTOMandatoryInputsWeb> listDtoMandatoryInput)
    {
        this.listDtoMandatoryInput = listDtoMandatoryInput;
    }

    public List<DTOMandatoryInputsWeb> getListDTOMandatoryInputsWeb() {
        return listDtoMandatoryInput;
    }

    public void setListDTOMandatoryInputsWeb(List<DTOMandatoryInputsWeb> dtoListDTOMandatoryInputsWeb) {
        this.listDtoMandatoryInput = dtoListDTOMandatoryInputsWeb;
    }

}
