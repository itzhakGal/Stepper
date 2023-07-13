package stepper.dataDefinition.impl.list;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListData<T extends Object> implements Serializable
{
    private List<T> item;
    public ListData()
    {
        item = new ArrayList<T>();
    }

    public ListData(ArrayList<T> item)
    {
        this.item = item;
    }
    public void setItem(List<T> item) {
        this.item = item;
    }

    public List<T> getItem() {
        return item;
    }


    public String toString()
    {
        String rerurnVal = "";
        int counter = 1;
        for (Object value : item) {
            rerurnVal = rerurnVal +"Item number: " + counter + ". " + value.toString() + "\n";
            counter++;
        }
        if(item.size() == 0)
            rerurnVal="There are no values in the list.";

        return rerurnVal;
    }

}
