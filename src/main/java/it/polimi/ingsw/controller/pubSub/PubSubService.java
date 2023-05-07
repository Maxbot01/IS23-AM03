package it.polimi.ingsw.controller.pubSub;

import it.polimi.ingsw.model.messageModel.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PubSubService {
    /**
     * Each topic has its observers controller
     */
    HashMap<TopicType, ArrayList<Subscriber>> topicSubMap;

    public PubSubService(){
        topicSubMap = new HashMap<>();
    }

    public void addSubscriber(TopicType topic, Subscriber subscriber){
        if(topicSubMap.containsKey(topic)){
            ArrayList<Subscriber> subs = topicSubMap.get(topic);
            subs.add(subscriber);
            topicSubMap.put(topic, subs);
        }else{
            //create the topic
            ArrayList<Subscriber> toAdd = new ArrayList<>();
            toAdd.add(subscriber);
            topicSubMap.put(topic, toAdd);

        }
    }

    //Remove an existing subscriber for a topic
    public void removeSubscriber(TopicType topic, Subscriber subscriber){
        if(topicSubMap.containsKey(topic)){
            ArrayList<Subscriber> subscribers = topicSubMap.get(topic);
            subscribers.remove(subscriber);
            topicSubMap.put(topic, subscribers);
        }

    }

    //possibility to hava a callback to ack if the sub received
    //TODO: use PubSubMessage instead of generic message
    public void publishMessage(TopicType topic, Message message){
        if (topicSubMap.containsKey(topic)){
            for (Subscriber s: topicSubMap.get(topic)) {
                s.receiveSubscriberMessages(message);
            }
        }else{
            //TODO: handle with exception noTopicExists
        }

    }

}
