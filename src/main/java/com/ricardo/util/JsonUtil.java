package com.ricardo.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class JsonUtil {
    // One single instance for the entire App - Gson is thread safe
//    private static final Gson GSON = new Gson();


    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.toString());
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return LocalDate.parse(json.getAsString());
                }
            }).create();

    // Another private constructor so nobody can instantiate a 'new' instance of this class
    // This is for pure utility classes
    private JsonUtil() {
    }

    ;

    // Deserialize: JSON string -> Java Object
    // Used in handlers to parse the request body
    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    // Serialize: Java Object -> JSON string
    // Used in handlers to build the response body
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }
}
