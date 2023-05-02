package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.NetworkMessage;

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
        nicknames = new ArrayList<>();
        currentGames = new HashMap<>();
        userMatches = new HashMap<>();
    }

    public void selectGame(String ID){

    }

    public void ping(){
        //received ping message
        //send pong
        //TODO: server.send(new NetworkMessage("pong"));
    }

    public void createGame(int numPlayers){

    }

    public void setUsername(String username, String password){
        //check if there was, else send message of erroneus urername set request.
        if(nicknames.containsKey(username)){
            //already exists, checks if psw is right
            if (nicknames.get(username).equals(password)){

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

    /*
    public void createGame() throws ErrorInGameCreationException{

    }
     */


}
