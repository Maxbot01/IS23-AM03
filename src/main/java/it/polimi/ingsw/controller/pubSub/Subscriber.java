package it.polimi.ingsw.controller.pubSub;

import it.polimi.ingsw.model.messageModel.Message;

import java.io.IOException;

public interface Subscriber {
    boolean receiveSubscriberMessages(Message message) throws IOException;
}
