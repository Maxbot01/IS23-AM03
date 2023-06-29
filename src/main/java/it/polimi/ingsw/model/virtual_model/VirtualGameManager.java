package it.polimi.ingsw.model.virtual_model;

import it.polimi.ingsw.controller.client.ClientMain;
import it.polimi.ingsw.controller.client.ClientManager;
import it.polimi.ingsw.model.MyRemoteInterface;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.server.RemoteUserInfo;
import it.polimi.ingsw.server.VirtualGameManagerSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


//TODO: mettere interfaccia GameManager e VirtualGameManager così hanno stessi metodi di endpoint

import static it.polimi.ingsw.server.VirtualGameManagerSerializer.serializeMethod;


/**
 * The VirtualGameManager class represents a virtual game manager.
        * It provides methods for managing game-related operations.
*/
public class VirtualGameManager implements Remote, Serializable {

    public boolean isSocketClient;
    private MyRemoteInterface remoteObject;

    /**
     * Constructs a VirtualGameManager object.
     *
     * @param isSocketClient true if the client is using sockets, false if using RMI
     * @param remoteObject   the remote object used for communication
     */
    public VirtualGameManager(boolean isSocketClient, MyRemoteInterface remoteObject) {
        this.isSocketClient = isSocketClient;
        this.remoteObject = remoteObject;
    }

    /**
     * Receives a chat message.
     *
     * @param gameID    the ID of the game
     * @param toUser    the user to whom the message is sent
     * @param fromUser  the user who sent the message
     * @param message   the chat message
     * @param fullChat  true if it's a full chat message, false otherwise
     * @param inGame    true if the message is in-game, false otherwise
     * @param stub      the remote object stub
     */
    public void receiveChatMessage(String gameID, String toUser, String fromUser, String message, boolean fullChat, boolean inGame, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("receiveChatMessage", new Object[]{gameID, toUser, fromUser, message, fullChat, inGame});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.receiveChatMessage(gameID, toUser, fromUser, message, fullChat, inGame);
                Message netMessage = stub.ReceiveMessageRMI(ClientManager.clientIP);
                System.out.println("Message received: " + netMessage);
                ClientManager.clientReceiveMessage(netMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Setter for remoteObject

    /**
     * Sends a ping message.
     *
     * @param stub the remote object stub
     */
    public void ping(MyRemoteInterface stub) {
        System.out.println("Sono socket o rmi? " + isSocketClient);
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("ping", new Object[]{});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            RemoteUserInfo remoteUserInfo = new RemoteUserInfo(false, null, ClientMain.clienRMIInstance);
            try {
                stub.ping(remoteUserInfo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sets the credentials for authentication.
     *
     * @param username the username
     * @param password the password
     * @param stub     the remote object stub
     */
    public void setCredentials(String username, String password, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("setCredentials", new Object[]{username, password});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            System.out.println("vediamo se clientIP è null: " + ClientManager.clientIP);
            RemoteUserInfo remoteUserInfo = new RemoteUserInfo(false, null, ClientMain.clienRMIInstance);
            System.out.println(remoteUserInfo.getRmiUID());
            try {
                stub.addRemoteUser(username, remoteUserInfo);
                stub.setCredentials(username, password, remoteUserInfo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Selects a game to join.
     *
     * @param gameID the ID of the game
     * @param user   the username of the user
     * @param stub   the remote object stub
     */
    public void selectGame(String gameID, String user, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectGame", new Object[]{gameID, user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                try {
                    stub.selectGame(gameID, user);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (LobbyFullException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Creates a new game.
     *
     * @param numPlayers the number of players
     * @param user       the username of the user
     * @param stub       the remote object stub
     */
    public void createGame(Integer numPlayers, String user, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("createGame", new Object[]{numPlayers, user});
            System.out.println("Creating game with " + numPlayers + " players from " + user);
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.createGame(numPlayers, user, ClientManager.clientIP);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sends an acknowledgment message.
     *
     * @param stub the remote object stub
     */
    public void sendAck(MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("sendAck", new Object[]{});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.sendAck();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Looks for new games.
     *
     * @param user the username of the user
     * @param stub the remote object stub
     */
    public void lookForNewGames(String user, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("lookForNewGames", new Object[]{user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.lookForNewGames(user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    Lobby methods
     */

    /**
     * Starts a match in the lobby.
     *
     * @param ID   the ID of the match
     * @param user the username of the user
     * @param stub the remote object stub
     */
    public void startMatch(String ID, String user, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("startMatch", new Object[]{ID, user});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.startMatch(ID, user, stub);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void userReady(String username, String lobbyID, MyRemoteInterface stub){
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("userReady", new Object[]{username, lobbyID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.userReady(username, lobbyID);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    Game methods
     */

    /**
     * Selects cards in the game.
     *
     * @param selected the list of selected cards
     * @param user     the username of the user
     * @param gameID   the ID of the game
     * @param stub     the remote object stub
     */
    public void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectedCards", new Object[]{selected, user, gameID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                try {
                    stub.selectedCards(selected, user, gameID);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (UnselectableCardException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Selects a column in the game.
     *
     * @param selected the list of selected cards in the column
     * @param column   the column number
     * @param user     the username of the user
     * @param gameID   the ID of the game
     * @param stub     the remote object stub
     */
    public void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID, MyRemoteInterface stub) {
        if (isSocketClient) {
            VirtualGameManagerSerializer serializedGameManager = new VirtualGameManagerSerializer("selectedColumn", new Object[]{selected, column, user, gameID});
            ClientMain.sendMessage(serializeMethod(serializedGameManager));
        } else {
            try {
                stub.selectedColumn(selected, column, user, gameID);
                //Message message = stub.ReceiveMessageRMI(ClientManager.clientIP);
                //ClientManager.clientReceiveMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}