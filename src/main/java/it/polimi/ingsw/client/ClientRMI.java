package it.polimi.ingsw.client;

import it.polimi.ingsw.model.MyRemoteInterface;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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