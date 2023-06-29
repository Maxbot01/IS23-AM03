package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.client.ClientManager;
import it.polimi.ingsw.controller.controllerObservers.GameViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.CommonGoals.Strategy.*;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.ChatMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.*;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import static it.polimi.ingsw.controller.client.ClientMain.stub;
import static it.polimi.ingsw.controller.client.ClientManager.lobbyController;
import static it.polimi.ingsw.controller.client.ClientManager.virtualGameManager;

/**
 * This controller manages the game phase, getting messages from the server, updating the view, and responding to user inputs
 */
public class GameController extends Controller implements GameViewObserver, Subscriber {

    private InitStateMessage latestInit;
    private String gameID;
    private String currentPlayerSelecting;
    public volatile boolean playerReady;


    /**
     * Creates the controller that handles the game
     * @param view the view which methods the controller calls
     * @param gameID keeps the game id from the server calls
     */
    public GameController(View view,  String gameID) {
        super(view);
        this.gameID = gameID;
        this.playerReady = false;
        ClientManager.pubsub.addSubscriber(TopicType.matchState, this);
        ClientManager.pubsub.addSubscriber(TopicType.errorMessageState, this);
    }
    public String getGameID() {
        return gameID;
    }

    /**
     * The user is ready and the view calls this method. The relative GameManager endpoint method is called
     * @param gameID the id of the current game
     * @param nickname the nickname of the user making the request
     */
    @Override
    public void setReady(String gameID, String nickname) {
        virtualGameManager.userReady(nickname, gameID, stub);
        this.playerReady = true;
        if (!ClientManager.isSocketClient && stub != null) {
            Message message = null;
            try {
                String lobbyhost = stub.getGameLobbyHost(gameID);
                System.err.println("sto gettando da:" + lobbyhost);
                message = stub.ReceiveMessageRMI(lobbyhost);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                ClientManager.clientReceiveMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     *User has selected the cards. The relative GameManager endpoint method is called
     * @param selected array list of the user-ordered selected cards
     * @param user usenrname
     */
    @Override
    public void onSelectedCards(ArrayList<Pair<Integer, Integer>> selected, String user) {

            System.err.println("sending selected cards");
            virtualGameManager.selectedCards(selected, user, gameID, stub);

    }

    /**
     * User has selected the column, it is selectable (checked by the controller). The relative GameManager endpoint method is called
     * @param selCards selected cards in order
     * @param colIndex
     * @param user
     */
    @Override
    public void onSelectedColumn(ArrayList<BoardCard> selCards, Integer colIndex, String user) {
        //view has selected columns
        virtualGameManager.selectedColumn(selCards, colIndex, user, gameID, stub);
    }

    /**
     *
     */
    @Override
    public void onAcceptFinishedGame() {
        //view has accepted finished game
        //virtualGame.acceptFinishedGame();
    }

    /**
     * User can start choosing cards
     */
    @Override
    public void startCardsSelection(){
        ClientManager.view.chooseCards();
    }

    /**
     * Returns the common goals to the view
     * @param goalString
     * @return
     */
    private CommonGoalStrategy getCommonGoalStrategy(String goalString) {
        System.out.println("goalString: "+goalString);
        switch (goalString) {
            case "SixOfTwoGoalStrategy":
                return new SixOfTwoGoalStrategy();
            case "FiveDiagonalGoalStrategy":
                return new FiveDiagonalGoalStrategy();
            case "FourOfFourGoalStrategy":
                return new FourOfFourGoalStrategy();
            case "FourCornersGoalStrategy":
                return new FourCornersGoalStrategy();
            case "Double2x2GoalStrategy":
                return new Double2x2GoalStrategy();
            case "ThreeColumns3DiffGoalStrategy":
                return new ThreeColumns3DiffGoalStrategy();
            case "EightTilesGoalStrategy":
                return new EightTilesGoalStrategy();
            case "FourLines3DiffGoalStrategy":
                return new FourLines3DiffGoalStrategy();
            case "TwoOf6DiffGoalStrategy":
                return new TwoOf6DiffGoalStrategy();
            case "TwoOf5DiffGoalStrategy":
                return new TwoOf5DiffGoalStrategy();
            case "FiveXGoalStrategy":
                return new FiveXGoalStrategy();
            case "TriangularGoalStrategy":
                return new TriangularGoalStrategy();
            default:
                throw new IllegalArgumentException("Invalid goal strategy: " + goalString);
        }
    }

    /**
     * The GameController received the message from a topic on which it has a subscription, the message is coming from the server
     * @param message Received message
     * @return
     */
    @Override
    public boolean receiveSubscriberMessages(Message message){
        //a message has been received
        //should receive matchStateMessages only
        //after it receives it, updates the view accordingly
        if (message instanceof InitStateMessage) {
            InitStateMessage mess = (InitStateMessage) message;
            //if the message was for this client send ack
            HashMap<String, Integer> playersPoints = new HashMap<>(); // updatedMatchDetails needs an input of points, but all the points are set at 0
            for (String s : mess.players) {
                playersPoints.put(s, 0);
            }
            // Supponendo che tu abbia già un'istanza di InitStateMessage chiamata initStateMessage
            String firstGoalString = mess.firstGoal;
            String secondGoalString = mess.secondGoal;
            System.out.println(firstGoalString);
            System.out.println(secondGoalString);

            CommonGoals commonGoals = new CommonGoals();
            commonGoals.setFirstGoal(getCommonGoalStrategy(firstGoalString));
            commonGoals.setSecondGoal(getCommonGoalStrategy(secondGoalString));

            // Command "-ready" section:
            if(ClientManager.userNickname.equals(lobbyController.lastLobbyMessage.host)){
                this.playerReady = true;
            }else{
                /*while (true) {
                    if (this.playerReady){
                        System.out.println("Sono nel while, nell'if");
                        break;
                    }
                }*/
                while (!this.playerReady) {
                    Thread.onSpinWait();
                }
            }
            try {
                System.out.println(commonGoals.getFirstGoal() + " " + commonGoals.getSecondGoal());
                ClientManager.view.initializeGame(mess.players, commonGoals, mess.personalGoals, mess.pieces, mess.selecectables,
                        mess.playersShelves, playersPoints, mess.gameState);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ClientManager.view.printLivingRoom();
            ClientManager.view.printShelves();
            //ClientManager.view.showPlayingPlayer(mess.chairedPlayer); // prints the playing layer at the beginning of the turn
            /*if (mess.chairedPlayer.equals(ClientManager.userNickname)) {
                latestInit = mess;
                virtualGameManager.sendAck();
                ClientManager.view.chooseCards();
            } else {
                ClientManager.view.waitingCommands(); // it needs to be sent continuously until it's his turn, or maybe a notify to the cli that blocks a while cycle
            }*/
            ClientManager.view.updatePlayingPlayer(mess.chairedPlayer);
            ClientManager.view.gameCommands();
        } else if (message instanceof GameStateMessage) {//Useful in case of disconnection
            //TODO: Basically identical to initStateMessage, be careful
        } else if (message instanceof SelectedCardsMessage) {
            System.out.println("Sono nel selectedCardsMessage");
            SelectedCardsMessage mess = (SelectedCardsMessage) message;
            ClientManager.view.updateMatchAfterSelectedCards(mess.pieces, mess.selectables, mess.gameState);
            ClientManager.view.printLivingRoom();
            this.currentPlayerSelecting = mess.currentPlayer.getNickname();
            if (mess.currentPlayer.getNickname().equals(ClientManager.userNickname)) {
                ClientManager.view.chooseColumn();
            }
        } else if (message instanceof SelectedColumnsMessage) {
            System.out.println("Sono nel selectedColumnsMessage");
            SelectedColumnsMessage mess = (SelectedColumnsMessage) message;
            ClientManager.view.updateMatchAfterSelectedColumn(mess.pieces, mess.selectables, mess.gameState, mess.updatedPoints, mess.updatedPlayerShelf);
            ClientManager.view.printShelves();
            ClientManager.view.printLivingRoom();
            //ClientManager.view.showPlayingPlayer(mess.newPlayer);// print the chairedPlayer from the view (CLI)
            /*if (mess.newPlayer.equals(ClientManager.userNickname)) {
                ClientManager.view.chooseCards();
            } else {
                ClientManager.view.waitingCommands();
            }*/
            ClientManager.view.updatePlayingPlayer(mess.newPlayer);
            if(ClientManager.userNickname.equals(currentPlayerSelecting)){
                ClientManager.view.gameCommands();
            }
        } else if (message instanceof FinishedGameMessage mess) {
            ClientManager.view.printShelves();
            ClientManager.view.printScoreBoard(mess.finalScoreBoard, mess.winnerNickname, mess.gameState);
            this.playerReady = false;
            ClientManager.view.showErrorMessage("Exit the game with the command \"leave_game\"");
            while (!playerReady){
                Thread.onSpinWait();
            }
            ClientManager.view.endCommands();
        } else if (message instanceof ErrorMessage mess) {
            switch (mess.error.toString()) {
                case "selectedColumnsError":
                    //System.out.println("error case in GameController: "+mess.info);
                    ClientManager.view.showErrorMessage(mess.info);
                    ClientManager.view.chooseColumn();
                    break;
                case "shelfFullError":
                    //System.out.println("error case in GameController: "+mess.info);
                    ClientManager.view.showErrorMessage(mess.info);
                    break;
                case "acceptFinishedGameError":
                    //System.out.println("error case in GameController: "+mess.info);
                    ClientManager.view.showErrorMessage(mess.info);
                    //TODO: Manage error
                    break;
                case "selectedCardsMessageError":
                    //System.out.println("error case in GameController: "+mess.info);
                    ClientManager.view.showErrorMessage(mess.info);
                    ClientManager.view.chooseCards();
                    break;
            }
        }else if (message instanceof ChatMessage mess) {
            ClientManager.view.printChat(mess.messages);
        }
        return true;
    }
    //every controller HAS to be subscribed to a topic and HAS to observe the view

    /**
     * User send chat message. The relative GameManager endpoint method is called
     * @param message
     * @param toUser
     */
    @Override
    public void onSendChatMessage(String message,String toUser){
        virtualGameManager.receiveChatMessage(this.gameID,toUser,ClientManager.userNickname,message,false,true,stub);
    }

    /**
     * User received chat info
     * @param fullChat
     */
    @Override
    public void onGetChat(boolean fullChat){
        virtualGameManager.receiveChatMessage(this.gameID,null,ClientManager.userNickname,null,fullChat,true,stub);
    }

}
