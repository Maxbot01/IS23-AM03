package it.polimi.ingsw.client;

import com.google.gson.*;
import it.polimi.ingsw.model.CommonGoals.Strategy.CommonGoalStrategy;
import it.polimi.ingsw.model.CommonGoals.Strategy.TriangularGoalStrategy;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.FinishedGameMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.InitStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedCardsMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedColumnsMessage;

import java.lang.reflect.Type;

public class MessageSerializer {

    private final Gson gson;

    public MessageSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageDeserializer());
        gson = gsonBuilder.create();
    }


    public String serialize(Message message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("messageType", message.getClass().getSimpleName());
        jsonObject.add("messageData", gson.toJsonTree(message));
        return jsonObject.toString();
    }

    public Message deserialize(String json) {
        return gson.fromJson(json, Message.class);
    }

    private static class MessageDeserializer implements JsonDeserializer<Message> {
        @Override
        public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String messageType = jsonObject.get("messageType").getAsString();
            JsonObject messageData = jsonObject.get("messageData").getAsJsonObject();
            switch (messageType) {
                case "InitStateMessage":
                    return new Gson().fromJson(messageData, InitStateMessage.class);
                case "FinishedGameMessage":
                    return new Gson().fromJson(messageData, FinishedGameMessage.class);
                case "SelectedCardsMessage":
                    return new Gson().fromJson(messageData, SelectedCardsMessage.class);
                case "SelectedColumnsMessage":
                    return new Gson().fromJson(messageData, SelectedColumnsMessage.class);
                case "NetworkMessage":
                    return new Gson().fromJson(messageData, NetworkMessage.class);

                // Aggiungere altri casi per i diversi tipi di messaggio che si vogliono deserializzare
                default:
                    throw new JsonParseException("Unknown message type: " + messageType);
            }
        }
    }

}