package utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * The ZonedDateTimeAdapter class is a TypeAdapter implementation for serializing and deserializing ZonedDateTime objects.
 * It is used by the Gson library to convert ZonedDateTime objects to and from JSON.
 *
 * The write method is responsible for writing a ZonedDateTime object to a JsonWriter.
 * If the ZonedDateTime object is null, it writes a null value to the JsonWriter.
 * Otherwise, it writes the string representation of the ZonedDateTime object to the JsonWriter.
 *
 * The read method is responsible for reading a ZonedDateTime object from a JsonReader.
 * If the next token in the JsonReader is null, it returns null.
 * Otherwise, it parses the string value from the JsonReader and returns a ZonedDateTime object.
 *
 * This class is part of the utils package.
 */
public class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {
    @Override
    public void write(JsonWriter jsonWriter, java.time.ZonedDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localDateTime.toString());
        }
    }

    @Override
    public ZonedDateTime read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        } else {
            return ZonedDateTime.parse(jsonReader.nextString());
        }
    }
}
