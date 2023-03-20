package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.messageModel.Message;

public interface SenderConnectionStrategy {
    void sendMessage(Message message);
}
