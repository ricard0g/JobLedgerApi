package com.ricardo.util;

import com.google.gson.Gson;

public class JsonUtil {
    // One single instance for the entire App - Gson is thread safe
    private static final Gson GSON = new Gson();

    // Another private constructor so nobody can instantiate a 'new' instance of this class
    // This is for pure utility classes
    private JsonUtil() {};

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
