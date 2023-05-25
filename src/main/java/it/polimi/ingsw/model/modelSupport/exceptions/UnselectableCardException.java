package it.polimi.ingsw.model.modelSupport.exceptions;

import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;

public class UnselectableCardException extends Exception{
    public UnselectableCardException( String msg){
        super(msg);
    }
    public UnselectableCardException(){

    };
}
