package it.polimi.ingsw.model;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.server.ServerMain;

import java.util.List;

public abstract class GameObservable {

    protected void notifyObserver(String toPlayer, Message withMessage, boolean inLobbyOrGame, String ID){
        //if we are in a lobby or in a game needs to send the id of the lobby/game
        MessageSerializer messageSerializer = new MessageSerializer();
        String serializedMessage;
        //GameManager.getInstance().getUID(t

        if(inLobbyOrGame){
            serializedMessage = messageSerializer.serialize(withMessage, toPlayer, ID);
            System.out.println("Sending message to " + toPlayer + ": " + serializedMessage.toString());
        }else{
            serializedMessage = messageSerializer.serialize(withMessage, toPlayer, ID);
            System.out.println("Sending message to " + toPlayer + ": " + serializedMessage.toString());
        }
        ServerMain.server.broadcastMessage(serializedMessage);

        //withMessage.printMessage();
    }
    protected void notifyAllObservers(List<String> observers, Message withMessage, boolean inLobbyOrGame, String ID){
        //send the message for every given nick (TO CHANGE MAYBE)
        System.out.println("Sending message to everyone" + observers);
        //TODO: change format for this
        for(String x: observers){
            MessageSerializer messageSerializer = new MessageSerializer();
            String serializedMessage;
            if(inLobbyOrGame){
                serializedMessage = messageSerializer.serialize(withMessage, x, ID);
                System.out.println("Sending message to " + x + ": " + serializedMessage.toString());
            }else{
                serializedMessage = messageSerializer.serialize(withMessage, x, ID);
                System.out.println("Sending message to " + x + ": " + serializedMessage.toString());
            }
            ServerMain.server.broadcastMessage(serializedMessage);
        }

        withMessage.printMessage();
    }

    /*
    protected void mapReceivedCall(String, String[]) throws Exception{
        //send the message
    }*/
}
