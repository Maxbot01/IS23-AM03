package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorType;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a game lobby.
 */
public class GameLobby extends GameObservable implements Serializable, Remote {

    private final String HostID;
    private String ID;
    private String host;
    private int numOfPlayers;
    private ArrayList<String> players;

    /**
     * Returns the list of players in the game lobby.
     *
     * @return The list of players.
     */
    public ArrayList<String> getPlayers(){
        return players;
    }

    /**
     * Returns the host of the game lobby.
     *
     * @return The host of the game lobby.
     */
    public String getHost(){
        return this.host;
    }

    /**
     * Starts the match in the game lobby.
     *
     * @param user The user who wants to start the match.
     * @param stub The remote interface for communication.
     */
    public void startMatch(String user, MyRemoteInterface stub) {
        System.out.println("startMatch from GameLobby");
        if(user.equals(host) && numOfPlayers == players.size()){
            GameManager.getInstance().createMatchFromLobby(ID, players);
        }else if (players.size() < numOfPlayers){
            super.notifyObserver(user, new ErrorMessage(ErrorType.notEnoughPlayers,"There aren't enough players to start the match"), false, "-");
        }else if(!user.equals(host)){
            // TODO: This check is not needed, it is done inside the cli, maybe it's needed for the gui, check before removing
            super.notifyObserver(user, new ErrorMessage(ErrorType.onlyHostCanStartMatch,"Only the host can start the match"), false, "-");
        }
    }

    /**
     * Returns the ID of the game lobby.
     *
     * @return The ID of the game lobby.
     */
    public String getID(){
        return ID;
    }

    /**
     * Constructs a new game lobby.
     *
     * @param ID           The ID of the game lobby.
     * @param host         The host of the game lobby.
     * @param numOfPlayers The number of players in the game lobby.
     * @param HostID       The ID of the host.
     */
    GameLobby(String ID, String host, int numOfPlayers, String HostID){
        this.HostID = HostID;
        this.ID = ID;
        this.host = host;
        this.numOfPlayers = numOfPlayers;
        players = new ArrayList<>();
        players.add(host);
        super.notifyObserver(host, new LobbyInfoMessage(ID, host, numOfPlayers, players, false), false, ID);
        // TODO: Send a message to all observers not in game (or don't show it) with the new available games (remember that there's the method lookForNewGames)
    }

    /**
     * Adds a player to the game lobby.
     *
     * @param player The player to add.
     * @throws LobbyFullException if the lobby is already full.
     */
    public void addPlayer(String player) throws LobbyFullException {
        if(players.size() + 1 > numOfPlayers){
            throw new LobbyFullException("The selected lobby is full");
        }else{
            players.add(player);
            super.notifyAllObservers(players, new LobbyInfoMessage(ID, host, numOfPlayers, players,false), true, this.ID);
            //super.notifyObserver(player, new LobbyInfoMessage(ID, host, numOfPlayers, players), true, this.ID);
        }
    }
    /**
     * Sets the number of players in the lobby.
     *
     * @param numOfPlayers the number of players to set
     */
    public void setNumOfPlayers(int numOfPlayers){
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Kills the lobby by setting the ID to "TOMB".
     */
    public void killLobby() {
        this.ID = "TOMB";
    }

    /**
     * Checks if the lobby is killed.
     *
     * @return true if the lobby is killed, false otherwise
     */
    public boolean isKilled(){
        return this.ID.equals("TOMB");
    }
}
