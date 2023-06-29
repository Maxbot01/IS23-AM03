package it.polimi.ingsw.controller;


import it.polimi.ingsw.controller.client.ClientManager;
import it.polimi.ingsw.controller.controllerObservers.GameManagerViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.model.MyRemoteInterface;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.List;

/**
 * The game manager controller manages the messages relative to the login of the user and shows the available messages to the view. It also manages the creation of games
 */
public class GameManagerController extends Controller implements GameManagerViewObserver, Subscriber {
    private final VirtualGameManager virtualGameManager;
    private loginGameMessage lastLoginMessage = null;
    private Thread lastThread;
    public GameManagerController(View view, VirtualGameManager virtualGameManager, MyRemoteInterface stub) {
        super(view);
        this.virtualGameManager = virtualGameManager;
        ClientManager.pubsub.addSubscriber(TopicType.gameManagerState, this);
        ClientManager.pubsub.addSubscriber(TopicType.errorMessageState, this);
        ClientManager.pubsub.addSubscriber(TopicType.networkMessageState, this);
    }

    /**
     * User has set credentials
     * @param username
     * @param password
     * @param stub
     */
    @Override
    public void onSetCredentials(String username, String password, MyRemoteInterface stub) {
        ClientManager.userNickname = username;
        virtualGameManager.setCredentials(username, password, stub);
    }

    /**
     * User has selected a game
     * @param gameId
     * @param user
     * @param stub
     */
    @Override
    public void onSelectGame(String gameId, String user, MyRemoteInterface stub) {
        virtualGameManager.selectGame(gameId, user, stub);
    }

    /**
     * User has created a game
     * @param numOfPlayers
     * @param user
     * @param stub
     */
    @Override
    public void onCreateGame(int numOfPlayers, String user, MyRemoteInterface stub) {
        virtualGameManager.createGame(numOfPlayers, user, stub);
    }

    /**
     * User wants info about new games
     * @param user
     * @param stub
     */
    @Override
    public void onLookForNewGames(String user, MyRemoteInterface stub){
        virtualGameManager.lookForNewGames(user, stub);
    }

    /**
     * Method needed to handle the messages published from the topics of which the user is subscribed
     * @param message
     * @return
     * @throws IOException
     */
    @Override
    public boolean receiveSubscriberMessages(Message message) throws IOException {
        if(message instanceof NetworkMessage){
            //this message holds Messages useful for network
            switch (((NetworkMessage) message).message){
                case "pong":
                    System.out.println(ClientManager.gameManagerController);
                    ClientManager.view.requestCredentials();
                    break;
                default:
                    break;
            }
        }else if(message instanceof ErrorMessage mess){
            switch (mess.error.toString()) {
                case "wrongPassword":
                    ClientManager.view.showErrorMessage(mess.info);
                    ClientManager.view.requestCredentials();
                    break;
                case "lobbyIsFull":
                    ClientManager.view.showErrorMessage(mess.info);
                    ClientManager.view.launchGameManager(lastLoginMessage.gamesPlayers);
                    break;
            }
        }else if(message instanceof loginGameMessage mess){
            //user can go in, launchGameManager phase
            if(lastLoginMessage == null){
                this.lastLoginMessage = mess;
                ClientManager.view.launchGameManager(this.lastLoginMessage.gamesPlayers);
            }else{
                String addedGameId = null;
                List<String> addedGamePlayers = null;
                //Check needed to add a new game or update an old one with a new player entry
                if(lastLoginMessage.gamesPlayers.keySet().containsAll(mess.gamesPlayers.keySet())){
                    boolean found = false;
                    for(String key: mess.gamesPlayers.keySet()){
                        for(String playerName: mess.gamesPlayers.get(key)){
                            if(!lastLoginMessage.gamesPlayers.get(key).contains(playerName)){
                                addedGameId = key;
                                addedGamePlayers = mess.gamesPlayers.get(key);
                                found = true;
                                break;
                            }
                        }
                        if(found){
                            break;
                        }
                    }
                }else{
                    for(String s: mess.gamesPlayers.keySet()){
                        if(!lastLoginMessage.gamesPlayers.containsKey(s)){
                            addedGameId = s;
                            addedGamePlayers = mess.gamesPlayers.get(s);
                            break;
                        }
                    }
                }
                this.lastLoginMessage = (loginGameMessage)message;
                Pair<String, List<String>> addedGame = new Pair<>(addedGameId, addedGamePlayers);
                ClientManager.view.addNewGame(addedGame);
            }
        }
        return true;
    }
}
