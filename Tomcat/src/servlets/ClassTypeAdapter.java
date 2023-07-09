package servlets;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ClassTypeAdapter extends TypeAdapter<Class<?>> {

    @Override
    public void write(JsonWriter out, Class<?> value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.getName());
    }

    @Override
    public Class<?> read(JsonReader in) throws IOException {
        throw new UnsupportedOperationException("Deserialization is not supported for java.lang.Class");
    }
}

