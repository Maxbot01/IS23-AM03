package it.polimi.ingsw.model.virtual_model;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.modelSupport.exceptions.ColumnNotSelectable;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;
import it.polimi.ingsw.server.MyRemoteInterface;
import it.polimi.ingsw.server.RemoteUserInfo;
import it.polimi.ingsw.server.VirtualGameManagerSerializer;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static it.polimi.ingsw.server.VirtualGameManagerSerializer.serializeMethod;

public class VirtualGameManager extends VirtualGameModel {

    public boolean isSocketClient;
    private MyRemoteInterface remoteObject;

    public VirtualGameManager(boolean isSocketClient,MyRemoteInterface remoteObject){
        this.isSocketClient = isSocketClient;
        this.remoteObject = remoteObject;
    }

    //setter for remoteObject

    public void ping() {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("ping", new Object[]{});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
            //serializeMethod(serializedGameManager);
        } else {
            try {
                RemoteUserInfo remoteUserInfo = new RemoteUserInfo(false, null, ClientManager.clientIP);
                remoteUserInfo.setRemoteObject(remoteObject);
                remoteObject.ping(remoteUserInfo);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setCredentials(String username, String password) {
        //TODO: IMPORTANTE; DA RIMUOVERE LA PROSSIMA LINEA (fatta per primo messaggio ma da fixare seializer)
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("setCredentials", new Object[]{username, password});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                RemoteUserInfo remoteUserInfo = new RemoteUserInfo(false, null, ClientManager.clientIP);
                remoteUserInfo.setRemoteObject(remoteObject);
                remoteObject.setCredentials(username, password, remoteUserInfo);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void selectGame(String gameID, String user) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectGame", new Object[]{gameID, user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                remoteObject.selectGame(gameID, user);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (LobbyFullException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void createGame(Integer numPlayers, String user) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("createGame", new Object[]{numPlayers, user});
            System.out.println("Creating game with " + numPlayers + " players from " + user);
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                remoteObject.createGame(numPlayers, user);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendAck() {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("sendAck", new Object[]{});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            remoteObject.sendAck();
        }
    }

    public void lookForNewGames(String user) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("lookForNewGames", new Object[]{user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                remoteObject.lookForNewGames(user);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    Lobby methods
     */
    public void startMatch(String ID, String user) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("startMatch", new Object[]{ID, user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                remoteObject.startMatch(ID, user);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    Game methods
     */

    public void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectedCards", new Object[]{selected, user, gameID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                remoteObject.selectedCards(selected, user, gameID);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (UnselectableCardException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectedColumn", new Object[]{selected, column, user, gameID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                remoteObject.selectedColumn(selected, column, user, gameID);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}