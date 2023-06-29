package it.polimi.ingsw.model.messageModel;

import java.io.Serializable;
import java.rmi.Remote;

public abstract class Message implements Serializable, Remote {
    public void printMessage(){
        System.out.println("Message");
    }
}
