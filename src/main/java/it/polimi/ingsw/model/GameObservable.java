package it.polimi.ingsw.model;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.server.MyRemoteInterface;
import it.polimi.ingsw.server.RemoteUserInfo;
import it.polimi.ingsw.server.ServerMain;

import java.util.HashMap;
import java.util.List;


import static it.polimi.ingsw.model.GameManager.userIdentification;
import static it.polimi.ingsw.server.MyRemoteObject.*;

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

        System.out.println(withMessage.toString());
        System.out.println("Sending message to " + toPlayer);
        sendMessageToNetworkUser(toPlayer, withMessage, gameID);

        //withMessage.printMessage();
    }

    protected void notifyNetworkClient(RemoteUserInfo client, Message withMessage){
        System.out.println("sending out");
        if(client.getSocketID() != null){
            //send socket
            MessageSerializer messageSerializer = new MessageSerializer();
            String serializedMessage;
            //GameManager.getInstance().getUID(t

            serializedMessage = messageSerializer.serialize(withMessage, "", "");
            ServerMain.server.sendMessageToSocket(serializedMessage, client.getSocketID());
        }else{
            System.out.println("sending out");
            SetMessage(withMessage, client.getRmiUID());
            //setMultiMatchClientMessage(client.getRmiUID(), client.getGameIDforRMI(), withMessage);
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
        System.out.println("Sending message to everyone " + observers);
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
        System.out.println("sending out");
      if(ServerMain.getUserIdentification().get(toPlayer).getIsSocket()){
            //user is socket
            MessageSerializer messageSerializer = new MessageSerializer();
            String serializedMessage;
            //GameManager.getInstance().getUID(t

            serializedMessage = messageSerializer.serialize(withMessage, toPlayer, gameID);
            System.out.println("Sending message to " + toPlayer + ": " + serializedMessage.toString());

            ServerMain.server.sendMessageToSocket(serializedMessage, ServerMain.getUserIdentification().get(toPlayer).getSocketID());
        }else{
          System.out.println("Sending message to " + toPlayer + ": " + withMessage);
          System.out.println(getRemoteUsers());
          SetMessage(withMessage, getRemoteUsers().get(toPlayer).getRmiUID());
        }

    }

    /*
    protected void mapReceivedCall(String, String[]) throws Exception{
        //send the message
    }*/
}
