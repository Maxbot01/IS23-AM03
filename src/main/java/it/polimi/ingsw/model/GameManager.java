package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorType;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Singleton with all the current games and the needed methods to create or join a game
 */
public class GameManager extends GameObservable{

    //this instance is used whenever we want the singleton
    private static GameManager instance;

    //hash map with the game lobby and the relative games
    private HashMap<GameLobby, Game> currentGames;
    private HashMap<String, String> nicknames;

    private HashMap<String, Game> userMatches;
    private GameManager(){
        nicknames = new HashMap<>();
        currentGames = new HashMap<>();
        userMatches = new HashMap<>();
    }

    public void selectGame(String ID, String fromUsername){
        //currentGames.put(new GameLobby());
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(ID)){
                //joins this lobby
                try {
                    x.addPlayer(fromUsername);
                }catch(LobbyFullException e){
                    //lobby is full, returns error

                }
            }
        }
    }

    public void ping(){
        //received ping message
        //send pong
        //TODO: server.send(new NetworkMessage("pong"));
    }

    public void createGame(int numPlayers){

    }

    public void setCredentials(String username, String password){
        //check if there was, else send message of erroneus urername set request.
        if(nicknames.containsKey(username)){
            //already exists, checks if psw is right
            if (nicknames.get(username).equals(password)){
                //ok login
                //sends all the games
                super.notifyObserver(username, new loginGameMessage(currentGames));
            }else{
                //username wrong password
                //sends error
                //super.notifyObserver(username, new ErrorMessage(ErrorType.wrongPassword));
            }
        }else{
            //new user
            nicknames.put(username, password);
            super.notifyObserver(username, new loginGameMessage(currentGames));
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

    /*
    public void createGame() throws ErrorInGameCreationException{

    }
     */


}
