package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.model.messageModel.Message;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * The interface for the RMI object used for client callbacks.
 */
public interface ClienRMIObjectInterface extends Remote {

    /**
     * Callback method for receiving a message from the server.
     *
     * @param rec The received message.
     * @throws RemoteException If a remote exception occurs.
     * @throws IOException     If an I/O error occurs.
     */
    void callback(Message rec) throws RemoteException, IOException;
}