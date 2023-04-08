package it.polimi.ingsw.controller.pubSub;

public interface Subscriber {
    boolean receiveSubscriberMessages(PubSubMessage message);
}
