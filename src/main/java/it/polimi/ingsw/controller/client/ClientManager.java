package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameManagerController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.controller.pubSub.PubSubService;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.MyRemoteInterface;
import it.polimi.ingsw.model.messageModel.ChatMessage;
import it.polimi.ingsw.model.messageModel.GameManagerMessage;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.lobbyMessages.LobbyInfoMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.*;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.view.CLIgeneral;
import it.polimi.ingsw.view.GUIView.GUIView;
import it.polimi.ingsw.view.View;

import java.io.IOException;

/**
 * The ClientManager class manages the client-side operations and controllers for the game.
 */
public class ClientManager {

    public static boolean isSocketClient;
    //SINGLETON
    private static ClientManager instance;
    public static PubSubService pubsub;
    public static View view;
    public static GameController gameController;
    public static GameManagerController gameManagerController;
    public static LobbyController lobbyController;

    public static VirtualGameManager virtualGameManager;

    public static String userNickname;
    public static boolean isCli;

    public static String clientIP;

    /**
     * Initializes the ClientManager singleton instance.
     *
     * @param isCLI           Flag indicating if the client is using CLI.
     * @param isSocketClient  Flag indicating if the client is using socket-based communication.
     * @param remoteObject    The remote object for RMI-based communication.
     * @param ipAddress       The IP address for RMI-based communication.
     * @return The initialized ClientManager instance.
     */
    public static ClientManager initializeClientManagerSingleton(boolean isCLI, boolean isSocketClient, MyRemoteInterface remoteObject, String ipAddress) {
        if (instance == null) {
            instance = new ClientManager(isCLI, isSocketClient, remoteObject, ipAddress);
        }
        return instance;
    }

    //getter userNickname
    public static String getUserNickname() {
        return userNickname;
    }

    private ClientManager(boolean isCLI, boolean isSocketClient, MyRemoteInterface remoteObject, String ipAddress) {
        gameController = null;
        lobbyController = null;
        pubsub = new PubSubService();
        isCli = isCLI;
        if (isCLI) {
            //cli mode
            view = new CLIgeneral();
            //gameController = new GameController(new CLIgeneral(), new VirtualGame());
        } else {
            view = new GUIView();
        }
        if (!(isSocketClient)) {
            clientIP = ipAddress;
        } else {
            clientIP = null;
        }
        //System.out.println("Client IP: " + clientIP);

        virtualGameManager = new VirtualGameManager(isSocketClient, remoteObject);
        gameManagerController = new GameManagerController(view, virtualGameManager, remoteObject);
        view.registerObserver(gameManagerController, null, null);
        virtualGameManager.ping(remoteObject);
    }

    /**
     * Creates new instances of controllers based on the received game ID.
     *
     * @param ID The game ID.
     */
    public static void createdControllers(String ID) {
        //GameManagerController sees that a game has been created with an ID, the game controller gets instantiated
        //unsubscribes previous controllers and subscribes the new ones
        boolean created = false;
        if (gameController != null) {
            if (!gameController.getGameID().equals(ID)) {
                pubsub.removeSubscriber(TopicType.matchState, gameController);
                gameController = new GameController(view, ID);
                created = true;
            }
        } else {
            gameController = new GameController(view, ID);
            created = true;
        }
        if (lobbyController != null) {
            if (!lobbyController.getID().equals(ID)) {
                pubsub.removeSubscriber(TopicType.lobbyState, lobbyController);
                lobbyController = new LobbyController(view, ID);
                created = true;
            }
        } else {
            lobbyController = new LobbyController(view, ID);
            created = true;
        }
        if (created) {
            view.registerObserver(gameManagerController, lobbyController, gameController);
        }
    }

    /**
     * Receives a message from the server and dispatches it to the appropriate controllers.
     *
     * @param receivedMessageDecoded The received message.
     * @throws IOException If an I/O error occurs.
     */
    public static void clientReceiveMessage(Message receivedMessageDecoded) throws IOException {
        if (receivedMessageDecoded instanceof NetworkMessage) {
            //received a network message (like ping or request of username)
            pubsub.publishMessage(TopicType.networkMessageState, receivedMessageDecoded);
        } else if (receivedMessageDecoded instanceof ErrorMessage) {
            pubsub.publishMessage(TopicType.errorMessageState, receivedMessageDecoded);
        } else if (receivedMessageDecoded instanceof GameManagerMessage) {
            pubsub.publishMessage(TopicType.gameManagerState, receivedMessageDecoded);
        } else if (receivedMessageDecoded instanceof LobbyInfoMessage) {
            //Crea dei nuovi controller ogni volta che gli arriva un messaggio, in base alle condizioni
            createdControllers(((LobbyInfoMessage) receivedMessageDecoded).ID);
            pubsub.publishMessage(TopicType.lobbyState, receivedMessageDecoded);
        } else if (receivedMessageDecoded instanceof InitStateMessage) {
            //message with all the info about the game
            pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
        } else if (receivedMessageDecoded instanceof GameStateMessage) {
            //message with all the info about the game
            pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
        } else if (receivedMessageDecoded instanceof SelectedCardsMessage) {
            pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
        } else if (receivedMessageDecoded instanceof loginGameMessage) {
            pubsub.publishMessage(TopicType.gameManagerState, receivedMessageDecoded);
        } else if (receivedMessageDecoded instanceof SelectedColumnsMessage) {
            pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
        } else if (receivedMessageDecoded instanceof FinishedGameMessage) {
            pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
        } else if (receivedMessageDecoded instanceof ChatMessage) {
            if (((ChatMessage) receivedMessageDecoded).inGame) {
                pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
            } else {
                pubsub.publishMessage(TopicType.lobbyState, receivedMessageDecoded);
            }
        }
    }

}
