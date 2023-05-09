package it.polimi.ingsw.client;

import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import com.google.gson.Gson;
import java.util.ArrayList;

/**
 * A class for serializing and deserializing VirtualGameManager method calls.
 */
public class VirtualGameManagerSerializer {
    private String method;
    private Object[] args;

    /**
     * Constructs a VirtualGameManagerSerializer object with the given method and arguments.
     * @param method the name of the method to be called
     * @param args the arguments for the method
     */
    public VirtualGameManagerSerializer(String method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    /**
     * Returns the name of the method.
     * @return the name of the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Returns the arguments for the method.
     * @return the arguments for the method
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * Sets the name of the method.
     * @param method the name of the method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Sets the arguments for the method.
     * @param args the arguments for the method
     */
    public void setArgs(Object[] args) {
        this.args = args;
    }

    /**
     * Serializes a VirtualGameManagerSerializer object to JSON.
     * @param virtualGameManagerSerializer the object to serialize
     * @return the JSON string representation of the object
     */
    public static String serializeMethod(VirtualGameManagerSerializer virtualGameManagerSerializer) {
        Gson gson = new Gson();
        return gson.toJson(virtualGameManagerSerializer);
    }

    /**
     * Deserializes a JSON string representation of a VirtualGameManagerSerializer object and calls the corresponding
     * VirtualGameManager method with the arguments.
     * @param jsonString the JSON string representation of the object
     */
    public static void deserializeMethod(String jsonString) {
        GameManager gameManager = GameManager.getInstance();
        Gson gson = new Gson();
        VirtualGameManagerSerializer virtualGameManagerSerializer = gson.fromJson(jsonString, VirtualGameManagerSerializer.class);

        switch(virtualGameManagerSerializer.getMethod()) {
            case "ping":
                String uid = (String) virtualGameManagerSerializer.getArgs()[0];
                gameManager.ping(uid);
                break;
            case "setCredentials":
                String username = (String) virtualGameManagerSerializer.getArgs()[0];
                String password = (String) virtualGameManagerSerializer.getArgs()[1];
                uid = (String) virtualGameManagerSerializer.getArgs()[2];
                gameManager.setCredentials(username, password, uid);
                break;
            case "selectGame":
                String gameID = (String) virtualGameManagerSerializer.getArgs()[0];
                String user = (String) virtualGameManagerSerializer.getArgs()[1];
                gameManager.selectGame(gameID, user);
                break;
            case "createGame":
                Double numPlayersDouble = (Double) virtualGameManagerSerializer.getArgs()[0];
                int numPlayers = numPlayersDouble.intValue();
                String user1 = (String) virtualGameManagerSerializer.getArgs()[1];
                gameManager.createGame(numPlayers, user1);
                break;
            case "sendAck":
                gameManager.sendAck();
                break;
            case "startMatch":
                String ID = (String) virtualGameManagerSerializer.getArgs()[0];
                String user2 = (String) virtualGameManagerSerializer.getArgs()[1];
                gameManager.startMatch(ID, user2);
                break;
            case "selectedCards":
                String user3 = (String) virtualGameManagerSerializer.getArgs()[1];
                String gameID1 = (String) virtualGameManagerSerializer.getArgs()[2];
                gameManager.selectedCards((ArrayList<Pair<Integer, Integer>>) virtualGameManagerSerializer.getArgs()[0], user3, gameID1);
                break;
            case "selectedColumn":
                String user4 = (String) virtualGameManagerSerializer.getArgs()[2];
                String gameID2 = (String) virtualGameManagerSerializer.getArgs()[3];
                gameManager.selectedColumn((ArrayList<BoardCard>) virtualGameManagerSerializer.getArgs()[0], (Integer)
                                                            virtualGameManagerSerializer.getArgs()[1], user4, gameID2);

        }
    }
}
