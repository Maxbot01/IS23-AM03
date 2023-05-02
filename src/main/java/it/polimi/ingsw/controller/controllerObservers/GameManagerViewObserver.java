package it.polimi.ingsw.controller.controllerObservers;

public interface GameManagerViewObserver {
    void onSetCredentials(String username, String password);
    void onSelectGame(String gameId);
    void onCreateGame(int numOfPlayers);
}
