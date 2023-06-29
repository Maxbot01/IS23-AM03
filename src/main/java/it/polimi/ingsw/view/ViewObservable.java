package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.controllerObservers.GameManagerViewObserver;
import it.polimi.ingsw.controller.controllerObservers.GameViewObserver;
import it.polimi.ingsw.controller.controllerObservers.LobbyViewObserver;

public interface ViewObservable {
    void registerObserver(GameManagerViewObserver gameManagerObserver, LobbyViewObserver lobbyObserver, GameViewObserver gameObserver);
}

