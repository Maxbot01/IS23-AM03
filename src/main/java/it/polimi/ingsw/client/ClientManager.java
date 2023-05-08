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
import it.polimi.ingsw.model.messageModel.matchStateMessages.GameStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.InitStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedCardsMessage;
import it.polimi.ingsw.model.virtual_model.VirtualGame;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.view.CLIgeneral;
import it.polimi.ingsw.view.View;

public class ClientManager {
    //SINGLETON
    private static ClientManager instance;
    public static PubSubService pubsub;
    public static View view;
    private static GameController gameController;
    public static GameManagerController gameManagerController;
    private static LobbyController lobbyController;

    public static VirtualGameManager virtualGameManager;

    public static String userNickname; // a cosa gli serve???
    public static String userUID;

    public ClientManager(boolean isCLI){
        gameController = null;
        lobbyController = null;
        pubsub = new PubSubService();
        if(isCLI){
            //cli mode
            view = new CLIgeneral();
            virtualGameManager = new VirtualGameManager();
            gameManagerController = new GameManagerController(view, virtualGameManager);
            view.registerObserver(gameManagerController, null, null);
            //gameController = new GameController(new CLIgeneral(), new VirtualGame());
        }else{
            // gui mode
        }
    }


    public static void createdControllers(String ID){
        //GameManagerController sees that a game has been created with an ID, the game controller gets instantiated
       //unsubscribes previous controllers anc subsucribes the new ones
        //TODO: check if the ones created has the same id if it does do not remove
        if(gameController != null){
            pubsub.removeSubscriber(TopicType.matchState, gameController);
        }
        if(lobbyController != null){
            pubsub.removeSubscriber(TopicType.lobbyState, lobbyController);
        }
        gameController = new GameController(view, new VirtualGame(), ID);
        lobbyController = new LobbyController(view, ID);
        view.registerObserver(gameManagerController, lobbyController, gameController);
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
        }
    }
}
