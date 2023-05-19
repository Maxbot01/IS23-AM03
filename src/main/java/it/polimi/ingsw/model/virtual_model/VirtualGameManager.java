package it.polimi.ingsw.model.virtual_model;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.server.RemoteUserInfo;
import it.polimi.ingsw.server.VirtualGameManagerSerializer;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;

import java.util.ArrayList;

import static it.polimi.ingsw.server.VirtualGameManagerSerializer.serializeMethod;

public class VirtualGameManager extends VirtualGameModel{

    public boolean isSocketClient;

    public VirtualGameManager(boolean isSocketClient){
        this.isSocketClient = isSocketClient;
    }

    public void ping(){
        if(isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("ping", new Object[]{});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
            //serializeMethod(serializedGameManager);
        } else {
          GameManager.getInstance().ping(new RemoteUserInfo(false, null, ClientManager.rmiUID));
        }
    }
    public void setCredentials(String username, String password){
        //TODO: IMPORTANTE; DA RIMUOVERE LA PROSSIMA LINEA (fatta per primo messaggio ma da fixare seializer)
        if(isSocketClient) {
            ClientManager.userNickname = username;
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("setCredentials", new Object[]{username, password});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager.getInstance().setCredentials(username, password, new RemoteUserInfo(false, null, ClientManager.rmiUID));
        }
    }
    public void selectGame(String gameID, String user){
        if(isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectGame", new Object[]{gameID, user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager.getInstance().selectGame(gameID, user);
        }

    }
    public void createGame(Integer numPlayers, String user){
        if(isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("createGame", new Object[]{numPlayers, user});
            System.out.println("Creating game with " + numPlayers + " players from " + user);
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager.getInstance().createGame(numPlayers, user);
        }
    }

    public void sendAck(){
        if(isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("sendAck", new Object[]{});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager.getInstance().sendAck();
        }
    }

    public void lookForNewGames(String user){
        if(isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("lookForNewGames", new Object[]{user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager.getInstance().lookForNewGames(user);
        }
    }

    /*
    Lobby methods
     */
    public void startMatch(String ID, String user){
        if(isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("startMatch", new Object[]{ID, user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager.getInstance().startMatch(ID, user);
        }
    }

    /*
    Game methods
     */

    public void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID){
        if(isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectedCards", new Object[]{selected, user, gameID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager.getInstance().selectedCards(selected, user, gameID);
        }
    }

    public void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID){
        if(isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectedColumn", new Object[]{selected,column,user,gameID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            GameManager.getInstance().selectedColumn(selected,column,user,gameID);
        }
    }
}
