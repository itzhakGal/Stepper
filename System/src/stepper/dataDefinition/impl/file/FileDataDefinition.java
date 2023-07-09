package stepper.dataDefinition.impl.file;

import stepper.dataDefinition.api.AbstractDataDefinition;

public class FileDataDefinition extends AbstractDataDefinition {
    public FileDataDefinition() {
        super("File", false, FileData.class);
    }
}
