package stepper.step.api;

import java.util.ArrayList;
import java.util.List;

public class StepsNamesImpl implements StepsNames
{
    private List<String> names;

    public StepsNamesImpl()
    {
        this.names = new ArrayList<>();
        names.add("Spend Some Time");
        names.add("Collect Files In Folder");
        names.add("Files Deleter");
        names.add("Files Renamer");
        names.add("Files Content Extractor");
        names.add("CSV Exporter");
        names.add("Properties Exporter");
        names.add("File Dumper");
        names.add("Properties Exporter");
        names.add("File Dumper");
        names.add("Zipper");
        names.add("Command Line");
        names.add("HTTP Call");
        names.add("To Json");
        names.add("Json Data Extractor");
    }

    @Override
    public List<String> getNames() {
        return names;
    }
}
