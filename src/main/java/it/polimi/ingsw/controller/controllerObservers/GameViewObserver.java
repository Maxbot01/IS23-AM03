package it.polimi.ingsw.controller.controllerObservers;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;

import java.util.ArrayList;

public interface GameViewObserver {
    //The controller could give an error on this choice
    void onSelectedCards(ArrayList<Pair<Integer, Integer>> selected, String user);
    void onSelectedColumn(ArrayList<BoardCard> selCards, Integer colIndex, String user);
    void onAcceptFinishedGame();
    void startCardsSelection();
    void setReady(String gameID, String nickname);
    void onSendChatMessage(String message);
    void onGetChat(boolean fullChat);
}
