package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;

import java.util.ArrayList;

public class GameLobby extends GameObservable {

    private String ID;
    private String host;
    private int numOfPlayers;
    private ArrayList<String> players;

    public ArrayList<String> getPlayers(){
        return players;
    }

    public String getID(){
        return ID;
    }

    GameLobby(String ID, String host, int numOfPlayers){
        this.ID = ID;
        this.host = host;
        this.numOfPlayers = numOfPlayers;
    }

    public void addPlayer(String player) throws LobbyFullException {
        if(players.size() + 1 > numOfPlayers){
            throw new LobbyFullException();
        }else{
            players.add(player);
            super.notifyObserver(player, new LobbyInfoMessage(ID, host, numOfPlayers, players));
        }
    }

    public void setNumOfPlayers(int numOfPlayers){
        this.numOfPlayers = numOfPlayers;
    }

}
