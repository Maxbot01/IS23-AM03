package it.polimi.ingsw.server;

import java.net.Socket;

public class RemoteUserInfo{
    private final Socket socketID;
    private final String rmiUID;

    private final boolean isSocket;

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
}