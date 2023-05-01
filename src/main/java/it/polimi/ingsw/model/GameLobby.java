package it.polimi.ingsw.model;

import it.polimi.ingsw.model.modelSupport.Player;

import java.util.ArrayList;

public class GameLobby extends GameObservable {

    private String ID;
    private String host;
    private ArrayList<Player> players;

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public String getID(){
        return ID;
    }
}
