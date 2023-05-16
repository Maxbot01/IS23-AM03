package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.server.ServerInterface;
import it.polimi.ingsw.view.CLIgeneral;

public class ClientRMI {
    private ServerInterface server;
    private static boolean isRunning;
    private ObjectInputStream input;


    public ClientRMI(String serverHostname) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverHostname);
            server = (ServerInterface) registry.lookup("Server");
            isRunning = true;
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            isRunning = false;
        }
        ClientManager.userUID = UUID.randomUUID().toString();
        ClientManager cl = new ClientManager(true);
        ClientManager.isSocket = false;
    }


    public static void main(String[] args) throws IOException {
        CLIgeneral cli = new CLIgeneral();
        String serverHostname = "localhost";
        ClientRMI client = new ClientRMI(serverHostname);

    }

}
