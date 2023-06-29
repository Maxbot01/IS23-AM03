package it.polimi.ingsw.model.modelSupport.exceptions;

import java.io.Serializable;

public class UnselectableCardException extends Exception implements Serializable {
    public String info;
    public UnselectableCardException(String info){
        this.info = info;
    };

}
