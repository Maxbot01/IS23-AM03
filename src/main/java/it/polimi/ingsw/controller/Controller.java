package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.view.View;

public abstract class Controller implements Subscriber{
    private View view;

    /**
     * Controller constructor
     * @param view
     */
    public Controller(View view){
        this.view = view;
    }
}
