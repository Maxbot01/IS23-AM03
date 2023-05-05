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
    private String gameID;
    public GameController(View view, VirtualGame virtualGame, String gameID) {
        super(view);
        this.virtualGame = virtualGame;
        this.gameID = gameID;
        //adds itself to the subscribers
        ClientManager.pubsub.addSubscriber(TopicType.matchState, this);
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
        if(message instanceof InitStateMessage){
            InitStateMessage mess = (InitStateMessage)message;
            //if the message was for this client send ack
            ClientManager.view.initializeGame(mess.players, mess.commonGoals, mess.personalGoals, mess.chairedPlayer);
            ClientManager.view.updatedMatchDetails(mess.pieces, mess.selecectables, mess.playersShelves, mess.gameState);

            if (mess.chairedPlayer.equals(ClientManager.userNickname)){
                ClientManager.virtualGameManager.sendAck();
                boolean selectionDone = false;
                while(!selectionDone){
                    try{
                        ClientManager.view.startGameSequence();
                    }catch (UnselectableCardException e){
                        //not selectable
                        break;
                    }
                    selectionDone = true;
                }
            }

        }else if(message instanceof GameStateMessage){
            //received info aboiut the match

        }else if(message instanceof SelectedCardsMessage){

        }else if(message instanceof SelectedColumnsMessage){

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
