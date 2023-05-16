package it.polimi.ingsw.model.virtual_model;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.client.VirtualGameManagerSerializer;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;

import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw.client.VirtualGameManagerSerializer.serializeMethod;

public class VirtualGameManager extends VirtualGameModel{

    public void ping(String withUID){
        VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("ping", new Object[]{withUID});
        ClientMain.sendMessage(serializeMethod(serializedGameManager));
        //serializeMethod(serializedGameManager);
    }
    public void setCredentials(String username, String password, String UID){
        //TODO: IMPORTANTE; DA RIMUOVERE LA PROSSIMA LINEA (fatta per primo messaggio ma da fixare seializer)
        ClientManager.userNickname = username;

        VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("setCredentials", new Object[]{username, password, UID});
        ClientMain.sendMessage(serializeMethod(serializedGameManager));
    }
    public void selectGame(String gameID, String user){

        VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectGame", new Object[]{gameID, user});
        ClientMain.sendMessage(serializeMethod(serializedGameManager));

    }
    public void createGame(Integer numPlayers, String user){
        VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("createGame", new Object[]{numPlayers, user});
        System.out.println("Creating game with " + numPlayers + " players from " + user);
        ClientMain.sendMessage(serializeMethod(serializedGameManager));
    }

    public void sendAck(){
        VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("sendAck", new Object[]{});
        ClientMain.sendMessage(serializeMethod(serializedGameManager));
    }

    public void lookForNewGames(String user){
        VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("lookForNewGames", new Object[]{user});
        ClientMain.sendMessage(serializeMethod(serializedGameManager));
    }

    /*
    Lobby methods
     */
    public void startMatch(String ID, String user){
        VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("startMatch", new Object[]{ID, user});
        ClientMain.sendMessage(serializeMethod(serializedGameManager));
    }

    /*
    Game methods
     */

    public void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID){
        VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectedCards", new Object[]{selected, user, gameID});
        ClientMain.sendMessage(serializeMethod(serializedGameManager));
    }

    public void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID){
        VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectedColumn", new Object[]{selected,column,user,gameID});
        ClientMain.sendMessage(serializeMethod(serializedGameManager));
    }
}
