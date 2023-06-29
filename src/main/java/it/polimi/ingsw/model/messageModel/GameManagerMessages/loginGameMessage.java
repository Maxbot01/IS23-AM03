package it.polimi.ingsw.model.messageModel.GameManagerMessages;

import java.util.HashMap;
import java.util.List;

public class loginGameMessage extends GameManagerMessage{

    public HashMap<String, List<String>> gamesPlayers;
    public String username;
    public loginGameMessage(HashMap<String, List<String>> gamesPlayers, String username){

        this.gamesPlayers = gamesPlayers;
        this.username = username;
    }
}
