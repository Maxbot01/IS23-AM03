package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.model.messageModel.Message;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClienRMIObject extends UnicastRemoteObject implements ClienRMIObjectInterface, Remote, Serializable {
    public ClienRMIObject() throws RemoteException {
        //UnicastRemoteObject.exportObject(this, 1011);
    }

    public void callback (Message rec) throws IOException {
        //System.out.println(who_what);
        System.out.println("GOT CALLBACK with " + rec);
        ClientManager.clientReceiveMessage(rec);
    }
}
