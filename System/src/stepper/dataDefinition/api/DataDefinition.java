package stepper.dataDefinition.api;

import java.io.Serializable;

public interface DataDefinition extends Serializable {
    String getName();
    boolean isUserFriendly();
    Class<?> getType();
}
