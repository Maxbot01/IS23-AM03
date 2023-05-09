package it.polimi.ingsw.client;

import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.CommonGoals.Strategy.CommonGoalStrategy;
import it.polimi.ingsw.model.CommonGoals.Strategy.SixOfTwoGoalStrategy;
import it.polimi.ingsw.model.CommonGoals.Strategy.TriangularGoalStrategy;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.matchStateMessages.FinishedGameMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.InitStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedCardsMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedColumnsMessage;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.model.GameStateType.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;

class MessageSerializerTest {

    /*
    @Test
    public void testSerialize() {
        BoardCard[][] pieces = new BoardCard[5][5];
        Boolean[][] selectables = new Boolean[5][5];
        CommonGoals commonGoals = new CommonGoals();
        CommonGoalStrategy firstGoal = new TriangularGoalStrategy();
        commonGoals.setFirstGoal(firstGoal);
        CommonGoalStrategy secondGoal = new SixOfTwoGoalStrategy();
        commonGoals.setSecondGoal(secondGoal);
        HashMap<Player, PersonalGoal> personalGoals = new HashMap<>();
        ArrayList<Player> players = new ArrayList<>();
        Player chairedPlayer = new Player("John");
        ArrayList<Pair<Player, BoardCard[][]>> playersShelves = new ArrayList<>();

        // Set some sample values for the objects
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                pieces[i][j] = new BoardCard(colorType.EMPTY_SPOT, null);
                selectables[i][j] = false;
            }
        }
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));

        // Create a new InitStateMessage object with the sample objects
        InitStateMessage initStateMessage = new InitStateMessage(IN_PROGRESS, "gameID123", pieces, selectables, commonGoals, personalGoals, players, chairedPlayer, playersShelves);

        // Print out the message to verify that it was created correctly
        initStateMessage.printMessage();

        // Serializziamo il messaggio usando il MessageSerializer
        MessageSerializer serializer = new MessageSerializer();
        String serializedMessage = serializer.serialize(initStateMessage);

        // Verifichiamo che la stringa serializzata non sia vuota e che contenga il tipo di messaggio corretto
        assertNotNull(serializedMessage);
        //output serializedMessage
        System.out.println(serializedMessage);
    }*/
    /*
    @Test
    void SerializeAndDeserializeInitStateMessage() {
        BoardCard[][] pieces = new BoardCard[3][4];
        pieces[0][0] = new BoardCard(colorType.BLUE, ornamentType.A);
        pieces[0][1] = new BoardCard(colorType.YELLOW, ornamentType.A);
        pieces[0][2] = new BoardCard(colorType.GREEN, ornamentType.A);
        pieces[0][3] = new BoardCard(colorType.PURPLE, ornamentType.A);
        pieces[1][0] = new BoardCard(colorType.PURPLE, ornamentType.A);
        pieces[1][1] = new BoardCard(colorType.YELLOW, ornamentType.A);
        pieces[1][2] = new BoardCard(colorType.YELLOW, ornamentType.A);
        pieces[1][3] = new BoardCard(colorType.GREEN, ornamentType.A);
        pieces[2][0] = new BoardCard(colorType.GREEN, ornamentType.A);
        pieces[2][1] = new BoardCard(colorType.PURPLE, ornamentType.A);
        pieces[2][2] = new BoardCard(colorType.BLUE, ornamentType.A);
        pieces[2][3] = new BoardCard(colorType.YELLOW, ornamentType.A);

        Boolean[][] selectables = new Boolean[3][4];
        selectables[0][0] = true;
        selectables[0][1] = false;
        selectables[0][2] = true;
        selectables[0][3] = false;
        selectables[1][0] = false;
        selectables[1][1] = true;
        selectables[1][2] = false;
        selectables[1][3] = true;
        selectables[2][0] = true;
        selectables[2][1] = false;
        selectables[2][2] = true;
        selectables[2][3] = false;

        CommonGoals commonGoals = new CommonGoals();
        CommonGoalStrategy firstGoal = new TriangularGoalStrategy();
        commonGoals.setFirstGoal(firstGoal);
        CommonGoalStrategy secondGoal = new SixOfTwoGoalStrategy();
        commonGoals.setSecondGoal(secondGoal);


        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");

        String chairedPlayer = "player1";

        HashMap<String, PersonalGoal> personalGoals = new HashMap<>();
        personalGoals.put("player1", personalGoals.get(0));
        personalGoals.put("player2", personalGoals.get(0));
        personalGoals.put("player3", personalGoals.get(0));


        ArrayList<Pair<String, BoardCard[][]>> playersShelves = new ArrayList<>();
        playersShelves.add(new Pair<>("player1", new BoardCard[][]{{new BoardCard(colorType.PURPLE, ornamentType.A), new BoardCard(colorType.BLUE, ornamentType.A)}, {new BoardCard(colorType.YELLOW, ornamentType.A), new BoardCard(colorType.GREEN, ornamentType.A)}}));
        playersShelves.add(new Pair<>("player2", new BoardCard[][]{{new BoardCard(colorType.GREEN, ornamentType.A), new BoardCard(colorType.YELLOW, ornamentType.A)}, {new BoardCard(colorType.PURPLE, ornamentType.A), new BoardCard(colorType.BLUE, ornamentType.A)}}));
        playersShelves.add(new Pair<>("player3", new BoardCard[][]{{new BoardCard(colorType.YELLOW, ornamentType.A), new BoardCard(colorType.PURPLE, ornamentType.A)}, {new BoardCard(colorType.YELLOW, ornamentType.A), new BoardCard(colorType.GREEN, ornamentType.A)}}));

        InitStateMessage initStateMessage = new InitStateMessage(IN_PROGRESS, "match123", pieces, selectables, commonGoals, personalGoals, players, chairedPlayer, playersShelves);


        // Print out the message to verify that it was created correctly
        initStateMessage.printMessage();

        // Serializziamo il messaggio usando il MessageSerializer
        MessageSerializer serializer = new MessageSerializer();
        String serializedMessage = serializer.serialize(initStateMessage, "Alice", "1234");

        // Verifichiamo che la stringa serializzata non sia vuota e che contenga il tipo di messaggio corretto
        assertNotNull(serializedMessage);
        //output serializedMessage
        System.out.println(serializedMessage);
        ClientManager.userNickname = "Alice";

        Message deserializedMessage = new MessageSerializer().deserialize(serializedMessage);
        //output deserializedMessage
        System.out.println(deserializedMessage);

    }*/

    @Test
    void serializeAndDeserializeFinishedGameMessage() {
        ArrayList<Pair<String, Integer>> finalScoreBoard = new ArrayList<>();
        finalScoreBoard.add(new Pair<>("Alice", 10));
        finalScoreBoard.add(new Pair<>("Bob", 5));
        String winnerNickname = "Alice";
        String matchID = "gameID123";
        FinishedGameMessage finishedGameMessage = new FinishedGameMessage(GameStateType.FINISHED, matchID, finalScoreBoard, winnerNickname);
        // Print out the message to verify that it was created correctly
        finishedGameMessage.printMessage();
        // Serialize the message
        MessageSerializer serializer = new MessageSerializer();
        String serializedMessage = serializer.serialize(finishedGameMessage,"Alice","gameID123");
        //output serializedMessage
        System.out.println(serializedMessage);
        ClientManager.userNickname = "Alice";


        // Deserialize the message
        Message deserializedMessage = serializer.deserialize(serializedMessage);
        //output deserializedMessage
        System.out.println(deserializedMessage);
        deserializedMessage.printMessage();
        // Verify that the deserialized message is of the correct type
        assertTrue(deserializedMessage instanceof FinishedGameMessage);
    }

    @Test
    void SerializeAndDeserializeSelectedCardsMessage() {
        BoardCard[][] pieces = new BoardCard[5][5];
        Boolean[][] selectables = new Boolean[5][5];
        ArrayList<BoardCard> selectedCards = new ArrayList<>();
        Player currentPlayer = new Player("Alice");
        GameStateType gameState = GameStateType.IN_PROGRESS;
        String matchID = "matchID123";

        // Set some sample values for the objects
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                pieces[i][j] = new BoardCard(colorType.PURPLE, ornamentType.A);
                selectables[i][j] = false;
            }
        }
        selectedCards.add(new BoardCard(colorType.BLUE, ornamentType.A));
        selectedCards.add(new BoardCard(colorType.PURPLE, ornamentType.A));

        // Create a new SelectedCardsMessage object with the sample objects
        SelectedCardsMessage selectedCardsMessage = new SelectedCardsMessage(gameState, matchID, selectedCards, selectables, pieces, currentPlayer);

        // Print out the message to verify that it was created correctly
        selectedCardsMessage.printMessage();

        // Serialize the message using the MessageSerializer
        MessageSerializer serializer = new MessageSerializer();
        String serializedMessage = serializer.serialize(selectedCardsMessage,"Alice","gameID123");

        // Verify that the serialized string is not empty and contains the correct message type
        assertNotNull(serializedMessage);
        //output serializedMessage
        System.out.println(serializedMessage);
        ClientManager.userNickname = "Alice";

        Message deserializedMessage = new MessageSerializer().deserialize(serializedMessage);
        //output deserializedMessage
        System.out.println(deserializedMessage);
        //print deserializedMessage
        deserializedMessage.printMessage();

    }
    @Test
    void SerializeAndDeserializeSelectedColumnsMessage() {
        BoardCard[][] pieces = new BoardCard[5][5];
        Boolean[][] selectables = new Boolean[5][5];
        Pair<String, Integer> updatedPoints = new Pair<>("Alice", 5);
        Pair<String, BoardCard[][]> updatedPlayerShelf = new Pair<>("Player2", new BoardCard[][]{
                {new BoardCard(colorType.BLUE, ornamentType.A), new BoardCard(colorType.BLUE, ornamentType.A), new BoardCard(colorType.BLUE, ornamentType.A)},
                {new BoardCard(colorType.PURPLE, ornamentType.A), new BoardCard(colorType.PURPLE, ornamentType.A), new BoardCard(colorType.PURPLE, ornamentType.A)},
                {new BoardCard(colorType.YELLOW, ornamentType.A), new BoardCard(colorType.YELLOW, ornamentType.A), new BoardCard(colorType.YELLOW, ornamentType.A)}
        });
        String newPlayer = "Player3";
        GameStateType gameState = GameStateType.IN_PROGRESS;
        String matchID = "matchID123";

        // Set some sample values for the objects
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                pieces[i][j] = new BoardCard(colorType.EMPTY_SPOT, ornamentType.A);
                selectables[i][j] = false;
            }
        }

        // Set some selectables to true
        selectables[0][0] = true;
        selectables[1][1] = true;

        // Create a new SelectedColumnsMessage object with the sample objects
        SelectedColumnsMessage selectedColumnsMessage = new SelectedColumnsMessage(gameState, matchID, updatedPoints, newPlayer, updatedPlayerShelf, pieces, selectables);

        // Print out the message to verify that it was created correctly
        selectedColumnsMessage.printMessage();


        // Serialize the message using the MessageSerializer
        MessageSerializer serializer = new MessageSerializer();
        String serializedMessage = serializer.serialize(selectedColumnsMessage,"Alice","gameID123");

        // Verify that the serialized string is not empty and contains the correct message type
        assertNotNull(serializedMessage);
        //output serializedMessage
        System.out.println(serializedMessage);
        ClientManager.userNickname = "Alice";

        // Deserialize the message
        Message deserializedMessage = serializer.deserialize(serializedMessage);

        // Verify that the deserialized message is an instance of SelectedColumnsMessage and has the same values as the original message
        assertTrue(deserializedMessage instanceof SelectedColumnsMessage);
        SelectedColumnsMessage deserializedSelectedColumnsMessage = (SelectedColumnsMessage) deserializedMessage;
        // output deserializedMessage
        System.out.println(deserializedMessage);
        deserializedMessage.printMessage();

        //getMatchID
        String matchID1 = serializer.getMatchID(serializedMessage);
        System.out.println(matchID1);

        //getToPlayer
        ArrayList <String> toPlayer = serializer.deserializeToPlayersList(serializedMessage);
        //print arraylist
        System.out.println(toPlayer);


    }



}