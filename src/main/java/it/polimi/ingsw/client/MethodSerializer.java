package it.polimi.ingsw.client;
import com.google.gson.*;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.FinishedGameMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.InitStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedCardsMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedColumnsMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;


private final Gson gson;

public class MethodSerializer {
    public MethodSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Method.class, new MethodSerializer.MethodDeserializer());
        gson = gsonBuilder.create();
    }

    public String serialize(Method method, String toPlayer, String ID) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("methodname", method.getMethodName();
        jsonObject.addProperty("toPlayer", toPlayer);
        jsonObject.addProperty("id", ID);
        return jsonObject.toString();
    }

    public Method deserialize(String json) {
        return gson.fromJson(json, Method.class);
    }

    public String getMatchID(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        return jsonObject.get("id").getAsString();
    }

    private static class MethodDeserializer implements JsonDeserializer<Method> {
        @Override
        public Method deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String methodname = jsonObject.get("methodname").getAsString();
            String toPlayer = jsonObject.get("toPlayer").getAsString();
            String id = jsonObject.get("id").getAsString();
            switch (methodname) {
                case "setCredentials":
                    return new Gson().fromJson(messageData, InitStateMessage.class);
                case "selectGame":
                    return new Gson().fromJson(messageData, FinishedGameMessage.class);
                case "createGame":
                    return new Gson().fromJson(messageData, SelectedCardsMessage.class);
                case "sendAck":
                    return new Gson().fromJson(messageData, SelectedColumnsMessage.class);
                case "startMatch":
                    return new Gson().fromJson(messageData, NetworkMessage.class);
                case "selectedCards":
                    return new Gson().fromJson(messageData, NetworkMessage.class);
            }


        }
    }
}
