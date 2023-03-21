package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.modelSupport.Player;

import java.util.ArrayList;

public abstract class GameObservable {

    protected void notifyObserver(Player toPlayer, Message withMessage){
        System.out.println("Arrived message to " + toPlayer.getNickname() + withMessage.toString());
        withMessage.printMessage();
    }
    protected void notifyAllObservers(ArrayList<Player> toPlayers, Message withMessage){
        //send the message
        System.out.println("Arrived message" + withMessage.toString());
        withMessage.printMessage();
    }

    /*
    protected void mapReceivedCall(String, String[]) throws Exception{
        //send the message
    }*/
}
