package stepper.dataDefinition.impl.json;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonData {
    private JsonElement json;

    public JsonData() {
    }

    public JsonElement getJson() {
        return json;
    }

    public void setJson(JsonElement json) {
        this.json = json;
    }

    public String toString() {
        return getFormattedJson(json);
    }

    private String getFormattedJson(JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return "null";
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
            if (primitive.isString()) {
                return "\"" + primitive.getAsString() + "\"";
            } else {
                return primitive.toString();
            }
        } else if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{");
            boolean first = true;
            for (java.util.Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if (!first) {
                    stringBuilder.append(", ");
                } else {
                    first = false;
                }
                stringBuilder.append("\"").append(key).append("\": ").append(getFormattedJson(value));
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        } else if (jsonElement.isJsonArray()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            for (int i = 0; i < jsonElement.getAsJsonArray().size(); i++) {
                if (i > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(getFormattedJson(jsonElement.getAsJsonArray().get(i)));
            }
            stringBuilder.append("]");
            return stringBuilder.toString();
        } else {
            // In case of unknown or unsupported types, return an empty string.
            return "";
        }
    }
}
