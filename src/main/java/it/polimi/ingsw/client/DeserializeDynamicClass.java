/**
 * This package contains classes for the client side of a networked game.
 */
package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.messageModel.Message;

/**
 * This class provides a static method for deserializing JSON strings into dynamic Java objects.
 */
public class DeserializeDynamicClass {

    /**
     * This method deserializes a JSON string into a Java object of unknown type,
     * which can then be cast to the correct type.
     *
     * @param json the JSON string to deserialize
     * @return the deserialized Java object
     * @throws ClassNotFoundException if the class specified in the JSON string is not found
     * @throws IllegalArgumentException if the JSON string is not an object
     */
    public static Object DeserializeFromJson(String json) throws ClassNotFoundException {
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
            throw new IllegalArgumentException("The JSON string is not an object");
        }
    }
}
