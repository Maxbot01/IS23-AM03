package it.polimi.ingsw.controller.controllerObservers;

public interface LobbyViewObserver{
    //lobby controller(s) have to manage these
    void onStartMatch(String ID, String user);
    void onGetHost();
    void onGetPlayers();
    void onSendChatMessage(String message);
    void onGetChat(boolean fullChat);
}