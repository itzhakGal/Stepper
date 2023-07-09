package stepper.dataDefinition.impl.relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationData implements Serializable
{
    private List<List<String>> table;
    private int numberOfLines;
    public RelationData(String... args)
    {
        numberOfLines =0;
        table = new ArrayList<List<String>>();
        addRow(args);
    }

    public List<List<String>> getTable() {
        return table;
    }

    public void addRow(String... values)
    {
        ArrayList<String> row = new ArrayList<>();
        for (String value : values) {
            row.add(value);
        }
        table.add(row);
        numberOfLines++;
    }


    public int getNumberOfLines() {
        return numberOfLines;
    }

    @Override
    public String toString()
    {
        return "The number of lines in the table is: " + numberOfLines + "\n" + "The Names of columns are: \n"
                + printRow(table,0);
    }

    public String printRow(List<List<String>> table, int indexOfRow)
    {
        String rowValues = "";
        List<String> row = table.get(indexOfRow);
        for (String value : row)
        {
            rowValues = rowValues + value + "\n";
        }
        return rowValues;
    }

    // Other methods and properties of the RelationTable class...
}


