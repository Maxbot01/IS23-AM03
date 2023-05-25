package it.polimi.ingsw.model;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorType;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.GameStateMessage;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.exceptions.ColumnNotSelectable;
import it.polimi.ingsw.model.modelSupport.exceptions.ShelfFullException;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;
import it.polimi.ingsw.server.RemoteUserInfo;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Singleton with all the current games and the needed methods to create or join a game
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
    private final HashMap<String,Boolean> playersNotInLobby; // boolean true means the player is in lobby
    private GameManager(){
        nicknames = new HashMap<>();
        currentGames = new HashMap<>();
        userMatches = new HashMap<>();
        userIdentification = new HashMap<>();
        playersNotInLobby = new HashMap<>();
    }


    public void selectGame(String ID, String user){
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
                    super.notifyObserver(user, new ErrorMessage(ErrorType.lobbyIsFull), false, "-");
                }
            }
        }
    }

    public void createGame(Integer numPlayers, String username){
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
    }



    public void ping(RemoteUserInfo fromClientInfo){
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


    public void setCredentials(String username, String password, RemoteUserInfo userInfo){
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
                super.notifyObserver(username, new ErrorMessage(ErrorType.wrongPassword), false, "-");
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
            super.notifyObserver(username, new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
        }

    }

    /**
     * It sends the available games when a player wants to play again
     * @param
     */
    //TODO: Fix this method
    /*public void lookForNewGames(String username){
        super.notifyObserver(username,new loginGameMessage(getAllCurrentJoinableLobbiesIDs(), username), false, "-");
    }*/


    //TODO: to do!!
    public void receiveAck(){

    }


    /*
    Gest methods LOBBIES and forward them to the exact game and lobby
     */

    public void startMatch(String ID, String user){
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
    public void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID){
        for(Game x: currentGames.values()){
            if(x.getID().equals(gameID)){
                try{
                    x.selectedCards(selected, user);
                }catch (UnselectableCardException e){
                    //TODO: manage exception
                }

            }
        }
    }

    public void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID){ //TODO: Capire come usare user
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



