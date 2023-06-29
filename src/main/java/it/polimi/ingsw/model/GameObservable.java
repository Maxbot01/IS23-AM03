package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.client.MessageSerializer;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.server.RemoteUserInfo;
import it.polimi.ingsw.server.ServerMain;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.model.GameManager.getRemoteUsers;


public abstract class GameObservable implements Serializable, Remote {

    /**
     * Notifies a single client, given the username gets the latest socket/rmi id and sends the message
     * @param toPlayer
     * @param withMessage
     * @param inLobbyOrGame
     * @param gameID
     */
    protected void notifyObserver(String toPlayer, Message withMessage, boolean inLobbyOrGame, String gameID){
        //if we are in a lobby or in a game needs to send the id of the lobby/game

        System.out.println(withMessage.toString());
        System.out.println("Sending message to " + toPlayer);
        try {
            sendMessageToNetworkUser(toPlayer, withMessage, gameID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //withMessage.printMessage();
    }

    protected void notifyNetworkClient(RemoteUserInfo client, Message withMessage) throws IOException {
        System.out.println("sending out");
        if (client.getSocketID() != null) {
            // send via socket
            MessageSerializer messageSerializer = new MessageSerializer();
            String serializedMessage = messageSerializer.serialize(withMessage, "", "");
            ServerMain.server.sendMessageToSocket(serializedMessage, client.getSocketID());
        } else {
            // RMI client
            System.out.println("sending out");

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    client.getRmiUID().callback(withMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            executor.shutdown();
        }
    }







    /**
     * Notifies multiple observers, usually every user of a game
     * @param observers
     * @param withMessage
     * @param inLobbyOrGame
     * @param gameID
     */
    protected void notifyAllObservers(List<String> observers, Message withMessage, boolean inLobbyOrGame, String gameID){
        //send the message for every given nick (TO CHANGE MAYBE)
        System.out.println("Sending message to everyone " + observers);
        //TODO: change format for this
        for(String player: observers){
            try {
                sendMessageToNetworkUser(player, withMessage, gameID);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        withMessage.printMessage();

    }

    /**
     * Private method sed to send a message to a player rmi or socket
     * @param toPlayer
     * @param withMessage
     * @param gameID
     */
    private void sendMessageToNetworkUser(String toPlayer, Message withMessage, String gameID) throws IOException {
        System.out.println("sending out NU+");
        if (ServerMain.getUserIdentification().get(toPlayer).getIsSocket()) {
            // user is socket
            MessageSerializer messageSerializer = new MessageSerializer();
            String serializedMessage;
            GameManager.setMessageRMI(withMessage, toPlayer);

            serializedMessage = messageSerializer.serialize(withMessage, toPlayer, gameID);
            System.out.println("Sending message to " + toPlayer + ": " + serializedMessage.toString());

            ServerMain.server.sendMessageToSocket(serializedMessage, ServerMain.getUserIdentification().get(toPlayer).getSocketID());
        } else {
            // RMI part
            System.out.println("Sending message to " + toPlayer + ": " + withMessage);
            System.out.println(getRemoteUsers());

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    ServerMain.getUserIdentification().get(toPlayer).getRmiUID().callback(withMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            executor.shutdown();
        }
    }



    /*
    protected void mapReceivedCall(String, String[]) throws Exception{
        //send the message
    }*/
}
