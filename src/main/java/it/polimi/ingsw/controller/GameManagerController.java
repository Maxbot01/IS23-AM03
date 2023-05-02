package it.polimi.ingsw.controller;


import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.controller.controllerObservers.GameManagerViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.MatchStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedCardsMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedColumnsMessage;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class GameManagerController extends Controller implements GameManagerViewObserver, Subscriber {
    private VirtualGameManager virtualGameManager;


    public GameManagerController(View view, VirtualGameManager virtualGameManager) {
        super(view);
        this.virtualGameManager = virtualGameManager;
        ClientManager.pubsub.addSubscriber(TopicType.gameManagerState, this);
        virtualGameManager.ping();
    }


    @Override
    public void onSetCredentials(String username, String password) {
        virtualGameManager.setCredentials(username, password);
    }


    @Override
    public void onSelectGame(String gameId) {
        //dovrà chiamare la print della nuova lobby per la cli, così da mostrarlo al giocatore
    }

    @Override
    public void onCreateGame(int numOfPlayers) {
        //dovrà chiamare la print della nuova lobby per la cli, così da mostrarlo al giocatore
    }

    @Override
    public boolean receiveSubscriberMessages(Message message) {
        if(message instanceof NetworkMessage){

            //this message holds Messages useful for network
            switch (((NetworkMessage) message).message){
                case "pong":
                    ClientManager.view.requestCredentials();
                    break;
                default:
                    break;
            }
        }else if(message instanceof loginGameMessage){
            //user can go in
            ClientManager.view.launchGameManager(new ArrayList<>(((loginGameMessage)message).currentGames.keySet()));
        }
        return true;
    }
}
