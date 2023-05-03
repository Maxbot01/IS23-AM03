package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SerializeDynamicClass {

    // Converte un oggetto in formato JSON
    public static String toJson(Object object) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("classe", object.getClass().getName());
        jsonObject.add("oggetto", gson.toJsonTree(object));
        return jsonObject.toString();
    }
}
