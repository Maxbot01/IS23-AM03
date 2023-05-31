package it.polimi.ingsw.model.modelSupport.exceptions;

public class ShelfFullException extends Exception{
    public String info;
    public ShelfFullException(String info) {
        this.info = info;
    }
}
