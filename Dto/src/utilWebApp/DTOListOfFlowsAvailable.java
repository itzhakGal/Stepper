package utilWebApp;

import java.util.List;

public class DTOListOfFlowsAvailable {

    List<String> allFlowInTheSystem;

    public DTOListOfFlowsAvailable(List<String> allFlowInTheSystem)
    {
        this.allFlowInTheSystem =allFlowInTheSystem;
    }

    public List<String> getAllFlowInTheSystem() {
        return allFlowInTheSystem;
    }

    public void setAllFlowInTheSystem(List<String> allFlowInTheSystem) {
        this.allFlowInTheSystem = allFlowInTheSystem;
    }
}
