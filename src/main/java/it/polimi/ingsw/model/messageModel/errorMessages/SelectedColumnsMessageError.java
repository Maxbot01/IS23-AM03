package it.polimi.ingsw.model.messageModel.errorMessages;

public class SelectedColumnsMessageError extends ErrorMessage{
    private String message;
    public SelectedColumnsMessageError(String message) {
        super(ErrorType.valueOf(message));
        this.message = message;
    }
    //TODO: fill the error message class
}
