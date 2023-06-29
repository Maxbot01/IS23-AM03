package it.polimi.ingsw.controller.pubSub;

import it.polimi.ingsw.model.messageModel.Message;

public interface Publisher {
    void publish(Message message, PubSubService pubSubService);
}
