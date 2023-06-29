package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.model.messageModel.Message;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienRMIObjectInterface extends Remote {
    void callback(Message rec) throws RemoteException, IOException;
}
