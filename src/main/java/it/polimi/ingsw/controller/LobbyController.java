package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.controllerObservers.LobbyViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.virtual_model.VirtualGameLobby;
import it.polimi.ingsw.view.View;

public class LobbyController extends Controller implements LobbyViewObserver, Subscriber {

    private VirtualGameLobby virtualGameLobby;
    LobbyController(View view, VirtualGameLobby virtualGameLobby ) {
        super(view);
        this.virtualGameLobby = virtualGameLobby;
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
        return false;
    }

    @Override
    public String onGetChatMessage(String msg){
        return msg; //TODO: fix this method with the correspondent virtual section
    }
}
