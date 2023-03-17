package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.GameStateType;

public abstract class MatchStateMessage extends Message {

    protected GameStateType gameState;
    protected String matchID;

}
