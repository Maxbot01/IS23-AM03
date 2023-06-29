package it.polimi.ingsw.model.messageModel.errorMessages;

import it.polimi.ingsw.model.messageModel.Message;

public class ErrorMessage extends Message {
    public String info;
    public ErrorType error;
    public ErrorMessage(ErrorType error, String info){
        this.info = info;
        this.error = error;
    }
}
