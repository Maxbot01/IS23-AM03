package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.messageModel.GameManagerMessage;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.*;

import java.rmi.RemoteException;

import static it.polimi.ingsw.client.ClientManager.createdControllers;
import static it.polimi.ingsw.client.ClientManager.pubsub;

public class MyRemoteObject extends GameManager implements MyRemoteInterface {

    public MyRemoteObject() throws RemoteException {
        super();
    }

    @Override
    public void receiveMessage(Message withMessage, String rmiUID) {
        System.out.println("Received message from " + rmiUID + " with content: " + withMessage);
        if(withMessage instanceof NetworkMessage){
            //received a network message (like ping or request of username)
            pubsub.publishMessage(TopicType.networkMessageState, withMessage);
        }else if(withMessage instanceof ErrorMessage){
            pubsub.publishMessage(TopicType.errorMessageState, withMessage);
        }else if(withMessage instanceof GameManagerMessage){
            pubsub.publishMessage(TopicType.gameManagerState, withMessage);
        }else if(withMessage instanceof LobbyInfoMessage){
            //Crea dei nuovi controller ogni volta che gli arriva un messaggio, in base alle condizioni
            createdControllers(((LobbyInfoMessage)withMessage).ID);
            pubsub.publishMessage(TopicType.lobbyState, withMessage);
        }else if(withMessage instanceof InitStateMessage){
            //message with all the info about the game
            pubsub.publishMessage(TopicType.matchState, withMessage);
        }else if(withMessage instanceof GameStateMessage){
            //message with all the info about the game
            pubsub.publishMessage(TopicType.matchState, withMessage);
        }else if(withMessage instanceof SelectedCardsMessage){
            pubsub.publishMessage(TopicType.matchState, withMessage);
        }else if(withMessage instanceof loginGameMessage){
            pubsub.publishMessage(TopicType.gameManagerState, withMessage);
        }else if(withMessage instanceof SelectedColumnsMessage){
            pubsub.publishMessage(TopicType.matchState, withMessage);
        }else if(withMessage instanceof FinishedGameMessage){
            pubsub.publishMessage(TopicType.matchState, withMessage);
        };
    }

    // Implementazione dei metodi remoti dell'interfaccia MyRemoteInterface
}
