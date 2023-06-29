package it.polimi.ingsw.model.modelSupport.exceptions;

import java.io.Serializable;

public class ShelfFullException extends Exception implements Serializable {
    public String info;
    public ShelfFullException(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

}
