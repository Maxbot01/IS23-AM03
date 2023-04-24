package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.controllerObservers.GameManagerViewObserver;
import it.polimi.ingsw.controller.controllerObservers.GameViewObserver;
import it.polimi.ingsw.controller.pubSub.PubSubMessage;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.virtual_model.VirtualGame;
import it.polimi.ingsw.model.virtual_model.VirtualGameLobby;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class GameController extends Controller implements GameViewObserver, Subscriber {
    private VirtualGame virtualGame;
    GameController(View view, VirtualGame virtualGame) {
        super(view);
        this.virtualGame = virtualGame;
    }

    @Override
    public void onSelectedCards(ArrayList<Pair<Integer, Integer>> selected) {
        //view has selected cards
        virtualGame.selectedCards(selected);
    }

    @Override
    public void onSelectedColumn(ArrayList<BoardCard> selCards, Integer colIndex) {
        //view has selected columns
        virtualGame.selectedColumn(selCards, colIndex);
    }

    @Override
    public void onAcceptFinishedGame() {
        //view has accepted finished game
        //virtualGame.acceptFinishedGame();
    }


    @Override
    public boolean receiveSubscriberMessages(Message message) {
        //a message has been received
        //should receive matchStateMessages only
        //after it receives it, updates the view accordingly

        return true;
    }
    //every controller HAS to be subscribed to a topic and HAS to observe the view
    /*
    Types of messages
     */
}
