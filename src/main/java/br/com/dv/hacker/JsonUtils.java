package br.com.dv.hacker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonUtils {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private JsonUtils() {
    }

    public static void printJson(Object object) {
        System.out.println(convertObjectToJson(object));
    }

    public static String convertObjectToJson(Object object) {
        return gson.toJson(object);
    }

}
