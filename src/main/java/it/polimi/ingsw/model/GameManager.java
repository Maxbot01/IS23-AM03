package it.polimi.ingsw.model;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorType;
import it.polimi.ingsw.model.messageModel.matchStateMessages.GameStateMessage;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.exceptions.ColumnNotSelectable;
import it.polimi.ingsw.model.modelSupport.exceptions.ShelfFullException;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Singleton with all the current games and the needed methods to create or join a game
 */
public class GameManager extends GameObservable{

    //this instance is used whenever we want the singleton
    private static GameManager instance;

    //hash map with the game lobby and the relative games
    private HashMap<GameLobby, Game> currentGames;
    private HashMap<String, String> nicknames;
    private HashMap<String, String> userIDs;
    private HashMap<String, Game> userMatches;
    private GameManager(){
        nicknames = new HashMap<>();
        currentGames = new HashMap<>();
        userMatches = new HashMap<>();
    }

    public void selectGame(String ID, String user){
        //currentGames.put(new GameLobby());
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(ID) && !x.isKilled()){
                //joins this lobby
                try {
                    x.addPlayer(user);
                }catch(LobbyFullException e){
                    //lobby is full, returns error
                    super.notifyObserver(user, new ErrorMessage(ErrorType.lobbyIsFull), false, "-");
                }
            }
        }
    }

    public void createGame(Integer numPlayers, String username){
        //creates game
        currentGames.put(new GameLobby(UUID.randomUUID().toString(), username, numPlayers), null);
        System.out.println("new current games: " + currentGames.keySet());
    }



    public String getUID(String fromUsername){
        return userIDs.get(fromUsername);
    }

    public void createMatchFromLobby(String withID, ArrayList<String> withPlayers){
        if(!currentGames.containsKey(withID)){
            //handle error id so not exist
            return;
        }
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(withID)){
                System.out.println("inside create");
                ArrayList<Player> players = new ArrayList<>();
                for(String p: withPlayers){
                    players.add(new Player(p));
                }
                currentGames.put(x, new Game(players, withID));
                x.killLobby();
                //game has been created
            }
        }

    }

    public void ping(String fromClientUID){
        //received ping message
        //send pong
        //TODO: server.send(new NetworkMessage("pong"));
        System.out.println("called ping() on server");
        super.notifyObserver(fromClientUID, new NetworkMessage("pong"), false, "-");
    }


    //LOBBY METHODS

    /**
     * Gets all the lobbies that have not been tombstoned (joinable)
     * @return
     */
    private HashMap<GameLobby, Game> getAllCurrentJoinableLobbies(){
        HashMap<GameLobby, Game> out = new HashMap<>();
        for(GameLobby x: this.currentGames.keySet()){
            if(!x.isKilled()){
                out.put(x, this.currentGames.get(x));
            }
        }
        return out;
    }

    public void setCredentials(String username, String password, String UID){
        //check if there was, else send message of erroneus urername set request.
        if(nicknames.containsKey(username)){
            //already exists, checks if psw is right
            if (nicknames.get(username).equals(password)){
                //ok login
                //sends all the games
                userIDs.put(username, UID);
                System.out.println(username + "connected with UID: " + UID);
                System.out.println("current games: " + getAllCurrentJoinableLobbies());
                super.notifyObserver(username, new loginGameMessage(getAllCurrentJoinableLobbies(), username), false, "-");
            }else{
                //username wrong password
                //sends error
                super.notifyObserver(username, new ErrorMessage(ErrorType.wrongPassword), false, "-");
            }
        }else{
            //new user
            System.out.println(username + "connected");
            System.out.println("current games: " + getAllCurrentJoinableLobbies());
            nicknames.put(username, password);
            super.notifyObserver(username, new loginGameMessage(getAllCurrentJoinableLobbies(), username), false, "-");
        }
    }


    //TODO: to do!!
    public void receiveAck(){

    }


    /*
    Gest methods LOBBIES and forward them to the exact game and lobby
     */

    public void startMatch(String ID, String user){
        System.out.println("match start "+ ID + " user: " + user);

        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(ID)){
                //TODO:REMOVE!!! before return
                System.out.println("inside create");
                ArrayList<Player> players = new ArrayList<>();
                for(String p: x.getPlayers()){
                    players.add(new Player(p));
                }
                currentGames.put(x, new Game(players, ID));
                x.killLobby();
                return;
                //x.startMatch(user);
            }
        }
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
