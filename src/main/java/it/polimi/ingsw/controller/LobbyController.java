package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.client.ClientManager;
import it.polimi.ingsw.controller.controllerObservers.LobbyViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.messageModel.ChatMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.view.View;

import static it.polimi.ingsw.controller.client.ClientMain.stub;


/**
 * This controller menages the lobby phase, getting messages from the server, updating the view, and responding to user inputs
 */
public class LobbyController extends Controller implements LobbyViewObserver, Subscriber {

    private final String ID;
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

    /**
     * User requests the match to start
     * @param ID
     * @param user
     */
    @Override
    public void onStartMatch(String ID, String user) { // The username sent is the host's username
        ClientManager.virtualGameManager.startMatch(ID, user, stub);
    }


    @Override
    public void onGetHost() {
    }
    @Override
    public void onGetPlayers() {
    }

    /**
     * Method needed to handle the messages published from the topics of which the user is subscribed
     * @param message
     * @return
     */
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

        }else if(message instanceof ErrorMessage mess){
            switch (mess.error.toString()) {
                case "notEnoughPlayers", "onlyHostCanStartMatch", "notEveryoneReady":
                    //System.out.println("error case in LobbyController: "+mess.info);
                    ClientManager.view.showErrorMessage(mess.info);
                    ClientManager.view.launchGameLobby(lastLobbyMessage.ID,lastLobbyMessage.players,lastLobbyMessage.host);
                    break;
            }
        } else if (message instanceof ChatMessage mess) {
            ClientManager.view.printChat(mess.messages);
        }
        return true;
    }
    @Override
    public void onSendChatMessage(String message,String toUser){
        ClientManager.virtualGameManager.receiveChatMessage(this.ID,toUser,ClientManager.userNickname,message,false,false,stub);
    }
    @Override
    public void onGetChat(boolean fullChat){
        ClientManager.virtualGameManager.receiveChatMessage(this.ID,null,ClientManager.userNickname,null,fullChat,false,stub);
    }
}
