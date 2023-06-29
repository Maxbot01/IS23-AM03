package it.polimi.ingsw.controller.pubSub;

import it.polimi.ingsw.model.messageModel.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The PubSubService class handles the publish-subscribe mechanism for message distribution.
 */
public class PubSubService {
    /**
     * Each topic has its observers' controller.
     */
    HashMap<TopicType, ArrayList<Subscriber>> topicSubMap;

    /**
     * Constructs a new instance of PubSubService.
     */
    public PubSubService() {
        topicSubMap = new HashMap<>();
    }

    /**
     * Adds a subscriber to the specified topic.
     *
     * @param topic     The topic to subscribe to.
     * @param subscriber The subscriber to add.
     */
    public void addSubscriber(TopicType topic, Subscriber subscriber) {
        if (topicSubMap.containsKey(topic)) {
            ArrayList<Subscriber> subs = topicSubMap.get(topic);
            subs.add(subscriber);
            topicSubMap.put(topic, subs);
        } else {
            // Create the topic
            ArrayList<Subscriber> toAdd = new ArrayList<>();
            toAdd.add(subscriber);
            topicSubMap.put(topic, toAdd);
        }
    }

    /**
     * Removes an existing subscriber from the specified topic.
     *
     * @param topic     The topic to unsubscribe from.
     * @param subscriber The subscriber to remove.
     */
    public void removeSubscriber(TopicType topic, Subscriber subscriber) {
        if (topicSubMap.containsKey(topic)) {
            ArrayList<Subscriber> subscribers = topicSubMap.get(topic);
            subscribers.remove(subscriber);
            topicSubMap.put(topic, subscribers);
        }
    }

    /**
     * Publishes a message to the specified topic, notifying all subscribers.
     *
     * @param topic   The topic to publish the message to.
     * @param message The message to publish.
     * @throws IOException If an I/O error occurs.
     */
    public void publishMessage(TopicType topic, Message message) throws IOException {
        if (topicSubMap.containsKey(topic)) {
            for (Subscriber s : topicSubMap.get(topic)) {
                s.receiveSubscriberMessages(message);
            }
        } else {
            System.err.println("No topic exists");
        }
    }
}