package it.polimi.ingsw.controller;


import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.MessageReceiver;
import it.polimi.ingsw.controller.controllerObservers.GameManagerViewObserver;
import it.polimi.ingsw.controller.controllerObservers.GameViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.InitStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.MatchStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedCardsMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedColumnsMessage;
import it.polimi.ingsw.model.virtual_model.VirtualGameLobby;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.view.View;

public class GameManagerController extends Controller implements GameManagerViewObserver, Subscriber {
    private VirtualGameManager virtualGameManager;

    public GameManagerController(View view, VirtualGameManager virtualGameManager) {
        super(view);
        this.virtualGameManager = virtualGameManager;
        MessageReceiver.pubsub.addSubscriber(TopicType.gameManagerState, this);
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

        }else if(message instanceof MatchStateMessage){

        }else if(message instanceof SelectedCardsMessage){

        }else if(message instanceof SelectedColumnsMessage){

        }
        return true;
    }
}
