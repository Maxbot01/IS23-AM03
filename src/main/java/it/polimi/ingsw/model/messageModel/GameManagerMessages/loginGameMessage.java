package it.polimi.ingsw.model.messageModel.GameManagerMessages;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameLobby;

import java.util.HashMap;

public class loginGameMessage extends GameManagerMessage{
    public HashMap<GameLobby, Game> currentGames;
    public loginGameMessage(HashMap<GameLobby, Game> currentGames){
        this.currentGames = currentGames;
    }
}
