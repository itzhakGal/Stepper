package stepper.dataDefinition.impl.Enumerator;

import java.util.HashSet;
import java.util.Set;

public class EnumeratorData {

    //GET, PUT, POST, DELETE, ZIP, UNZIP
    private final Set<String> enumerator = new HashSet<>();

    public EnumeratorData() {
    }

    public Set<String> getEnumerator() {
        return this.enumerator;
    }

    public void add(String str) {
        enumerator.add(str);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        boolean isFirst = true;
        for (String str : enumerator) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append(str);
            isFirst = false;
        }

        return sb.toString();
    }

    public String getAllMembers() {
        StringBuilder sb = new StringBuilder();

        boolean isFirst = true;
        for (String str : enumerator) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append(str);
            isFirst = false;
        }

        return sb.toString();
    }
}
