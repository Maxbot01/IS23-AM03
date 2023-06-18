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

    public void updateState() {
        flag = true;
    }

    public boolean getFlag() {
        return flag;
    }

    public void updateStateFalse(){
        flag = false;
    }


    public GameManager(){
        nicknames = new HashMap<>();
        currentGames = new HashMap<>();
        userMatches = new HashMap<>();
        userIdentification = new HashMap<>();
        playersInLobby = new HashMap<>();
        chats = new HashMap<>();
    }

    //getter useridentification
    public synchronized HashMap<String, RemoteUserInfo> getUserIdentification() {
        return userIdentification;
    }



    public synchronized void selectGame(String ID, String user){
        //currentGames.put(new GameLobby());
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(ID) && !x.isKilled()){
                //joins this lobby
                try {
                    x.addPlayer(user);
                    this.playersInLobby.put(user,true);
                    //It notifies the not-in-lobby players that a game has been entered by someone (the players are updated)
                    for(String s: playersInLobby.keySet()){
                        if(playersInLobby.get(s).equals(false)){
                            super.notifyObserver(s,new loginGameMessage(getAllCurrentJoinableLobbiesIDs(),user),false,"-");
                        }
                    }
                }catch(LobbyFullException e){
                    //lobby is full, returns error
                    super.notifyObserver(user, new ErrorMessage(ErrorType.lobbyIsFull,e.info), false, "-");
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

    public synchronized void createGame(Integer numPlayers, String username, String clientId){
        //userIdentification.get(username).setGameID(UUID.randomUUID().toString());
        currentGames.put(new GameLobby(UUID.randomUUID().toString(), username, numPlayers, clientId), null);
        if(playersInLobby.containsKey(username)){ //It notifies every player still outside the lobby when a new game is created, and activates launchGameManager in the view
            this.playersInLobby.remove(username);
            this.playersInLobby.put(username,true);
            for(String s: this.playersInLobby.keySet()) {
                if (this.playersInLobby.get(s).equals(false)) {
                    super.notifyObserver(s, new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
                }
            }
        }else{
            System.out.println("Player is not present");
        }
        System.out.println("new current games: " + currentGames.keySet());
    }




    public synchronized void ping(RemoteUserInfo fromClientInfo) {
        //received ping message
        //send pong
        //TODO: server.send(new NetworkMessage("pong"));
        System.out.println("called ping() on server");
        super.notifyNetworkClient(fromClientInfo, new NetworkMessage("pong"));
    }


    //LOBBY METHODS

    /**
     * Gets all the lobbies that have not been tombstoned (joinable)
     * @return
     */
    private synchronized HashMap<String, List<String>> getAllCurrentJoinableLobbiesIDs(){
        HashMap<String, List<String>> out = new HashMap<>();
        for(GameLobby x: this.currentGames.keySet()){
            if(!x.isKilled()){
                out.put(x.getID(), x.getPlayers());
            }
        }
        return out;
    }


    public synchronized void setCredentials(String username, String password, RemoteUserInfo userInfo){
        //check if there was, else send message of erroneous username set request.
        boolean loggedSuccesful = false;
        if(nicknames.containsKey(username)){
            //already exists, checks if psw is right
            if (nicknames.get(username).equals(password)){
                //ok login
                //sends all the games
                //userIDs.put(username, UID);
                System.out.println(username + "connected");
                System.out.println("current games: " + getAllCurrentJoinableLobbiesIDs());
                loggedSuccesful = true;
            }else{
                //username wrong password
                //sends error
                 super.notifyObserver(username, new ErrorMessage(ErrorType.wrongPassword,"Wrong password"), false, "-");
            }
        }else{
            //new user
            System.out.println(username + "connected");
            System.out.println("current games: " + getAllCurrentJoinableLobbiesIDs());
            nicknames.put(username, password);
            this.playersInLobby.put(username,false);
            loggedSuccesful = true;
        }
        if(loggedSuccesful){
            //TODO: save map of user -> RMI or socket id
            userIdentification.put(username, userInfo);
            ServerMain.addUserToHashMap(username, userInfo);
            //stampa userIdentification
            System.out.println("userIdentification: " + ServerMain.getUserIdentification());
            if(ServerMain.getUserIdentification().get(username).getIsSocket()){
                registerClient(username);
            }
            super.notifyObserver(username, new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
        }
    }

    /**
     * It sends the available games when a player wants to play again
     *
     * @param
     * @return
     */
    //TODO: Fix this method
    public synchronized void lookForNewGames(String username){
        super.notifyObserver(username,new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
    }


    //TODO: to do!!
    public void receiveAck(){

    }


    /*
    Gest methods LOBBIES and forward them to the exact game and lobby
     */

    public synchronized void startMatch(String ID, String user, MyRemoteInterface stub){
        boolean found = false;
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(ID)){
                x.startMatch(user, stub);
                found = true;
            }
        }
        if(!found){
            //TODO: Manage "ID not found" error
        }
    }
    public synchronized void createMatchFromLobby(String ID, ArrayList<String> withPlayers){
        System.out.println("createMatchFromLobby");
        ArrayList<Player> players = new ArrayList<>();
        for(String p: withPlayers){
            players.add(new Player(p));
        }
        //TODO: nel caso in cui il giocatore stia creando una nuova partita dopo che ne ha terminata un'altra, devo controllare che ci sia giò e nel caso rimpiazzare il game a cui è collegato
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(ID)){
                if(ServerMain.userIdentificationInServer.get(x.getHost()).getIsSocket()){
                    currentGames.put(x, new Game(players,ID, x.getHost()));
                } else {
                    currentGames.put(x, new Game(players,ID, ServerMain.userIdentificationInServer.get(x.getHost()).getRmiUID()));
                }
                x.killLobby();
                for(String s: withPlayers){
                    if(!s.equals(x.getHost())){
                        super.notifyObserver(s,new LobbyInfoMessage(ID,x.getHost(), withPlayers.size(), withPlayers,true),true,ID);
                    }
                }
                break;
            }//It's certainly there
        }
        //game has been created
    }


    /*
    GAME methods
     */
    public synchronized void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID){
        for(Game x: currentGames.values()){
            if(x != null) {
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

    public synchronized void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID){
        for(Game x: currentGames.values()){
            if(x.getID().equals(gameID)){
                x.selectedColumn(selected,column,user); // Per i try catch, non basta averli nel "game"?
            }
        }
    }
    /*
    Thread safe GameManager instance creator
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


        public synchronized Message ReceiveMessageRMI(String ipAddress) {
            System.out.println("CLient che fa Get: " + ipAddress);
            for (Map.Entry<String, Message> entry : clientMessages.entrySet()) {
                System.out.println("\u001B[33m" + entry.getKey() + " Facciamo Get: " + entry.getValue() + "\u001B[0m");
            }
            return clientMessages.get(ipAddress);
        }

        //gettter MUltiMatchClientMessage
        public synchronized Message getMultiMatchClientMessage(String ipAddress, String gameID) {
            if (MultiMatchClientMessage.containsKey(gameID)) {
                if (MultiMatchClientMessage.get(gameID).containsKey(ipAddress)) {
                    return MultiMatchClientMessage.get(gameID).get(ipAddress);
                }
            }
            return null;
        }

        //add MultiMatchClientMessage
        public synchronized void addMultiMatchClientMessage(String gameID, Map<String, Message> clientMessages) {
            MultiMatchClientMessage.put(gameID, clientMessages);
        }

        //setter MultiMatchClientMessage
        public synchronized static void setMultiMatchClientMessage(String ipAddress, String gameID, Message message) {
            if (MultiMatchClientMessage.containsKey(gameID)) {
                if (clientMessages.containsKey(ipAddress)) {
                    clientMessages.put(ipAddress, message);
                }
            }
        }


        //remoteUsers
        public synchronized void addRemoteUser(String username, RemoteUserInfo remoteUserInfo){
            remoteUsers.put(username, remoteUserInfo);
        }

        public synchronized void setHostID(String ID) throws RemoteException {
            System.out.println("Setting host ID to " + ID);
            this.hostID = ID;
        }

        //getter remoteUsers
        public synchronized static HashMap<String, RemoteUserInfo> getRemoteUsers(){
            return remoteUsers;
        }

        //getter clients
        public synchronized List<String> getClients() {
            return clients;
        }


        //getter clientMessages
        public synchronized Map<String, Message> getClientMessages() {
            return clientMessages;
        }

        public synchronized boolean update(String ipAddress) throws RemoteException {
            for (Map.Entry<String, Message> entry : clientMessages.entrySet()) {
                String key = entry.getKey();
                Message currentMessage = entry.getValue();
                Message previousMessage = previousClientMessages.get(key);

                if (previousMessage == null || !currentMessage.equals(previousMessage)) {
                    // Il valore associato alla chiave è diverso dal valore precedente
                    previousClientMessages.put(key, currentMessage);
                    System.out.println("TRUE");
                    return true;
                }
            }
            System.out.println("FALSE");
            return false;
        }


        public synchronized static void SetMessage(Message withMessage, String ipAddress) {
            clientMessages.put(ipAddress, withMessage);
            System.out.println("Message set for: " + ipAddress + " " + withMessage.toString());
            //output clientMessages
            for (Map.Entry<String, Message> entry : clientMessages.entrySet()) {
                System.out.println(entry.getKey() + " Stiamo settando: " + entry.getValue());
            } //per tutti i client diversi da me
       /* if(message instanceof MatchStateMessage){
            //facciamo fare a tutti la get
            SetAllCLientsMessage(message);
        }*/
        }

        public synchronized static void SetAllCLientsMessage(Message message) {
            for (String client : clients) {
                clientMessages.put(client, message);
            }
        }


        public synchronized void registerClient(String ipAddress) {
            System.out.println("Registering client " + ipAddress);
            clients.add(ipAddress);
            clientMessages.put(ipAddress, null);
        }

        public synchronized String getHostID() {
            return hostID;
        }

        // getter gamelobby from currentGames
        public synchronized String getGameLobbyHost(String gameID) {
            System.err.println("GameID: " + gameID);
            //stampa tutti i currentGames
            for(GameLobby x: currentGames.keySet()){
                System.err.println("GameLobby: " + x.getID());
            }
            // itera su game
            for(Game x: currentGames.values()){
                if(x.getID().equals(gameID)){
                    System.err.println("HostID dentro equals: " + x.getHost());
                    return x.getHost();
                }
            }
            System.err.println("HostID: " + null);
            return null;
        }
}



