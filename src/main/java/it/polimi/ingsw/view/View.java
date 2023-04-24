package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.controllerObservers.GameManagerViewObserver;
import it.polimi.ingsw.controller.controllerObservers.GameViewObserver;
import it.polimi.ingsw.controller.controllerObservers.LobbyViewObserver;

import java.util.ArrayList;

public abstract class View implements ViewObservable, UpdateHandler{

    protected GameManagerViewObserver gameManagerController;
    protected LobbyViewObserver lobbyController;
    protected GameViewObserver gameController;

    /**
     * Register the 3 observers of the different parts (for now)
     * @param gameManagerController
     * @param lobbyController
     * @param gameController
     */
    @Override
    public void registerObserver(GameManagerViewObserver gameManagerController, LobbyViewObserver lobbyController, GameViewObserver gameController) {
        this.gameManagerController = gameManagerController;
        this.lobbyController = lobbyController;
        this.gameController = gameController;
    }

}
