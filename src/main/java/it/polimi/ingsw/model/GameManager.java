package it.polimi.ingsw.model;

import java.util.HashMap;

/**
 * Singleton with all the current games and the needed methods to create or join a game
 */
public class GameManager extends GameObservable{

    //this instance is used whenever we want the singleton
    private static GameManager instance;

    //hash map with the game lobby and the relative games
    private HashMap<GameLobby, Game> currentGames;
    private GameManager(){}

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
