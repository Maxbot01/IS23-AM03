package it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions;

import java.io.Serializable;

public class LobbyFullException extends Exception implements Serializable {
    public String info;
    public LobbyFullException(String info){this.info = info;}
}
