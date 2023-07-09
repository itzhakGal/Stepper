package utilWebApp;

import java.util.ArrayList;
import java.util.List;

public class DTOListFullDetailsPastRunWeb {

    List<DTOFullDetailsPastRunWeb> dtoListFullDetailsPastRun;

    public DTOListFullDetailsPastRunWeb()
    {
        this.dtoListFullDetailsPastRun = new ArrayList<>();
    }

    public List<DTOFullDetailsPastRunWeb> getDtoListFullDetailsPastRun() {
        return dtoListFullDetailsPastRun;
    }

    public void setDtoListFullDetailsPastRun(List<DTOFullDetailsPastRunWeb> dtoListFullDetailsPastRun) {
        this.dtoListFullDetailsPastRun = dtoListFullDetailsPastRun;
    }
}
