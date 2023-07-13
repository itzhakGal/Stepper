package stepper.dataDefinition.impl.fileList;

import stepper.dataDefinition.impl.file.FileData;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FileListData implements Serializable
{
    private List<FileData> item;

    public FileListData() {
        item = new ArrayList<FileData>();
    }

    public FileListData(ArrayList<FileData> item) {
        this.item =item;
    }

    public void setItem(List<FileData> item) {
        this.item = item;
    }

    public List<FileData> getItem() {
        return item;
    }

    public String toString()
    {
        String rerurnVal = "";
        int counter = 1;
        for (FileData fileData : item) {
            rerurnVal = rerurnVal + "Item number: " + counter + ". " + fileData.toString() + "\n";
            counter++;
        }
        return rerurnVal;
    }
}
