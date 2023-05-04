package it.polimi.ingsw.client;

import com.google.gson.*;
import it.polimi.ingsw.model.messageModel.Message;

import java.lang.reflect.Type;
import java.util.Map;

public class SerializeDynamicClass {

    /**
     * Converts an object into JSON format.
     *
     * @param object the object to be converted
     * @return a string representing the JSON format of the object
     */
    public static String SerializeToJson(Object object) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("classe", object.getClass().getName());
        jsonObject.add("oggetto", gson.toJsonTree(object));
        return jsonObject.toString();
    }
}
/*
//crea una classe serializer personalizzata per Message e le sue sottoclassi
    class MessageSerializer implements JsonSerializer<Message> {
        @Override
        public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
            //crea un JsonObject vuoto e aggiungi il nome della classe come campo
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("className", src.getClass().getName());

            //aggiungi le altre proprietà dell'oggetto, utilizzando Gson per la serializzazione
            JsonElement otherProperties = context.serialize(src, src.getClass());
            for (Map.Entry<String, JsonElement> entry : otherProperties.getAsJsonObject().entrySet()) {
                jsonObject.add(entry.getKey(), entry.getValue());
            }

            return jsonObject;
        }
    }}
}
        */