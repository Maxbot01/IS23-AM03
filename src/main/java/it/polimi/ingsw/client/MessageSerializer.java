package it.polimi.ingsw.client;

import com.google.gson.*;
import it.polimi.ingsw.model.CommonGoals.Strategy.CommonGoalStrategy;
import it.polimi.ingsw.model.CommonGoals.Strategy.TriangularGoalStrategy;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This class is responsible for serializing and deserializing messages exchanged between the client and the server.
 */
public class MessageSerializer {

    private final Gson gson;

    /**
     * Constructs a new instance of the MessageSerializer class.
     */
    public MessageSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageDeserializer());
        gson = gsonBuilder.create();
    }

    /**
     * Serializes a message to a JSON string.
     * @param message The message to serialize.
     * @param toPlayer The recipient of the message.
     * @param ID The ID of the message.
     * @return The serialized message as a JSON string.
     */
    public String serialize(Message message, String toPlayer, String ID) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("messageType", message.getClass().getSimpleName());
        jsonObject.addProperty("toPlayerORtoUid", toPlayer);
        jsonObject.addProperty("id", ID);
        jsonObject.add("messageData", gson.toJsonTree(message));
        return jsonObject.toString();
    }

    /**
     * Deserializes a message from a JSON string.
     * @param json The JSON string to deserialize.
     * @return The deserialized message.
     */
    public Message deserialize(String json) {
        return gson.fromJson(json, Message.class);
    }

    /**
     * Gets the ID of the match from a JSON string.
     * @param json The JSON string to parse.
     * @return The ID of the match.
     */
    public String getMatchID(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        return jsonObject.get("id").getAsString();
    }

    /**
     * Deserializes a list of player names from a JSON string.
     * @param json The JSON string to deserialize.
     * @return A list of player names.
     */
    public ArrayList<String> deserializeToPlayersList(String json) {
        ArrayList<String> toPlayersList = new ArrayList<String>();
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        toPlayersList.add(jsonObject.get("toPlayerORtoUid").getAsString());
        return toPlayersList;
    }

    private static class MessageDeserializer implements JsonDeserializer<Message> {
        //WE KNOW THAT THE DESERIALIZER WILL BE CALLED BY THE CLIENT SO WE CAN CHECK ID
        /**
         * Deserializes a JSON element into a {@link Message} object.
         *
         * @param jsonElement The JSON element to deserialize.
         * @param type The type of the object to deserialize.
         * @param jsonDeserializationContext The JSON deserialization context.
         * @return A {@link Message} object.
         * @throws JsonParseException If the JSON element cannot be deserialized into a {@link Message} object.
         */
        @Override
        public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String messageType = jsonObject.get("messageType").getAsString();
            String toPlayer = jsonObject.get("toPlayerORtoUid").getAsString();
            String id = jsonObject.get("id").getAsString();
            if(!(toPlayer.equals(ClientManager.userUID) || toPlayer.equals(ClientManager.userNickname))){
                //not directed to this client, return a null
                //System.out.println("not directed to me, got " + toPlayer + " but have "+ ClientManager.userUID + " | " + ClientManager.userNickname);
                return null;
            }
            JsonObject messageData = jsonObject.get("messageData").getAsJsonObject();
            switch (messageType) {
                case "InitStateMessage":
                    return new Gson().fromJson(messageData, InitStateMessage.class);
                case "LobbyInfoMessage":
                    return new Gson().fromJson(messageData, LobbyInfoMessage.class);
                case "ErrorMessage":
                    return new Gson().fromJson(messageData, ErrorMessage.class);
                case "loginGameMessage":
                    return new Gson().fromJson(messageData, loginGameMessage.class);
                case "FinishedGameMessage":
                    return new Gson().fromJson(messageData, FinishedGameMessage.class);
                case "SelectedCardsMessage":
                    return new Gson().fromJson(messageData, SelectedCardsMessage.class);
                case "SelectedColumnsMessage":
                    return new Gson().fromJson(messageData, SelectedColumnsMessage.class);
                case "NetworkMessage":
                    return new Gson().fromJson(messageData, NetworkMessage.class);
                case "GameStateMessage":
                    return new Gson().fromJson(messageData, GameStateMessage.class);


                //TODO: add other cases for other message types that you want to deserialize

                // Aggiungere altri casi per i diversi tipi di messaggio che si vogliono deserializzare
                default:
                    throw new JsonParseException("Unknown message type: " + messageType);
            }
        }
    }
}
