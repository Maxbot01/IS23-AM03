package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.modelSupport.Player;

public abstract class GameObservable {

    protected void notifyObserver(Player toPlayer, Message withMessage){

    }
    protected void notifyAllObservers(List<Player> toPlayers, Message withMessage){
        //send the message

    }

    /*
    protected void mapReceivedCall(String, String[]) throws Exception{
        //send the message
    }*/
}
