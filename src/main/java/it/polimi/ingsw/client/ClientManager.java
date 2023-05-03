package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameManagerController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.controller.pubSub.PubSubService;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.messageModel.GameManagerMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.LobbyMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.MatchStateMessage;
import it.polimi.ingsw.model.virtual_model.VirtualGame;
import it.polimi.ingsw.model.virtual_model.VirtualGameLobby;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.view.CLIgeneral;
import it.polimi.ingsw.view.View;

public class ClientManager {
    //SINGLETON
    private static ClientManager instance;
    public static PubSubService pubsub;
    public static View view;
    private static GameController gameController;
    private static GameManagerController gameManagerController;
    private static LobbyController lobbyController;

    public ClientManager(boolean isCLI){
        pubsub = new PubSubService();
        if(isCLI){
            //cli mode
            view = new CLIgeneral();
            gameManagerController = new GameManagerController(view, new VirtualGameManager());
            pubsub.addSubscriber(TopicType.gameManagerState, gameManagerController);
            pubsub.addSubscriber(TopicType.networkMessageState, gameManagerController);
            //gameController = new GameController(new CLIgeneral(), new VirtualGame());
        }else{

        }
    }


    public static void createdControllers(String ID){
        //GameManagerController sees that a game has been created with an ID, the game controller gets instantiated
        gameController = new GameController(view, new VirtualGame(), ID);
        lobbyController = new LobbyController(view, new VirtualGameLobby());
    }

    //accessible from ClientMain (socket) and RMI
    public static void clientReceiveMessage(Message receivedMessageDecoded){
        if(receivedMessageDecoded instanceof NetworkMessage){
            //received a network message (like ping or request of username)
            pubsub.publishMessage(TopicType.networkMessageState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof GameManagerMessage){
            pubsub.publishMessage(TopicType.gameManagerState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof ErrorMessage){
            //ssss
        }else if(receivedMessageDecoded instanceof LobbyMessage){
            pubsub.publishMessage(TopicType.lobbyState, receivedMessageDecoded);
        }else if(receivedMessageDecoded instanceof MatchStateMessage){
            pubsub.publishMessage(TopicType.matchState, receivedMessageDecoded);
        }
    }
}
