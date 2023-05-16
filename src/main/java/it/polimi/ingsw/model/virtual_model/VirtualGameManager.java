package it.polimi.ingsw.model.virtual_model;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.client.VirtualGameManagerSerializer;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.virtual_model.VirtualGameModel;

import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw.client.VirtualGameManagerSerializer.serializeMethod;

public class VirtualGameManager extends VirtualGameModel {

    private boolean isSocket;

    public VirtualGameManager(boolean isSocket) {
        this.isSocket = isSocket;
    }

    public void ping(String withUID){
        VirtualGameManagerSerializer serializedGameManager;
        if (isSocket) {
            serializedGameManager = new VirtualGameManagerSerializer("ping", new Object[]{withUID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager gameManager = GameManager.getInstance();
            //chiamata al metodo senza serializzazione
            gameManager.ping(withUID);
        }
    }

    public void setCredentials(String username, String password, String UID){
        //TODO: IMPORTANTE; DA RIMUOVERE LA PROSSIMA LINEA (fatta per primo messaggio ma da fixare seializer)
        ClientManager.userNickname = username;
        VirtualGameManagerSerializer serializedGameManager;
        if (isSocket) {
            serializedGameManager = new VirtualGameManagerSerializer("setCredentials", new Object[]{username, password, UID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager gameManager = GameManager.getInstance();
            // chiamata al metodo senza serializzazione
            gameManager.setCredentials(username, password, UID);
        }
    }

    public void selectGame(String gameID, String user){
        VirtualGameManagerSerializer serializedGameManager;
        if (isSocket) {
            serializedGameManager = new VirtualGameManagerSerializer("selectGame", new Object[]{gameID, user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager gameManager = GameManager.getInstance();
            // chiamata al metodo senza serializzazione
            gameManager.selectGame(gameID, user);
        }
    }

    public void createGame(Integer numPlayers, String user){
        VirtualGameManagerSerializer serializedGameManager;
        if (isSocket) {
            serializedGameManager = new VirtualGameManagerSerializer("createGame", new Object[]{numPlayers, user});
            System.out.println("Creating game with " + numPlayers + " players from " + user);
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager gameManager = GameManager.getInstance();
            // chiamata al metodo senza serializzazione
            gameManager.createGame(numPlayers, user);
        }
    }

    public void sendAck(){
        VirtualGameManagerSerializer serializedGameManager;
        if (isSocket) {
            serializedGameManager = new VirtualGameManagerSerializer("sendAck", new Object[]{});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager gameManager = GameManager.getInstance();
            // chiamata al metodo senza serializzazione
            gameManager.sendAck();
        }
    }

    public void lookForNewGames(String user){
        VirtualGameManagerSerializer serializedGameManager;
        if (isSocket) {
            serializedGameManager = new VirtualGameManagerSerializer("lookForNewGames", new Object[]{user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager gameManager = GameManager.getInstance();
            // chiamata al metodo senza serializzazione
            gameManager.lookForNewGames(user);
        }
    }

    /*
    Lobby methods
     */
    public void startMatch(String ID, String user){
        VirtualGameManagerSerializer serializedGameManager;
        if (isSocket) {
            serializedGameManager = new VirtualGameManagerSerializer("startMatch", new Object[]{ID, user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager gameManager = GameManager.getInstance();
            // chiamata al metodo senza serializzazione
            gameManager.startMatch(ID, user);
        }
    }

    /*
    Game methods
     */

    public void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID){
        VirtualGameManagerSerializer serializedGameManager;
        if (isSocket) {
            serializedGameManager = new VirtualGameManagerSerializer("selectedCards", new Object[]{selected, user, gameID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager gameManager = GameManager.getInstance();
            // chiamata al metodo senza serializzazione
            gameManager.selectedCards(selected, user, gameID);
        }
    }

    public void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID){
        VirtualGameManagerSerializer serializedGameManager;
        if (isSocket) {
            serializedGameManager = new VirtualGameManagerSerializer("selectedColumn", new Object[]{selected, column, user, gameID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager gameManager = GameManager.getInstance();
            // chiamata al metodo senza serializzazione
            gameManager.selectedColumn(selected, column, user, gameID);
        }
    }
}
