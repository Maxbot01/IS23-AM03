package it.polimi.ingsw.controller.controllerObservers;

public interface LobbyViewObserver{
    //lobby controller(s) have to manage these
    void onStartMatch();
    void onGetHost();
    void onGetPlayers();
}