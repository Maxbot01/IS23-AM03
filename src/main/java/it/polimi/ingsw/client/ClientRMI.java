package it.polimi.ingsw.client;

import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.server.MyRemoteInterface;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.SecureRandom;

import static it.polimi.ingsw.server.ServerMain.server;

public class ClientRMI {
    static int PORT = 1099;
    private static boolean isRunning = true;
    ClientRMI() {
        try {
            // Getting the registry
            Registry registry = null;
            registry = LocateRegistry.getRegistry("localhost", PORT);
            // Looking up the registry for the remote object
            MyRemoteInterface stub = (MyRemoteInterface) registry.lookup("MyRemoteInterface");
            //setStub(stub);

            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            //stub.registerClient(ipAddress);
            //ClientManager.initializeClientManagerSingleton(true, false, stub);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }



}