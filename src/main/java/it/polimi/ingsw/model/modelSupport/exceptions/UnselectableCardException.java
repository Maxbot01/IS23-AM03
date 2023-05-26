package it.polimi.ingsw.model.modelSupport.exceptions;

import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;

public class UnselectableCardException extends Exception{
    public String info;
    public UnselectableCardException(String info){
        this.info = info;
    };
}
