package it.polimi.ingsw.model;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.ChatMessage;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorType;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;
import it.polimi.ingsw.server.RemoteUserInfo;
import it.polimi.ingsw.server.ServerMain;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Singleton that represents the front-end logic of the server and exposes the endpoint method that every client can call, it manages the requests creating and deleting the games
 */
public class GameManager extends GameObservable implements Serializable, Remote, MyRemoteInterface {

    //this instance is used whenever we want the singleton
    private static GameManager instance;

    //hash map with the game lobby and the relative games
    private HashMap<GameLobby, Game> currentGames;
    private HashMap<String, String> nicknames;
    //private HashMap<String, String> userIDs;
    public static HashMap<String, RemoteUserInfo> userIdentification;
    private HashMap<String, Game> userMatches;

    private HashMap<String, ArrayList<Pair<String, String>>> chats;
    private final HashMap<String,Boolean> playersInLobby; // boolean true means the player is in lobby
    static int PORT = 1099;
    static Message message;

    private static List<String> clients = new ArrayList<>();
    private static Map<String, Message> clientMessages = new HashMap<>();
    private static Map<String, Map<String, Message>> MultiMatchClientMessage = new HashMap<>();
    public static HashMap<String, RemoteUserInfo> remoteUsers = new HashMap<>();
    private String hostID;
    private static Map<String, Message> previousClientMessages = new HashMap<>();

    private boolean flag = false;

    //setter updtate
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return flag;
    }


    /**
     * Constructs a new GameManager instance.
     */
    public GameManager(){
        nicknames = new HashMap<>();
        currentGames = new HashMap<>();
        userMatches = new HashMap<>();
        userIdentification = new HashMap<>();
        playersInLobby = new HashMap<>();
        chats = new HashMap<>();
    }

    /**
     * Gets the map of user identification.
     *
     * @return The map of user identification.
     */
    public synchronized HashMap<String, RemoteUserInfo> getUserIdentification() {
        return userIdentification;
    }

    /**
     * Selects a game lobby with the specified ID and adds the user to the lobby.
     *
     * @param ID   The ID of the game lobby.
     * @param user The username of the user.
     */
    public synchronized void selectGame(String ID, String user) {
        for (GameLobby x : currentGames.keySet()) {
            if (x.getID().equals(ID) && !x.isKilled()) {
                // joins this lobby
                try {
                    x.addPlayer(user);
                    this.playersInLobby.put(user, true);
                    // It notifies the not-in-lobby players that a game has been entered by someone (the players are updated)
                    for (String s : playersInLobby.keySet()) {
                        if (playersInLobby.get(s).equals(false)) {
                            super.notifyObserver(s, new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), user), false, "-");
                        }
                    }
                } catch (LobbyFullException e) {
                    // lobby is full, returns error
                    super.notifyObserver(user, new ErrorMessage(ErrorType.lobbyIsFull, e.info), false, "-");
                }
            }
        }
    }


    /**
     * User has either requested to see messages (fromUser and message null), or add message and get chat messages
     * (gameId, fromUser, null, true, inGame) -> receive all the messages
     * (gameId, fromUser, null, false, inGame) -> receive last 5 messages
     * (gameID, fromUser, message, false, inGame) -> send message and receive last 5 messages
     * @param gameID
     * @param fromUser
     * @param message
     * @param fullChat
     * @param inGame
     */
    public synchronized void receiveChatMessage(String gameID, String fromUser, String message, boolean fullChat, boolean inGame){
        boolean found = false;
        for(GameLobby x: this.currentGames.keySet()){
            if(x.getID().equals(gameID)){
                found = true;
            }else if(x.isKilled() && currentGames.get(x) != null){
                if(currentGames.get(x).getID().equals(gameID)){
                    found = true;
                }
            }
            if(found){
                if(message == null){//We are sending the chat
                    ArrayList<Pair<String, String>> lastFive = new ArrayList<>();
                    if(chats.containsKey(gameID)) {
                        if (!fullChat) {
                            for (int i = chats.get(gameID).size() - 1; i >= 0 && i > chats.get(gameID).size() - 6; i--) {
                                lastFive.add(chats.get(gameID).get(i));
                            }
                            Collections.reverse(lastFive);
                            super.notifyObserver(fromUser, new ChatMessage(lastFive,inGame), true, gameID);
                        } else {
                            super.notifyObserver(fromUser, new ChatMessage(chats.get(gameID),inGame), true, gameID);
                        }
                    }else{//It sent an empty list if there are no messages
                        super.notifyObserver(fromUser,new ChatMessage(lastFive,inGame),true,gameID);
                    }
                }else{//We got a message to add to the chat
                    chats.computeIfAbsent(gameID, k -> new ArrayList<>());
                    Pair<String, String> myPair = new Pair<>(fromUser, message);
                    chats.get(gameID).add(myPair);
                    ArrayList<Pair<String, String>> lastFive = new ArrayList<>();
                    for (int i = chats.get(gameID).size() - 1; i >= 0 && i > chats.get(gameID).size() - 6; i--) {
                        lastFive.add(chats.get(gameID).get(i));
                    }
                    Collections.reverse(lastFive);
                    super.notifyAllObservers(x.getPlayers(),new ChatMessage(lastFive,inGame),true,gameID);
                }
                break;
            }
        }
    }

    /**
     * Creates a game lobby with the specified number of players, username, and client ID.
     *
     * @param numPlayers The number of players for the game.
     * @param username   The username of the player.
     * @param clientId   The client ID.
     */
    public synchronized void createGame(Integer numPlayers, String username, String clientId) {
        //userIdentification.get(username).setGameID(UUID.randomUUID().toString());
        currentGames.put(new GameLobby(UUID.randomUUID().toString(), username, numPlayers, clientId), null);
        if (playersInLobby.containsKey(username)) {
            // It notifies every player still outside the lobby when a new game is created and activates launchGameManager in the view
            this.playersInLobby.remove(username);
            this.playersInLobby.put(username, true);
            for (String s : this.playersInLobby.keySet()) {
                if (this.playersInLobby.get(s).equals(false)) {
                    super.notifyObserver(s, new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
                }
            }
        } else {
            System.out.println("Player is not present");
        }
        System.out.println("new current games: " + currentGames.keySet());
    }

    /**
     * Handles the ping message from the client and sends a pong message in response.
     *
     * @param fromClientInfo The information of the client sending the ping message.
     */
    public synchronized void ping(RemoteUserInfo fromClientInfo) {
        // received ping message
        // send pong
        // TODO: server.send(new NetworkMessage("pong"));
        System.out.println("called ping() on server");
        super.notifyNetworkClient(fromClientInfo, new NetworkMessage("pong"));
    }

    /**
     * Gets all the joinable lobby IDs.
     *
     * @return A map of joinable lobby IDs and their corresponding player lists.
     */
    private synchronized HashMap<String, List<String>> getAllCurrentJoinableLobbiesIDs() {
        HashMap<String, List<String>> out = new HashMap<>();
        for (GameLobby x : this.currentGames.keySet()) {
            if (!x.isKilled()) {
                out.put(x.getID(), x.getPlayers());
            }
        }
        return out;
    }

    /**
     * Sets the credentials for the specified username, password, and user information.
     * If the user already exists, it checks the password; otherwise, it creates a new user.
     *
     * @param username  The username.
     * @param password  The password.
     * @param userInfo  The user information.
     */
    public synchronized void setCredentials(String username, String password, RemoteUserInfo userInfo) {
        // check if there was, else send message of erroneous username set request.
        boolean loggedSuccessful = false;
        if (nicknames.containsKey(username)) {
            // already exists, check if the password is correct
            if (nicknames.get(username).equals(password)) {
                // ok login
                // send all the games
                // userIDs.put(username, UID);
                System.out.println(username + " connected");
                System.out.println("current games: " + getAllCurrentJoinableLobbiesIDs());
                loggedSuccessful = true;
            } else {
                // username wrong password
                // send error
                super.notifyObserver(username, new ErrorMessage(ErrorType.wrongPassword, "Wrong password"), false, "-");
            }
        } else {
            // new user
            System.out.println(username + " connected");
            System.out.println("current games: " + getAllCurrentJoinableLobbiesIDs());
            nicknames.put(username, password);
            this.playersInLobby.put(username, false);
            loggedSuccessful = true;
        }
        if (loggedSuccessful) {
            // TODO: save map of user -> RMI or socket id
            userIdentification.put(username, userInfo);
            ServerMain.addUserToHashMap(username, userInfo);
            // print userIdentification
            System.out.println("userIdentification: " + ServerMain.getUserIdentification());
            if (ServerMain.getUserIdentification().get(username).getIsSocket()) {
                registerClient(username);
            }
            super.notifyObserver(username, new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
        }
    }

    /**
     * Sends the available games when a player wants to play again.
     *
     * @param username The username of the player.
     */
    public synchronized void lookForNewGames(String username) {
        super.notifyObserver(username, new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
    }



    //TODO: to do!!
    public void receiveAck(){

    }


    /*
    Gest methods LOBBIES and forward them to the exact game and lobby
     */

    /**
     * Starts the match for the specified game ID, user, and remote interface stub.
     *
     * @param ID   The ID of the game.
     * @param user The username of the user.
     * @param stub The remote interface stub.
     */
    public synchronized void startMatch(String ID, String user, MyRemoteInterface stub) {
        boolean found = false;
        for (GameLobby x : currentGames.keySet()) {
            if (x.getID().equals(ID)) {
                x.startMatch(user, stub);
                found = true;
            }
        }
        if (!found) {
            // TODO: Manage "ID not found" error
        }
    }

    /**
     * Creates a match from the specified lobby ID and list of players.
     *
     * @param ID          The ID of the lobby.
     * @param withPlayers The list of players.
     */
    public synchronized void createMatchFromLobby(String ID, ArrayList<String> withPlayers) {
        System.out.println("createMatchFromLobby");
        ArrayList<Player> players = new ArrayList<>();
        for (String p : withPlayers) {
            players.add(new Player(p));
        }
        // TODO: In the case where the player is creating a new game after finishing another one, I need to check if it already exists and replace the associated game if necessary.
        for (GameLobby x : currentGames.keySet()) {
            if (x.getID().equals(ID)) {
                if (ServerMain.userIdentificationInServer.get(x.getHost()).getIsSocket()) {
                    currentGames.put(x, new Game(players, ID, x.getHost()));
                } else {
                    currentGames.put(x, new Game(players, ID, ServerMain.userIdentificationInServer.get(x.getHost()).getRmiUID()));
                }
                x.killLobby();
                for (String s : withPlayers) {
                    if (!s.equals(x.getHost())) {
                        super.notifyObserver(s, new LobbyInfoMessage(ID, x.getHost(), withPlayers.size(), withPlayers, true), true, ID);
                    }
                }
                break;
            } // It's certainly there
        }
        // The game has been created
    }



    /*
    GAME methods
     */
    /**
     * Processes the selected cards for the specified user and game ID.
     *
     * @param selected The list of selected card pairs.
     * @param user     The username of the user.
     * @param gameID   The ID of the game.
     */
    public synchronized void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID) {
        for (Game x : currentGames.values()) {
            if (x != null) {
                if (x.getID().equals(gameID)) {
                    try {
                        x.selectedCards(selected, user);
                    } catch (UnselectableCardException e) {
                        super.notifyObserver(user, new ErrorMessage(ErrorType.selectedCardsMessageError, e.info), true, gameID);
                    }
                }
            }
        }
    }

    /**
     * Processes the selected column for the specified user, column, and game ID.
     *
     * @param selected The list of selected board cards.
     * @param column   The column index of the selected cards.
     * @param user     The username of the user.
     * @param gameID   The ID of the game.
     */
    public synchronized void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID) {
        for (Game x : currentGames.values()) {
            if (x.getID().equals(gameID)) {
                x.selectedColumn(selected, column, user); // Per i try catch, non basta averli nel "game"?
            }
        }
    }

    /**
     * Returns the thread-safe instance of the GameManager.
     *
     * @return The GameManager instance.
     */
    public static synchronized GameManager getInstance() {
        synchronized (GameManager.class) {
            if (instance == null) {
                instance = new GameManager();
            }
        }
        return instance;
    }


    public void sendAck() {
    }

    /*
    public void createGame() throws ErrorInGameCreationException{

    }
     */

//RMI methods
    /**
     * Receives a message for the specified IP address.
     *
     * @param ipAddress The IP address of the client.
     * @return The message associated with the IP address.
     */
    public synchronized Message ReceiveMessageRMI(String ipAddress) {
        System.out.println("\u001B[33mFaccio GET da: " + ipAddress + "\u001B[0m");
        return clientMessages.get(ipAddress);
    }

    /**
     * Adds a remote user to the collection.
     *
     * @param username       The username of the remote user.
     * @param remoteUserInfo Information about the remote user.
     */
    public synchronized void addRemoteUser(String username, RemoteUserInfo remoteUserInfo) {
        remoteUsers.put(username, remoteUserInfo);
    }

    /**
     * Returns the collection of remote users.
     *
     * @return The collection of remote users.
     */
    public synchronized static HashMap<String, RemoteUserInfo> getRemoteUsers() {
        return remoteUsers;
    }

    /**
     * Sets the message for the specified IP address.
     *
     * @param withMessage The message to set.
     * @param ipAddress   The IP address associated with the message.
     */
    public synchronized static void setMessageRMI(Message withMessage, String ipAddress) {
        previousClientMessages = clientMessages;
        clientMessages.put(ipAddress, withMessage);
        System.out.println("\u001B[33mMessage set for: " + ipAddress + " " + withMessage.toString() + "\u001B[0m");
        System.out.println("\u001B[33mStampiamo tutto il clientMessages\u001B[0m");
        // Output clientMessages
        for (Map.Entry<String, Message> entry : clientMessages.entrySet()) {
            System.out.println("\u001B[33m" + entry.getKey() + " Stiamo settando: " + entry.getValue() + "\u001B[0m");
        }
    }

    // getter for previousClientMessages message with ip
    public synchronized Message getPreviousMessageRMI(String ipAddress) {
        return previousClientMessages.get(ipAddress);
    }

    //setter updtate


    /**
     * Registers a client with the specified IP address.
     *
     * @param ipAddress The IP address of the client to register.
     */
    public synchronized void registerClient(String ipAddress) {
        System.out.println("\u001B[33mRegistering client " + ipAddress + "\u001B[0m");
        clients.add(ipAddress);
        clientMessages.put(ipAddress, null);
    }

    /**
     * Returns the host ID.
     *
     * @return The host ID.
     */
    public synchronized String getHostID() {
        return hostID;
    }

    /**
     * Returns the host of the game lobby associated with the specified game ID.
     *
     * @param gameID The ID of the game.
     * @return The host of the game lobby.
     */
    public synchronized String getGameLobbyHost(String gameID) {
        for (Game x : currentGames.values()) {
            if (x.getID().equals(gameID)) {
                return x.getHost();
            }
        }
        return null;
    }




}



