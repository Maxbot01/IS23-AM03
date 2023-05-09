package it.polimi.ingsw.model.messageModel.GameManagerMessages;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameLobby;

import java.util.HashMap;
import java.util.List;

public class loginGameMessage extends GameManagerMessage{
    public List<String> availableGamesId;
    public HashMap<String, List<String>> gamesPlayers;
    public String username;
    public loginGameMessage(List<String> availableGamesId, HashMap<String, List<String>> gamesPlayers, String username){
        this.availableGamesId = availableGamesId;
        this.gamesPlayers = gamesPlayers;
        this.username = username;
    }
}
