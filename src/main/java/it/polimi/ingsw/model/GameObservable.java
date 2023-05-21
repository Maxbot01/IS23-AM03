package it.polimi.ingsw.model;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.server.RemoteUserInfo;
import it.polimi.ingsw.server.ServerMain;

import java.util.ArrayList;
import java.util.List;

public abstract class GameObservable {

    /**
     * Notifies a single client, given the username gets the latest socket/rmi id and sends the message
     * @param toPlayer
     * @param withMessage
     * @param inLobbyOrGame
     * @param gameID
     */
    protected void notifyObserver(String toPlayer, Message withMessage, boolean inLobbyOrGame, String gameID){
        //if we are in a lobby or in a game needs to send the id of the lobby/game

        sendMessageToNetworkUser(toPlayer, withMessage, gameID);


        //withMessage.printMessage();
    }

    protected void notifyNetworkClient(RemoteUserInfo client, Message withMessage){
        if(client.isConnectionSocket()){
            //send socket
            MessageSerializer messageSerializer = new MessageSerializer();
            String serializedMessage;
            //GameManager.getInstance().getUID(t

            serializedMessage = messageSerializer.serialize(withMessage, "", "");
            ServerMain.server.sendMessageToSocket(serializedMessage, client.getSocketID());
        }else{
            //send rmi

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
        System.out.println("Sending message to everyone" + observers);
        //TODO: change format for this
        for(String player: observers){
            sendMessageToNetworkUser(player, withMessage, gameID);
        }

        withMessage.printMessage();
    }

    /**
     * Private method sed to send a message to a player rmi or socket
     * @param toPlayer
     * @param withMessage
     * @param gameID
     */
    private void sendMessageToNetworkUser(String toPlayer, Message withMessage, String gameID) {
        if(GameManager.getInstance().userIdentification.get(toPlayer).isConnectionSocket()){
            //user is socket
            MessageSerializer messageSerializer = new MessageSerializer();
            String serializedMessage;
            //GameManager.getInstance().getUID(t

            serializedMessage = messageSerializer.serialize(withMessage, toPlayer, gameID);
            System.out.println("Sending message to " + toPlayer + ": " + serializedMessage.toString());

            ServerMain.server.sendMessageToSocket(serializedMessage, GameManager.getInstance().userIdentification.get(toPlayer).getSocketID());
        }else{
            //TODO: user is RMI

        }
    }

    /*
    protected void mapReceivedCall(String, String[]) throws Exception{
        //send the message
    }*/
}
