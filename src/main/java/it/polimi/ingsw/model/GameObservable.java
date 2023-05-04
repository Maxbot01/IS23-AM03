package it.polimi.ingsw.model;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.modelSupport.Player;

import java.util.ArrayList;

public abstract class GameObservable {

    protected void notifyObserver(String toPlayer, Message withMessage, boolean inLobbyOrGame, String ID){
        //if we are in a lobby or in a game needs to send the id of the lobby/game
        MessageSerializer messageSerializer = new MessageSerializer();
        String serializedMessage = messageSerializer.serialize(withMessage);


        System.out.println("Arrived message to " + toPlayer + serializedMessage.toString());
        withMessage.printMessage();
    }
    protected void notifyAllObservers(Message withMessage, boolean inLobbyOrGame, String ID){
        //send the message
        System.out.println("Arrived message" + withMessage.toString());

        withMessage.printMessage();
    }

    /*
    protected void mapReceivedCall(String, String[]) throws Exception{
        //send the message
    }*/
}
