package it.polimi.ingsw.controller.controllerObservers;

public interface GameManagerViewObserver {
    void onSetCredentials(String username, String password);
    void onSelectGame(String gameId, String user);
    void onCreateGame(int numOfPlayers, String user);
}
