package it.polimi.ingsw.client;

import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.helpers.*;
import com.google.gson.Gson;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;

import java.util.ArrayList;

public class VirtualGameManagerSerializer {
    private String method;
    private Object[] args;

    public VirtualGameManagerSerializer(String method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    //getter and setter
    public String getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public static String serializeMethod(VirtualGameManagerSerializer virtualGameManagerSerializer) {
        Gson gson = new Gson();
        return gson.toJson(virtualGameManagerSerializer);
    }

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

        }
    }
}
