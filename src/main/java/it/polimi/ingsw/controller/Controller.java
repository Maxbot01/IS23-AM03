package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.model.virtual_model.VirtualGame;
import it.polimi.ingsw.model.virtual_model.VirtualGameLobby;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObservable;

public abstract class Controller implements Subscriber{
    private View view;

    Controller(View view){
        this.view = view;
    }
}
