package it.polimi.ingsw.model;

import java.util.HashMap;

//this class is a singleton with all the current games
public class GameManager extends GameObservable{

    //this instance is used whenever we want the singleton
    private static GameManager instance;

    //hash map with the game lobby and the relative games
    private HashMap<GameLobby, Game> currentGames;
    private GameManager(){}

    //thread safe way of getting the GameManager singleton
    public static synchronized GameManager getInstance() {
        if (instance == null) {
            synchronized (GameManager.class) {
                if (instance == null) {
                    instance = new GameManager();
                }
            }
        }
        return instance;
    }

    /*
    public void createGame() throws ErrorInGameCreationException{

    }
     */


}
