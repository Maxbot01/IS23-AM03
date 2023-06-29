package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.client.ClienRMIObjectInterface;
import it.polimi.ingsw.model.MyRemoteInterface;

import java.io.Serializable;
import java.net.Socket;

public class RemoteUserInfo implements Serializable {
    private final Socket socketID;
    private final ClienRMIObjectInterface rmiUID;

    private boolean isSocket;
    public static String ipAddress;
    public static String GameID;

    /**
     * This class is inteded to represent the connection of a logged user (RMI or socket)
     */
    private MyRemoteInterface remoteObject;

    /**
     * Constructor of the RemoteUserInfo class
     * @param isSocket
     * @param socketID
     * @param rmiUID
     */
    public RemoteUserInfo(boolean isSocket, Socket socketID, ClienRMIObjectInterface rmiUID){
        this.socketID = socketID;
        this.rmiUID = rmiUID;
        this.isSocket = isSocket;
    }
    /**
     * Getter for the socketID
     * @return socket ID
     */
    public Socket getSocketID(){
        return this.socketID;
    }

    /**
     * Getter for the rmi UID
     * @return rmiUID
     */
    public ClienRMIObjectInterface getRmiUID(){
        return rmiUID;
    }
    public boolean isConnectionSocket(){
        return isSocket;
    }

    /**
     * Getter isSocket
     * @return boolean (true -> socket)
     */
    public boolean getIsSocket() {
        return this.isSocket;
    }

    /**
     * Setter ip address
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Setter gameID
     * @param GameID
     */
    public void setGameID(String GameID) {
        this.GameID = GameID;
    }

    /**
     * Getter gameID
     * @return String, ID of the game
     */
    public String getGameIDforRMI() {
        return this.GameID;
    }


}