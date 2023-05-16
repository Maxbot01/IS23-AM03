package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorType;
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

    public void startMatch(String user){
        System.out.println("startMatch from GameLobby");
        if(user.equals(host) && numOfPlayers == players.size()){
            GameManager.getInstance().createMatchFromLobby(ID, players);
        }else if (numOfPlayers < players.size()){
            //TODO: send wrong request error
            super.notifyObserver(user, new ErrorMessage(ErrorType.notEnoughPlayers), false, "-");
        }else if(!user.equals(host)){
            super.notifyObserver(user, new ErrorMessage(ErrorType.onlyHostCanStartMatch), false, "-");
        }
    }

    public String getID(){
        return ID;
    }

    GameLobby(String ID, String host, int numOfPlayers){
        this.ID = ID;
        this.host = host;
        this.numOfPlayers = numOfPlayers;
        players = new ArrayList<>();
        players.add(host);
        super.notifyObserver(host, new LobbyInfoMessage(ID, host, numOfPlayers, players), false, ID); //TODO: non dovrebbe essere true inLobbyorGame?
        //TODO: Send a message to all observers not in game (or don't show it) with the new available games (remember that there's the method lookForNewGames)
    }

    public void addPlayer(String player) throws LobbyFullException {
        if(players.size() + 1 > numOfPlayers){
            throw new LobbyFullException();
        }else{
            players.add(player);
            super.notifyAllObservers(players, new LobbyInfoMessage(ID, host, numOfPlayers, players), true, this.ID);
            //super.notifyObserver(player, new LobbyInfoMessage(ID, host, numOfPlayers, players), true, this.ID);
        }
    }

    public void setNumOfPlayers(int numOfPlayers){
        this.numOfPlayers = numOfPlayers;
    }

    public void killLobby() {
        this.ID = "TOMB";
    }

    public boolean isKilled(){
        return this.ID.equals("TOMB");
    }
}
