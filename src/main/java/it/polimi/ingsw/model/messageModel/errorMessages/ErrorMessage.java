package it.polimi.ingsw.model.messageModel.errorMessages;

import it.polimi.ingsw.model.messageModel.Message;

public class ErrorMessage extends Message {
    public ErrorType error;
    ErrorMessage(ErrorType error){
        this.error = error;
    }
}
