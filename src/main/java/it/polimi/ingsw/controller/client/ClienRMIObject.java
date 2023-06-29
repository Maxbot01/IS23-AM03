package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.model.messageModel.Message;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The RMI object implementation for client callbacks.
 */
public class ClienRMIObject extends UnicastRemoteObject implements ClienRMIObjectInterface, Serializable {

    /**
     * Constructs a new instance of ClienRMIObject.
     *
     * @throws RemoteException If a remote exception occurs.
     */
    public ClienRMIObject() throws RemoteException {
        //UnicastRemoteObject.exportObject(this, 1011);
    }

    /**
     * Callback method for receiving a message from the server.
     *
     * @param rec The received message.
     * @throws IOException If an I/O error occurs.
     */
    public void callback(Message rec) throws IOException {
        //System.out.println(who_what);
        System.out.println("GOT CALLBACK with " + rec);
        ClientManager.clientReceiveMessage(rec);
    }
}
