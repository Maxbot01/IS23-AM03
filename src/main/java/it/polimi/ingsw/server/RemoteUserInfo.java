package it.polimi.ingsw.server;

import java.net.Socket;

public class RemoteUserInfo{
    private final Socket socketID;
    private final String rmiUID;

    private final boolean isSocket;

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
        return socketID;
    }

    public String getRmiUID(){
        return rmiUID;
    }
    public boolean isConnectionSocket(){
        return isSocket;
    }

    public MyRemoteInterface getRemoteObject() {
        return remoteObject;
    }

    public void setRemoteObject(MyRemoteInterface remoteObject) {
        this.remoteObject = remoteObject;
    }
}