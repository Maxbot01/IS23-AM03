package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.controller.controllerObservers.LobbyViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.modelSupport.Client;
import it.polimi.ingsw.view.View;

import java.util.HashMap;

public class LobbyController extends Controller implements LobbyViewObserver, Subscriber {

    private String ID;
    private boolean isFirstLobbyMessage;
    public LobbyInfoMessage lastLobbyMessage;
    private Thread lastThread;
    public LobbyController(View view, String ID) {
        super(view);
        this.ID = ID;
        this.isFirstLobbyMessage = true;
        ClientManager.pubsub.addSubscriber(TopicType.lobbyState, this);
        ClientManager.pubsub.addSubscriber(TopicType.errorMessageState, this);
    }

    public String getID() {
        return ID;
    }

    @Override
    public void onStartMatch(String ID, String user) { // The username sent is the host's username
        //virtualGameLobby.startMatch(ID, user);
        ClientManager.virtualGameManager.startMatch(ID, user);
    }

    @Override
    public void onGetHost() {
    }
    @Override
    public void onGetPlayers() {
    }
    @Override
    public boolean receiveSubscriberMessages(Message message) {
        if(message instanceof LobbyInfoMessage mess) {
            //received a lobby info message, shows info about the lobby
            if(this.isFirstLobbyMessage) {
                this.isFirstLobbyMessage = false;
                this.lastLobbyMessage = mess;
                ClientManager.view.launchGameLobby(mess.ID, mess.players, mess.host);
            }else{
                for(String s: mess.players){
                    if(!lastLobbyMessage.players.contains(s)){
                        ClientManager.view.addNewLobbyPlayer(s);
                    }
                }
                this.lastLobbyMessage = mess;
            }
            /*if(lastThread != null){
                System.out.println("launchGameLobby "+lastThread.getName()+" interrupted");
                lastThread.interrupt();
            }else{
                System.out.println("First launchGameLobby");
            }
            this.lastThread = Thread.currentThread();
            System.out.println("launchGameLobby Thread name: "+Thread.currentThread().getName());
            if(!mess.lastLobbyMessage) {
                ClientManager.view.launchGameLobby(mess.ID, mess.players, mess.host);
            }else{
                this.lastThread.interrupt();
                System.out.println("Last Lobby thread interrupted");
            }*/
        }else if(message instanceof ErrorMessage mess){
            System.out.println("errorMessage in LobbyController");//DEBUG
            switch (mess.error.toString()) {
                case "notEnoughPlayers", "onlyHostCanStartMatch":
                    System.out.println("error case in LobbyController: "+mess.error.toString());
                    ClientManager.view.launchGameLobby(lastLobbyMessage.ID,lastLobbyMessage.players,lastLobbyMessage.host);
                    break;
            }
        }
        return true;
    }
    @Override
    public String onGetChatMessage(String msg){
        return msg; //TODO: fix this method with the correspondent virtual section
    }
}
