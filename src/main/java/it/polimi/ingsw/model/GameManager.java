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
import it.polimi.ingsw.server.MyRemoteInterface;
import it.polimi.ingsw.server.RemoteUserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    private final HashMap<String,Boolean> playersNotInLobby; // boolean true means the player is in lobby
    protected GameManager(){
        nicknames = new HashMap<>();
        currentGames = new HashMap<>();
        userMatches = new HashMap<>();
        userIdentification = new HashMap<>();
        playersNotInLobby = new HashMap<>();
        chats = new HashMap<>();
    }


    public Message selectGame(String ID, String user){
        //currentGames.put(new GameLobby());
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(ID) && !x.isKilled()){
                //joins this lobby
                try {
                    x.addPlayer(user);
                    this.playersNotInLobby.remove(user);
                    this.playersNotInLobby.put(user,true);
                }catch(LobbyFullException e){
                    //lobby is full, returns error
                    super.notifyObserver(user, new ErrorMessage(ErrorType.lobbyIsFull,e.info), false, "-");
                }
            }
        }
        return null;
    }

    /**
     * User has either requested to see messages (fromUser and message null), or add message and get chat messages
     * (gameId, null, null, true) -> receive all the messages
     * (gameId, null, null. false) -> receive last 5 messages
     * (gameID, fromUser, message, false) -> send message and receive all the messages
     * (gameID, fromUser, message, true) -> send message and receive last 5 messages
     * @param gameID
     * @param fromUser
     * @param message
     * @param fullChat
     */
    public void receiveChatMessage(String gameID, String fromUser, String message, boolean fullChat){

        if(fromUser != null && message != null){
            chats.computeIfAbsent(gameID, k -> new ArrayList<>());
            Pair<String, String> myPair = new Pair<>(fromUser, message);
            chats.get(gameID).add(myPair);
        }

        for(GameLobby x: this.currentGames.keySet()){
            if(x.getID().equals(gameID)){
                ChatMessage sent;
                if(fullChat){
                    sent = new ChatMessage(chats.get(gameID));
                }else {
                    //just sends the last 5 messages
                    sent = new ChatMessage((ArrayList<Pair<String, String>>) chats.get(gameID).subList(Math.max(chats.get(gameID).size() - 5, 0), chats.get(gameID).size()));
                }
                super.notifyAllObservers(x.getPlayers(), new ChatMessage(chats.get(gameID)), true, gameID);
            }
        }
    }

    public Message createGame(Integer numPlayers, String username){
        currentGames.put(new GameLobby(UUID.randomUUID().toString(), username, numPlayers), null);
        if(playersNotInLobby.containsKey(username)){ //It notifies every player still outside the lobby when a new game is created, and activates launchGameManager in the view
            this.playersNotInLobby.remove(username);
            this.playersNotInLobby.put(username,true);
            for(String s: this.playersNotInLobby.keySet()) {
                if (this.playersNotInLobby.get(s).equals(false)) {
                    super.notifyObserver(s, new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
                }
            }
        }else{
            System.out.println("Player is not present");
        }
        System.out.println("new current games: " + currentGames.keySet());
        return null;
    }




    public Message ping(RemoteUserInfo fromClientInfo, MyRemoteInterface stub) {
        //received ping message
        //send pong
        //TODO: server.send(new NetworkMessage("pong"));
        System.out.println("called ping() on server");
        return super.notifyNetworkClient(fromClientInfo, new NetworkMessage("pong"), stub);
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


    public Message setCredentials(String username, String password, RemoteUserInfo userInfo){
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
            this.playersNotInLobby.put(username,false);
            loggedSuccesful = true;
        }

        if(loggedSuccesful){
            //TODO: save map of user -> RMI or socket id
            userIdentification.put(username, userInfo);
            return super.notifyObserver(username, new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
        }

        return null;
    }

    /**
     * It sends the available games when a player wants to play again
     *
     * @param
     * @return
     */
    //TODO: Fix this method
    public Message lookForNewGames(String username){
        super.notifyObserver(username,new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
        return null;
    }


    //TODO: to do!!
    public void receiveAck(){

    }


    /*
    Gest methods LOBBIES and forward them to the exact game and lobby
     */

    public Message startMatch(String ID, String user){
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
        return null;
    }
    public void createMatchFromLobby(String ID, ArrayList<String> withPlayers){
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
    public Message selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID){
        for(Game x: currentGames.values()){
            if(x.getID().equals(gameID)){
                try{
                    x.selectedCards(selected, user);
                }catch (UnselectableCardException e){
                    super.notifyObserver(user,new ErrorMessage(ErrorType.selectedCardsMessageError, e.info),true,gameID);
                }

            }
        }
        return null;
    }

    public Message selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID){
        for(Game x: currentGames.values()){
            if(x.getID().equals(gameID)){
                x.selectedColumn(selected,column,user); // Per i try catch, non basta averli nel "game"?
            }
        }
        return null;
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



