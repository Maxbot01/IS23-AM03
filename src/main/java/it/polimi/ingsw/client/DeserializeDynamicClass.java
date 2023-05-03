package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class DeserializeDynamicClass{

    //ritorna un oggetto di tipo Object, che poi verrà CASTATO al tipo corretto
    public static Object fromJson(String json) throws ClassNotFoundException {
        Gson gson = new Gson();
        JsonElement root = new JsonParser().parse(json);
        if (root.isJsonObject()) {
            JsonObject jsonObj = root.getAsJsonObject();
            String className = jsonObj.get("classe").getAsString();
            JsonElement jsonElement = jsonObj.get("oggetto");
            try {
                Class<?> clazz = Class.forName(className);
                Object instance = gson.fromJson(jsonElement, clazz);
                return instance;
            } catch (ClassNotFoundException e) {
                throw e;
            }
        } else {
            throw new IllegalArgumentException("Il JSON non è un oggetto");
        }
    }


}

