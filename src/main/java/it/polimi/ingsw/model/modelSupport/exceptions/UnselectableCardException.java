package it.polimi.ingsw.model.modelSupport.exceptions;

import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;

import java.io.Serializable;

public class UnselectableCardException extends Exception implements Serializable {
    public String info;
    public UnselectableCardException(String info){
        this.info = info;
    };
}
