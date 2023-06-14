package it.polimi.ingsw.model.virtual_model;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;
import it.polimi.ingsw.server.MyRemoteInterface;
import it.polimi.ingsw.server.MyRemoteObject;
import it.polimi.ingsw.server.RemoteUserInfo;
import it.polimi.ingsw.server.VirtualGameManagerSerializer;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


//TODO: mettere interfaccia GameManager e VirtualGameManager così hanno stessi metodi di endpoint

import static it.polimi.ingsw.server.VirtualGameManagerSerializer.serializeMethod;


public class VirtualGameManager {

    public boolean isSocketClient;
    private MyRemoteInterface remoteObject;

    public VirtualGameManager(boolean isSocketClient,MyRemoteInterface remoteObject){
        this.isSocketClient = isSocketClient;
        this.remoteObject = remoteObject;
    }


    public void receiveChatMessage(String gameID, String fromUser, String message, boolean fullChat, boolean inGame){
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("receiveChatMessage", new Object[]{gameID,fromUser,message,fullChat,inGame});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
            //serializeMethod(serializedGameManager);
        } else {
           /* RemoteUserInfo remoteUserInfo = new RemoteUserInfo(false, null, ClientManager.clientIP);
            remoteUserInfo.setRemoteObject(remoteObject);
            remoteObject.receiveChatMessage(gameID, fromUser, message, fullChat, inGame);*/
        }
    }

    //setter for remoteObject

    public void ping(MyRemoteInterface stub) {
        System.out.println("Sono socket o rmi? " + isSocketClient);
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("ping", new Object[]{});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
            //serializeMethod(serializedGameManager);
        } else {
            RemoteUserInfo remoteUserInfo = new RemoteUserInfo(false, null, ClientManager.clientIP);
            try {
                stub.addMultiMatchClientMessage("SetupID",new HashMap<>());
                stub.ping(remoteUserInfo);
                Message message = stub.ReceiveMessageRMI(ClientManager.clientIP);
                //Message message = stub.getMultiMatchClientMessage(ClientManager.clientIP,"SetupID");
                System.out.println("Message received: " + message);
                ClientManager.clientReceiveMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setCredentials(String username, String password, MyRemoteInterface stub) {
        //TODO: IMPORTANTE; DA RIMUOVERE LA PROSSIMA LINEA (fatta per primo messaggio ma da fixare seializer)
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("setCredentials", new Object[]{username, password});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            System.out.println("vediamo se clientIP è null: " + ClientManager.clientIP);
            RemoteUserInfo remoteUserInfo = new RemoteUserInfo(false, null, ClientManager.clientIP);
            System.out.println(remoteUserInfo.getRmiUID());
            try {
                stub.addRemoteUser(username,remoteUserInfo);
                stub.setCredentials(username, password, remoteUserInfo);
                Message message = stub.ReceiveMessageRMI(ClientManager.clientIP);
                //Message message = stub.getMultiMatchClientMessage(ClientManager.clientIP,"SetupID");
                ClientManager.clientReceiveMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void selectGame(String gameID, String user, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectGame", new Object[]{gameID, user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                try {
                    stub.selectGame(gameID, user);
                    Message message = stub.ReceiveMessageRMI(ClientManager.clientIP);
                    //Message message = stub.getMultiMatchClientMessage(ClientManager.clientIP,"SetupID");
                    ClientManager.clientReceiveMessage(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (LobbyFullException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void createGame(Integer numPlayers, String user, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("createGame", new Object[]{numPlayers, user});
            System.out.println("Creating game with " + numPlayers + " players from " + user);
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.createGame(numPlayers, user,ClientManager.clientIP);
                stub.setHostID(ClientManager.clientIP);
                Message message = stub.ReceiveMessageRMI(ClientManager.clientIP);
                ClientManager.clientReceiveMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendAck(MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("sendAck", new Object[]{});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.sendAck();
                Message message = stub.ReceiveMessageRMI(ClientManager.clientIP);
                ClientManager.clientReceiveMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void lookForNewGames(String user, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("lookForNewGames", new Object[]{user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.lookForNewGames(user);
                Message message = stub.ReceiveMessageRMI(ClientManager.clientIP);
                ClientManager.clientReceiveMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    Lobby methods
     */
    public void startMatch(String ID, String user,MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("startMatch", new Object[]{ID, user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.startMatch(ID, user,stub);
                Message message = stub.ReceiveMessageRMI(ClientManager.clientIP);
                ClientManager.clientReceiveMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    Game methods
     */

    public void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectedCards", new Object[]{selected, user, gameID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                try {
                    stub.selectedCards(selected, user, gameID);
                    Message message = stub.ReceiveMessageRMI(ClientManager.clientIP);
                    ClientManager.clientReceiveMessage(message);
                    // per tutti i client in clients
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (UnselectableCardException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectedColumn", new Object[]{selected, column, user, gameID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.selectedColumn(selected, column, user, gameID);
                Message message = stub.ReceiveMessageRMI(ClientManager.clientIP);
                ClientManager.clientReceiveMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}