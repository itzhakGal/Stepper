package utilsDesktopApp;

import java.util.ArrayList;
import java.util.List;

public class DTOListFlowsDetails {

    List<DTOFlowDetails> dtoFlowDetailsList;

    public DTOListFlowsDetails()
    {
        this.dtoFlowDetailsList = new ArrayList<>();
    }

    public List<DTOFlowDetails> getDtoFlowDetailsList() {
        return dtoFlowDetailsList;
    }
}
