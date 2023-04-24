package it.polimi.ingsw.controller.controllerObservers;

public interface GameManagerViewObserver {
    void onSelectGame();
    void onCreateGame(int numOfPlayers);
}
