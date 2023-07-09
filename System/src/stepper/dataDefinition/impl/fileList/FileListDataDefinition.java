package stepper.dataDefinition.impl.fileList;

import stepper.dataDefinition.api.AbstractDataDefinition;

public class FileListDataDefinition extends AbstractDataDefinition {
    public FileListDataDefinition() {
        super("List", false, FileListData.class);
    }

}