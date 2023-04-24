package it.polimi.ingsw.controller;


import it.polimi.ingsw.controller.controllerObservers.GameManagerViewObserver;
import it.polimi.ingsw.controller.controllerObservers.GameViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.virtual_model.VirtualGameLobby;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.view.View;

public class GameManagerController extends Controller implements GameManagerViewObserver, Subscriber {
    private VirtualGameManager virtualGameManager;

    GameManagerController(View view, VirtualGameManager virtualGameManager) {
        super(view);
        this.virtualGameManager = virtualGameManager;
    }

    @Override
    public void onSelectGame() {

    }

    @Override
    public void onCreateGame(int numOfPlayers) {

    }

    @Override
    public boolean receiveSubscriberMessages(Message message) {
        return false;
    }
}
