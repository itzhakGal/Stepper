package stepper.dataDefinition.impl.file;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileData implements Serializable
{
    private String fileName;
    private File item;


    public FileData(String str) {
        fileName = str;
        item = null;
    }

    public FileData(String pathFather, String newName) {

        item = new File(pathFather, newName);
        fileName = item.getName();
    }
    public void setItem(File item) {
        this.item = item;
    }
    public File getItem() {
        return item;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return item.getPath();
    }

    public String toString() {
        return fileName;
    }

}
