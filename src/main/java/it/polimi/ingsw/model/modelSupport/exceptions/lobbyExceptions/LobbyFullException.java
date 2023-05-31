package it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions;

public class LobbyFullException extends Exception{
    public String info;
    public LobbyFullException(String info){this.info = info;}
}
