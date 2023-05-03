package it.polimi.ingsw.model.messageModel.errorMessages;

import it.polimi.ingsw.model.messageModel.Message;

public class ErrorMessage extends Message {
    public ErrorType error;
    public ErrorMessage(ErrorType error){
        this.error = error;
    }
}
