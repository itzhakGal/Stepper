package stepper.dataDefinition.impl.mapping;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MappingData <car,cdr>  implements Serializable
{
    private Map<car,cdr> pairs;
    public MappingData()
    {
        pairs = new HashMap<car,cdr>();
    }

    public Map<car, cdr> getPairs() {
        return pairs;
    }

    public void setPairs(Map<car, cdr> pairs) {
        this.pairs = pairs;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<car,cdr> entry : pairs.entrySet()) {
            sb.append("The number of files deleted successfully: " + entry.getKey() + "\n");
            sb.append("The amount of files that failed to delete: " + entry.getValue() + "\n" );
        }
        return sb.toString();
    }
}
