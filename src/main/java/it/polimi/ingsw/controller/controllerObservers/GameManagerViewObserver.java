package it.polimi.ingsw.controller.controllerObservers;

public interface GameManagerViewObserver {
    void onSetUsername(String username);
    void onSetPassword(String password);
    void onSelectGame(String gameId);
    void onCreateGame(int numOfPlayers);
}
