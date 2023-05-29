package it.polimi.ingsw.controller.controllerObservers;

import it.polimi.ingsw.model.helpers.Pair;

public interface GameManagerViewObserver {
    void onSetCredentials(String username, String password);
    void onSelectGame(String gameId, String user);
    void onCreateGame(int numOfPlayers, String user);
    void onLookForNewGames(String user);
}
