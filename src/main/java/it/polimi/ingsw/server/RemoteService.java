package it.polimi.ingsw.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteService extends Remote {
    void registerClient(String rmiUID) throws RemoteException;
    void sendMessage(String message, String rmiUID) throws RemoteException;
    void broadcastMessage(String message) throws RemoteException;
    void stop() throws RemoteException;
}
