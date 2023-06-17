package it.polimi.ingsw.model;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.ChatMessage;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorType;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;
import it.polimi.ingsw.server.RemoteUserInfo;

import java.io.IOException;
import java.util.*;

/**
 * Singleton that represents the front-end logic of the server and exposes the endpoint method that every client can call, it manages the requests creating and deleting the games
 */
public class GameManager extends GameObservable{

    //this instance is used whenever we want the singleton
    private static GameManager instance;

    //hash map with the game lobby and the relative games
    private HashMap<GameLobby, Game> currentGames;
    private HashMap<String, String> nicknames;
    //private HashMap<String, String> userIDs;
    public HashMap<String, RemoteUserInfo> userIdentification;
    private HashMap<String, Game> userMatches;

    private HashMap<String, ArrayList<Pair<String, String>>> chats;
    private final HashMap<String,Boolean> playersInLobby; // boolean true means the player is in lobby
    protected GameManager(){
        nicknames = new HashMap<>();
        currentGames = new HashMap<>();
        userMatches = new HashMap<>();
        userIdentification = new HashMap<>();
        playersInLobby = new HashMap<>();
        chats = new HashMap<>();
    }


    public void selectGame(String ID, String user) throws IOException {
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
                    super.notifyObserver(user, new ErrorMessage(ErrorType.lobbyIsFull, e.info), false, "-");
                }
            }
        }
    }

    /**
     * User has either requested to see messages (fromUser and message null), or add message and get chat messages
     * (gameId, null, null, true) -> receive all the messages //WE NEED THE USER TOO
     * (gameId, null, null. false) -> receive last 5 messages //WE NEED THE USER TOO
     * (gameID, fromUser, message, false) -> send message and receive all the messages
     * (gameID, fromUser, message, true) -> send message and receive last 5 messages
     * @param gameID
     * @param fromUser
     * @param message
     * @param fullChat
     */
    public void receiveChatMessage(String gameID, String fromUser, String message, boolean fullChat, boolean inGame) throws IOException {
        for(GameLobby x: this.currentGames.keySet()){
            if(x.getID().equals(gameID) || currentGames.get(x).getID().equals(gameID)){
                System.out.println("GM 1");
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
                    System.out.println("GM 2");
                    super.notifyAllObservers(x.getPlayers(),new ChatMessage(lastFive,inGame),true,gameID);
                }
                break;
            }
        }
    }

    public void createGame(Integer numPlayers, String username) throws IOException {
        currentGames.put(new GameLobby(UUID.randomUUID().toString(), username, numPlayers), null);
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




    public void ping(RemoteUserInfo fromClientInfo) throws IOException {
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
    private HashMap<String, List<String>> getAllCurrentJoinableLobbiesIDs(){
        HashMap<String, List<String>> out = new HashMap<>();
        for(GameLobby x: this.currentGames.keySet()){
            if(!x.isKilled()){
                out.put(x.getID(), x.getPlayers());
            }
        }
        return out;
    }


    public void setCredentials(String username, String password, RemoteUserInfo userInfo) throws IOException {
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
            super.notifyObserver(username, new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
        }

    }

    /**
     * It sends the available games when a player wants to play again
     * @param
     */
    //TODO: Fix this method
    public void lookForNewGames(String username) throws IOException {
        super.notifyObserver(username,new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
    }


    //TODO: to do!!
    public void receiveAck(){

    }


    /*
    Gest methods LOBBIES and forward them to the exact game and lobby
     */

    public void startMatch(String ID, String user) throws IOException {
        boolean found = false;
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(ID)){
                x.startMatch(user);
                found = true;
            }
        }
        if(!found){
            //TODO: Manage "ID not found" error
        }
    }
    public void createMatchFromLobby(String ID, ArrayList<String> withPlayers) throws IOException {
        System.out.println("createMatchFromLobby");
        ArrayList<Player> players = new ArrayList<>();
        for(String p: withPlayers){
            players.add(new Player(p));
        }
        //TODO: nel caso in cui il giocatore stia creando una nuova partita dopo che ne ha terminata un'altra, devo controllare che ci sia giò e nel caso rimpiazzare il game a cui è collegato
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(ID)){
                currentGames.put(x, new Game(players,ID));
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
    public void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID) throws IOException {
        for(Game x: currentGames.values()){
            if(x.getID().equals(gameID)){
                try{
                    x.selectedCards(selected, user);
                }catch (UnselectableCardException e){
                    super.notifyObserver(user,new ErrorMessage(ErrorType.selectedCardsMessageError, e.info),true,gameID);
                }

            }
        }
    }

    public void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID) throws IOException {
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





}



