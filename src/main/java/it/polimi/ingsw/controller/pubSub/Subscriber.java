package it.polimi.ingsw.controller.pubSub;

import it.polimi.ingsw.model.messageModel.Message;

public interface Subscriber {
    boolean receiveSubscriberMessages(Message message);
}
