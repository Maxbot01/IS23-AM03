package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.controller.controllerObservers.LobbyViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.virtual_model.VirtualGameLobby;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class LobbyController extends Controller implements LobbyViewObserver, Subscriber {

    private VirtualGameLobby virtualGameLobby;
    public LobbyController(View view, VirtualGameLobby virtualGameLobby ) {
        super(view);
        this.virtualGameLobby = virtualGameLobby;
        ClientManager.pubsub.addSubscriber(TopicType.lobbyState, this);
    }
    @Override
    public void onStartMatch() {
    }
    @Override
    public void onGetHost() {
    }
    @Override
    public void onGetPlayers() {
    }
    @Override
    public boolean receiveSubscriberMessages(Message message) {
        if(message instanceof LobbyInfoMessage){
            //received a lobby info message, shows info about the lobby
            LobbyInfoMessage castedMex = ((LobbyInfoMessage) message);
            ClientManager.view.launchGameLobby(castedMex.ID, castedMex.players, castedMex.host);
        }
        return true;
    }
    @Override
    public String onGetChatMessage(String msg){
        return msg; //TODO: fix this method with the correspondent virtual section
    }
}
