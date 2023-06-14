package it.polimi.ingsw.server;

import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.messageModel.Message;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRemoteObject extends GameManager implements MyRemoteInterface {

    static int PORT = 1099;
    static Message message;

    private static List<String> clients = new ArrayList<>();
    private static Map<String, Message> clientMessages = new HashMap<>();
    private static Map<String, Map<String, Message>> MultiMatchClientMessage = new HashMap<>();
    public static HashMap<String, RemoteUserInfo> remoteUsers = new HashMap<>();
    private String hostID;
    private static Map<String, Message> previousClientMessages = new HashMap<>();

    private boolean flag = false;

    public void updateState() {
        flag = true;
    }

    public boolean getFlag() {
        return flag;
    }

    public void updateStateFalse(){
        flag = false;
    }


    public Message ReceiveMessageRMI(String ipAddress) {
        System.out.println("CLient che fa Get: " + ipAddress);
        for (Map.Entry<String, Message> entry : clientMessages.entrySet()) {
            System.out.println("\u001B[33m" + entry.getKey() + " Facciamo Get: " + entry.getValue() + "\u001B[0m");
        }
        return clientMessages.get(ipAddress);
    }

    //gettter MUltiMatchClientMessage
    public Message getMultiMatchClientMessage(String ipAddress, String gameID) {
        if (MultiMatchClientMessage.containsKey(gameID)) {
            if (MultiMatchClientMessage.get(gameID).containsKey(ipAddress)) {
                return MultiMatchClientMessage.get(gameID).get(ipAddress);
            }
        }
        return null;
    }

    //add MultiMatchClientMessage
    public void addMultiMatchClientMessage(String gameID, Map<String, Message> clientMessages) {
        MultiMatchClientMessage.put(gameID, clientMessages);
    }

    //setter MultiMatchClientMessage
    public static void setMultiMatchClientMessage(String ipAddress, String gameID, Message message) {
        if (MultiMatchClientMessage.containsKey(gameID)) {
            if (clientMessages.containsKey(ipAddress)) {
                clientMessages.put(ipAddress, message);
            }
        }
    }


    //remoteUsers
    public void addRemoteUser(String username, RemoteUserInfo remoteUserInfo){
        remoteUsers.put(username, remoteUserInfo);
    }

    public void setHostID(String ID) throws RemoteException {
        System.out.println("Setting host ID to " + ID);
        this.hostID = ID;

    }

    //getter remoteUsers
    public static HashMap<String, RemoteUserInfo> getRemoteUsers(){
        return remoteUsers;
    }

    //getter clients
    public List<String> getClients() {
        return clients;
    }


    //getter clientMessages
    public Map<String, Message> getClientMessages() {
        return clientMessages;
    }

    public boolean update(String ipAddress) throws RemoteException {
        for (Map.Entry<String, Message> entry : clientMessages.entrySet()) {
            String key = entry.getKey();
            Message currentMessage = entry.getValue();
            Message previousMessage = previousClientMessages.get(key);

            if (previousMessage == null || !currentMessage.equals(previousMessage)) {
                // Il valore associato alla chiave è diverso dal valore precedente
                previousClientMessages.put(key, currentMessage);
                System.out.println("TRUE");
                return true;
            }
        }
        System.out.println("FALSE");
        return false;
    }


    public static void SetMessage(Message withMessage, String ipAddress) {
        clientMessages.put(ipAddress, withMessage);
        System.out.println("Message set for: " + ipAddress + " " + withMessage.toString());
        //output clientMessages
        for (Map.Entry<String, Message> entry : clientMessages.entrySet()) {
            System.out.println(entry.getKey() + " Stiamo settando: " + entry.getValue());
        } //per tutti i client diversi da me
       /* if(message instanceof MatchStateMessage){
            //facciamo fare a tutti la get
            SetAllCLientsMessage(message);
        }*/
    }

    public static void SetAllCLientsMessage(Message message) {
        for (String client : clients) {
            clientMessages.put(client, message);
        }
    }


    public void registerClient(String ipAddress) {
        System.out.println("Registering client " + ipAddress);
        clients.add(ipAddress);
        clientMessages.put(ipAddress, null);
    }

    public String getHostID() {
        return hostID;
    }
}
