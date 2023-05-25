package it.polimi.ingsw.controller;


import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.controller.controllerObservers.GameManagerViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.MatchStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedCardsMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedColumnsMessage;
import it.polimi.ingsw.model.modelSupport.Client;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameManagerController extends Controller implements GameManagerViewObserver, Subscriber {
    private VirtualGameManager virtualGameManager;
    private loginGameMessage lastLoginMessage;
    private Thread lastThread;
    public GameManagerController(View view, VirtualGameManager virtualGameManager) {
        super(view);
        this.virtualGameManager = virtualGameManager;
        ClientManager.pubsub.addSubscriber(TopicType.gameManagerState, this);
        ClientManager.pubsub.addSubscriber(TopicType.errorMessageState, this);
        ClientManager.pubsub.addSubscriber(TopicType.networkMessageState, this);
        virtualGameManager.ping();
    }

    @Override
    public void onSetCredentials(String username, String password) {
        virtualGameManager.setCredentials(username, password);
    }

    @Override
    public void onSelectGame(String gameId, String user) {
        virtualGameManager.selectGame(gameId, user);
    }

    @Override
    public void onCreateGame(int numOfPlayers, String user) {
        virtualGameManager.createGame(numOfPlayers, user);
    }

    @Override
    public void onLookForNewGames(String user){
        virtualGameManager.lookForNewGames(user);
    }

    @Override
    public boolean receiveSubscriberMessages(Message message) {
        if(message instanceof NetworkMessage){

            //this message holds Messages useful for network
            switch (((NetworkMessage) message).message){
                case "pong":
                    ClientManager.view.requestCredentials();
                    break;
                default:
                    break;
            }
        }else if(message instanceof ErrorMessage mess){
            System.out.println("errorMessage in GameManagerController");//DEBUG
            //TODO: Uncomment this instruction -> ClientManager.view.showErrorMessage(mess.error.toString());
            switch (mess.error.toString()) {
                case "wrongPassword":
                    System.out.println("error case in GameManagerController: "+mess.error.toString());
                    ClientManager.view.requestCredentials();
                    break;
                case "lobbyIsFull":
                    System.out.println("error case in GameManagerController: "+mess.error.toString());
                    ClientManager.view.launchGameManager(lastLoginMessage.gamesPlayers);
                    break;
            }
        }else if(message instanceof loginGameMessage){
            //user can go in, launchGameManager phase
            this.lastLoginMessage = (loginGameMessage)message;
            if(ClientManager.userNickname == null){
                ClientManager.userNickname = lastLoginMessage.username;
                ClientManager.view.launchGameManager(this.lastLoginMessage.gamesPlayers);
            }else{
                int allGamesSize = lastLoginMessage.gamesPlayers.keySet().toArray().length;
                String addedGameId = lastLoginMessage.gamesPlayers.keySet().toArray()[allGamesSize-1].toString();
                List<String> addedGamePlayers = lastLoginMessage.gamesPlayers.get(addedGameId);
                Pair<String, List<String>> addedGame = new Pair<>(addedGameId,addedGamePlayers);
                ClientManager.view.addNewGame(addedGame);
            }



            /*if(lastThread != null){
                System.out.println("launchGameManager "+lastThread.getName()+" interrupted");
                lastThread.interrupt();
            }else{
                System.out.println("First launchGameManager");
            }
            this.lastThread = Thread.currentThread();
            System.out.println("launchGameManager Thread name: "+Thread.currentThread().getName());
            ClientManager.view.launchGameManager(this.lastLoginMessage.gamesPlayers);*/
        }
        return true;
    }
}
