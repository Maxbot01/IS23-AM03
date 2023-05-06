package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.controller.controllerObservers.GameViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.matchStateMessages.*;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.model.virtual_model.VirtualGame;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class GameController extends Controller implements GameViewObserver, Subscriber {
    private VirtualGame virtualGame;

    private InitStateMessage latestInit;
    private String gameID;
    public GameController(View view, VirtualGame virtualGame, String gameID) {
        super(view);
        this.virtualGame = virtualGame;
        this.gameID = gameID;
        //adds itself to the subscribers
        ClientManager.pubsub.addSubscriber(TopicType.matchState, this);
    }

    @Override
    public void onSelectedCards(ArrayList<Pair<Integer, Integer>> selected, String user) {
        //view has selected cards
        //check if selection was correct
        if(isSelectionPossible(selected)){
            //ClientManager.view.chooseColumn();
            ClientManager.virtualGameManager.selectedCards(selected, user, gameID);
        }else{
            ClientManager.view.showErrorMessage("Every chosen card must be adiacent to at least another chosen card");
            ClientManager.view.chooseCards();
        }
        virtualGame.selectedCards(selected);
    }

    //TODO: make this!!!
    private boolean isSelectionPossible(ArrayList<Pair<Integer, Integer>> selected){
        //TODO: check if is the selection is right
        //latestInit.selecex
        return true;
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
        if(message instanceof InitStateMessage){
            InitStateMessage mess = (InitStateMessage)message;
            //if the message was for this client send ack
            ClientManager.view.initializeGame(mess.players, mess.commonGoals, mess.personalGoals, mess.chairedPlayer);
            ClientManager.view.updatedMatchDetails(mess.pieces, mess.selecectables, mess.playersShelves, mess.gameState);

            ClientManager.view.showPlayingPlayer(mess.chairedPlayer); // prints the playing layer at the beginning of the turn
            if (mess.chairedPlayer.equals(ClientManager.userNickname)){
                latestInit = mess;
                ClientManager.virtualGameManager.sendAck();
                ClientManager.view.chooseCards();
            }else{
                ClientManager.view.waitingCommands();
            }

        }else if(message instanceof GameStateMessage){
            //received info about the match

        }else if(message instanceof SelectedCardsMessage){
            ClientManager.view.chooseColumn();

        }else if(message instanceof SelectedColumnsMessage){
            SelectedColumnsMessage mess = (SelectedColumnsMessage)message;
            ClientManager.view.updatedMatchDetails(mess.pieces, mess.selectables, mess.updatedPlayerShelf, mess.gameState, mess.updatedPoints);
            ClientManager.view.showPlayingPlayer(mess.newPlayer);
        }
        return true;
    }
    //every controller HAS to be subscribed to a topic and HAS to observe the view
    /*
    Types of messages
     */
    @Override
    public String onGetChatMessage(String msg){
        return msg; //TODO: fix this method with the correspondent virtual section
    }
}
