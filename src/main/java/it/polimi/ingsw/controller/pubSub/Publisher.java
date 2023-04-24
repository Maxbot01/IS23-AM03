package it.polimi.ingsw.controller.pubSub;

public interface Publisher {
    void publish(PubSubMessage message, PubSubService pubSubService);
}
