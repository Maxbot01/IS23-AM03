package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.List;

public class GameLobby {

    private static String gameID;
    private ArrayList<Player> players;
    private int numOfPlayers;
    private String serverAddress;
    private Player host;
    private Game game;

    public void startMatch() {
    }

    public Player getHost(){
        return host;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public void setNumOfPlayers(int numOfPlayers){
        this.numOfPlayers = numOfPlayers;
    }
}