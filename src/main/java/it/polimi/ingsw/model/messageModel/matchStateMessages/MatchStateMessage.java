package it.polimi.ingsw.model.messageModel.matchStateMessages;

import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.messageModel.Message;

public abstract class MatchStateMessage extends Message {

    protected GameStateType gameState;
    protected String matchID;

    MatchStateMessage(GameStateType gameState, String matchID){
        this.gameState = gameState;
        this.matchID = matchID;
    }

}
