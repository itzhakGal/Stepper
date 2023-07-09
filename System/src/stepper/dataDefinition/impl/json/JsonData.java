package stepper.dataDefinition.impl.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonData {
    private String json;

    public JsonData(String json) {
        this.json = json;
    }

    public JsonData() {
        //this.json = json;
    }

    public String getJson() {
        return json;
    }

    /*public String toString() {
        return "JsonData: " + json;
    }*/

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(json);
        return jsonString;
    }

}
