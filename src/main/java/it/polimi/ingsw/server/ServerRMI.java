package it.polimi.ingsw.server;

import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.messageModel.Message;

import java.net.InetAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerRMI{
    /*
    static int PORT = 1099;
    static Message message;

    private static List<String> clients = new ArrayList<>();

    public Message ReceiveMessageRMI() {
        return message;
    }

    public static void SetMessage(Message withMessage) {
        System.out.println("Setting message" + withMessage);
        message = withMessage;
    }

    public void registerClient(String ipAddress) {
        System.out.println("Registering client " + ipAddress);
        clients.add(ipAddress);
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello from Server!" );
        ServerRMI obj = new ServerRMI();
        try {
            MyRemoteInterface stub = (MyRemoteInterface) UnicastRemoteObject.exportObject(obj, PORT);
            Registry registry = null;
            registry = LocateRegistry.createRegistry(PORT);
            registry.bind("MyRemoteInterface", stub);
            System.out.println("Server ready");
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void receiveChatMessage(String gameID, String fromUser, String message) {

    }*/
}
