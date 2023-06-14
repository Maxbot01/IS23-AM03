package it.polimi.ingsw.server;

import java.io.Serializable;
import java.net.Socket;

public class RemoteUserInfo implements Serializable {
    private final Socket socketID;
    private final String rmiUID;

    private boolean isSocket;
    public static String ipAddress;
    public static String GameID;

    /**
     * This class is inteded to represent the connection of a logged user (RMI or socket)
     */
    private MyRemoteInterface remoteObject;

    public RemoteUserInfo(boolean isSocket, Socket socketID, String rmiUID){
        this.socketID = socketID;
        this.rmiUID = rmiUID;
        this.isSocket = isSocket;
    }
    public Socket getSocketID(){
        return this.socketID;
    }

    public String getRmiUID(){
        return rmiUID;
    }
    public boolean isConnectionSocket(){
        return isSocket;
    }

    //getter isSocket
    public boolean getIsSocket() {
        return this.isSocket;
    }

    //setter ip address
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    //setter game id
    public void setGameID(String GameID) {
        this.GameID = GameID;
    }

    //getter game id
    public String getGameIDforRMI() {
        return this.GameID;
    }


}