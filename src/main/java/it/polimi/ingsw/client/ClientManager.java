package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameManagerController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.controller.pubSub.PubSubService;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.messageModel.GameManagerMessage;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.*;
import it.polimi.ingsw.model.virtual_model.VirtualGame;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.server.MyRemoteInterface;
import it.polimi.ingsw.view.CLIgeneral;
import it.polimi.ingsw.view.GUIView.GUIView;
import it.polimi.ingsw.view.View;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.util.UUID;

public class ClientManager {

    public static it.polimi.ingsw.controller.GameManagerController GameManagerController;
    //SINGLETON
    private static ClientManager instance;
    public static PubSubService pubsub;
    public static View view;
    public static GameController gameController;
    public static GameManagerController gameManagerController;
    public static LobbyController lobbyController;

    public static VirtualGameManager virtualGameManager;

    public static String userNickname;
    public boolean isCli;

    public static String clientIP;

    // rest of the class

    // Private constructor to prevent instantiation from outside the class

    // Public static method to get the singleton instance and be sure to never initialize the ClientManager twice
    public static ClientManager initializeClientManagerSingleton(boolean isCLI, boolean isSocketClient, MyRemoteInterface remoteObject ) {
        if (instance == null) {
            instance = new ClientManager(isCLI, isSocketClient, remoteObject);
        }
        return instance;
    }

    private ClientManager(boolean isCLI, boolean isSocketClient, MyRemoteInterface remoteObject){
        gameController = null;
        lobbyController = null;
        pubsub = new PubSubService();
        isCli = isCLI;
        if(isCLI){
            //cli mode
            view = new CLIgeneral();
            //gameController = new GameController(new CLIgeneral(), new VirtualGame());
        }else{
            view = new GUIView();
        }
        if (!(isSocketClient)) {
            try {
                clientIP = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                System.out.println("Error getting local IP address: " + e.getMessage());
                clientIP = "Unknown"; // Valore di fallback in caso di eccezione
            }
        } else {
            clientIP = null;
        }
        System.out.println("Client IP: " + clientIP);

        virtualGameManager = new VirtualGameManager(isSocketClient, remoteObject);
        System.out.println("GameManagerController: " + ClientManager.gameManagerController);
        gameManagerController = new GameManagerController(view, virtualGameManager);
        view.registerObserver(gameManagerController, null, null);
    }

    /*public static void startReceivingCommands(){ CLIInputThread
        try {
            view.readInput();
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName()+" relative to the commands input is interrupted");
        }
    }*/



    public static void createdControllers(String ID){
        //GameManagerController sees that a game has been created with an ID, the game controller gets instantiated
       //unsubscribes previous controllers anc subsucribes the new ones
        //TODO: check if the ones created has the same id if it does do not remove
        //Changes: I've put the "new" inside every second nested if, and added a check for the registerObserver
        boolean created = false;
        if(gameController != null){
            if(!gameController.getGameID().equals(ID)) {
                pubsub.removeSubscriber(TopicType.matchState, gameController);
                gameController = new GameController(view, new VirtualGame(), ID);
                created = true;
            }
        }else{
            gameController = new GameController(view, new VirtualGame(), ID);
            created = true;
        }
        if(lobbyController != null){
            if(!lobbyController.getID().equals(ID)) {
                pubsub.removeSubscriber(TopicType.lobbyState, lobbyController);
                lobbyController = new LobbyController(view, ID);
                created = true;
            }
        }else{
            lobbyController = new LobbyController(view, ID);
            created = true;
        }
        if(created) {
            view.registerObserver(gameManagerController, lobbyController, gameController);
        }
    }

    //accessible from ClientMain (socket) and RMI
    public static void clientReceiveMessage(Message receivedMessageDecoded){

        if(receivedMessageDecoded instanceof NetworkMessage){
            //received a network message (like ping or request of username)
            pubsub.publishMessage(TopicType.networkMessageState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof ErrorMessage){
            pubsub.publishMessage(TopicType.errorMessageState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof GameManagerMessage){
            pubsub.publishMessage(TopicType.gameManagerState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof LobbyInfoMessage){
            //Crea dei nuovi controller ogni volta che gli arriva un messaggio, in base alle condizioni
            createdControllers(((LobbyInfoMessage)receivedMessageDecoded).ID);
            pubsub.publishMessage(TopicType.lobbyState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof InitStateMessage){
            //message with all the info about the game
            pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof GameStateMessage){
            //message with all the info about the game
            pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof SelectedCardsMessage){
            pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof loginGameMessage){
            pubsub.publishMessage(TopicType.gameManagerState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof SelectedColumnsMessage){
            pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof FinishedGameMessage){
            pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
        }
    }
}
