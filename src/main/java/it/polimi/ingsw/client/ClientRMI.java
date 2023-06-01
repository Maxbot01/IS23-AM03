package it.polimi.ingsw.client;

import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.server.MyRemoteInterface;
import it.polimi.ingsw.server.ServerRMI;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.SecureRandom;

import static it.polimi.ingsw.server.ServerMain.server;

public class ClientRMI extends Thread{
    static int PORT = 1099;
    public static MyRemoteInterface stub;
    private static boolean isRunning = true;
    public static void main(String[] args) {
        try {
            // Getting the registry
            ClientRMI client = new ClientRMI();
            Registry registry = null;
            registry = LocateRegistry.getRegistry("localhost", PORT);
            // Looking up the registry for the remote object
            MyRemoteInterface stub = (MyRemoteInterface) registry.lookup("MyRemoteInterface");
            setStub(stub);
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            stub.registerClient(ipAddress);
            ClientManager.initializeClientManagerSingleton(true, false, stub);
           client.run();

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }


    public void run(){
        while(isRunning){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //set the stub
    public static void setStub(MyRemoteInterface stub) {
        ClientRMI.stub = stub;
    }

}