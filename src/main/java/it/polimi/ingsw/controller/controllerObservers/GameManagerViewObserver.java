package it.polimi.ingsw.controller.controllerObservers;

public interface GameManagerViewObserver {
    void onSelectGame(String gameId);
    void onCreateGame(int numOfPlayers);
}
