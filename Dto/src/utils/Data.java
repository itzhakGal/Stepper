package utils;


public class Data {
    private  Class<?> itemType;
    private Object item;
    private String userString;

    public Data(Class<?> itemType, Object item, String userString)
    {
        this.itemType = itemType;
        this.item=item;
        this.userString = userString;
    }

    public Object getItem() {
        return item;
    }

    public String getUserString() {
        return userString;
    }

    public Class<?> getItemType() {
        return itemType;
    }
}
