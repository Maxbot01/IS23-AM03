package it.polimi.ingsw.model;

import java.io.Serializable;
import java.rmi.Remote;

public enum GameStateType implements Serializable, Remote {
    IN_PROGRESS,
    LAST_ROUND,
    FINISHED
}
