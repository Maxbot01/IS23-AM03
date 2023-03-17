package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.Message;

public abstract class GameObservable {

    protected void notifyObserver(Player toPlayer, Message withMessage) throws Exception{

    }
    protected void notifyAllObservers(List<Player> toPlayers, Message withMessage) throws Exception{
        //send the message

    }

    /*
    protected void mapReceivedCall(String, String[]) throws Exception{
        //send the message
    }*/
}
