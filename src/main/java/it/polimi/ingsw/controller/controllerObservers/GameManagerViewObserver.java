package it.polimi.ingsw.controller.controllerObservers;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.server.MyRemoteInterface;

public interface GameManagerViewObserver {
    void onSetCredentials(String username, String password, MyRemoteInterface stub);
    void onSelectGame(String gameId, String user, MyRemoteInterface stub);
    void onCreateGame(int numOfPlayers, String user, MyRemoteInterface stub);
    void onLookForNewGames(String user, MyRemoteInterface stub);
}
