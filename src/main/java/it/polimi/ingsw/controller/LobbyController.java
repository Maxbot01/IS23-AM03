package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.controller.controllerObservers.LobbyViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.view.View;

public class LobbyController extends Controller implements LobbyViewObserver, Subscriber {

    public LobbyController(View view) {
        super(view);
        ClientManager.pubsub.addSubscriber(TopicType.lobbyState, this);
    }
    @Override
    public void onStartMatch(String ID, String user) {
        //virtualGameLobby.startMatch(ID, user);
        ClientManager.virtualGameManager.startMatch(ID, user);
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
