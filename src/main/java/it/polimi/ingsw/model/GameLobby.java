package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorType;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.ArrayList;

/**
 * Represents a game lobby.
 * This class extends GameObservable and implements Serializable and Remote interfaces.
 */
public class GameLobby extends GameObservable implements Serializable, Remote {

    private final String HostID;
    private String ID;
    private String host;
    private int numOfPlayers;
    private ArrayList<String> players;
    private ArrayList<String> readyPlayers;

    /**
     * Gets the list of players in the game lobby.
     *
     * @return the list of players
     */
    public ArrayList<String> getPlayers() {
        return players;
    }

    /**
     * Gets the host of the game lobby.
     *
     * @return the host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Adds a player to the list of ready players.
     *
     * @param player the player to add
     */
    public void addReadyPlayer(String player) {
        readyPlayers.add(player);
        System.out.println("ADDED" + player);
    }

    /**
     * Starts the match if conditions are met.
     *
     * @param user the user requesting to start the match
     * @param stub the remote interface for the user
     */
    public void startMatch(String user, MyRemoteInterface stub) {
        if (players.size() < numOfPlayers) {
            super.notifyObserver(user, new ErrorMessage(ErrorType.notEnoughPlayers,
                    "There aren't enough players to start the match"), false, "-");
        } else if (!user.equals(host)) {
            super.notifyObserver(user, new ErrorMessage(ErrorType.onlyHostCanStartMatch,
                    "Only the host can start the match"), false, "-");
        } else if (!(numOfPlayers == readyPlayers.size() + 1)) {
            super.notifyObserver(user, new ErrorMessage(ErrorType.notEveryoneReady,
                    "Not everyone is ready"), false, "-");
        } else if (numOfPlayers == players.size()) {
            GameManager.getInstance().createMatchFromLobby(ID, players);
        }
    }

    /**
     * Gets the ID of the game lobby.
     *
     * @return the ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Constructs a new GameLobby instance.
     *
     * @param ID           the ID of the game lobby
     * @param host         the host of the game lobby
     * @param numOfPlayers the number of players allowed in the game lobby
     * @param HostID       the ID of the host
     */
    GameLobby(String ID, String host, int numOfPlayers, String HostID) {
        this.HostID = HostID;
        this.ID = ID;
        this.host = host;
        this.numOfPlayers = numOfPlayers;
        readyPlayers = new ArrayList<>();
        players = new ArrayList<>();
        players.add(host);
        super.notifyObserver(host, new LobbyInfoMessage(ID, host, numOfPlayers, players, false), false, ID);
    }

    /**
     * Adds a player to the game lobby.
     *
     * @param player the player to add
     * @throws LobbyFullException if the game lobby is full
     */
    public void addPlayer(String player) throws LobbyFullException {
        if (players.size() + 1 > numOfPlayers) {
            throw new LobbyFullException("The selected lobby is full");
        } else {
            players.add(player);
            super.notifyAllObservers(players, new LobbyInfoMessage(ID, host, numOfPlayers, players, false), true, this.ID);
        }
    }

    /**
     * Sets the number of players allowed in the game lobby.
     *
     * @param numOfPlayers the number of players
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Marks the game lobby as killed.
     */
    public void killLobby() {
        this.ID = "TOMB";
    }

    /**
     * Checks if the game lobby is killed.
     *
     * @return true if the game lobby is killed, false otherwise
     */
    public boolean isKilled() {
        return this.ID.equals("TOMB");
    }
}
