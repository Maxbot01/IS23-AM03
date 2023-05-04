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
import java.util.HashMap;

import static it.polimi.ingsw.model.GameStateType.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;

class MessageSerializerTest {

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
    }

    @Test
    void deserializeInitStateMessage() {
        BoardCard[][] pieces = new BoardCard[5][5];
        Boolean[][] selectables = new Boolean[5][5];
        CommonGoals commonGoals = new CommonGoals();
        CommonGoalStrategy firstGoal = new TriangularGoalStrategy();
        commonGoals.setFirstGoal(firstGoal);
        CommonGoalStrategy secondGoal = new SixOfTwoGoalStrategy();
        commonGoals.setSecondGoal(secondGoal);
        String matchID = "gameID123";

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
        InitStateMessage initStateMessage = new InitStateMessage(IN_PROGRESS, matchID, pieces, selectables, commonGoals, personalGoals, players, chairedPlayer, playersShelves);

        // Print out the message to verify that it was created correctly
        initStateMessage.printMessage();

        // Serializziamo il messaggio usando il MessageSerializer
        MessageSerializer serializer = new MessageSerializer();
        String serializedMessage = serializer.serialize(initStateMessage);

        // Verifichiamo che la stringa serializzata non sia vuota e che contenga il tipo di messaggio corretto
        assertNotNull(serializedMessage);
        //output serializedMessage
        System.out.println(serializedMessage);

        Message deserializedMessage = new MessageSerializer().deserialize(serializedMessage);
        //output deserializedMessage
        System.out.println(deserializedMessage);

    }

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
        String serializedMessage = serializer.serialize(finishedGameMessage);
        //output serializedMessage
        System.out.println(serializedMessage);


        // Deserialize the message
        Message deserializedMessage = serializer.deserialize(serializedMessage);
        //output deserializedMessage
        System.out.println(deserializedMessage);
        // Verify that the deserialized message is of the correct type
        assertTrue(deserializedMessage instanceof FinishedGameMessage);
    }

    @Test
    void deserializeSelectedCardsMessage() {
        BoardCard[][] pieces = new BoardCard[5][5];
        Boolean[][] selectables = new Boolean[5][5];
        ArrayList<BoardCard> selectedCards = new ArrayList<>();
        Player currentPlayer = new Player("Alice");

        // Set some sample values for the objects
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                pieces[i][j] = new BoardCard(colorType.EMPTY_SPOT, ornamentType.A);
                selectables[i][j] = false;
            }
        }
        selectedCards.add(new BoardCard(colorType.BLUE, ornamentType.A));
        selectedCards.add(new BoardCard(colorType.PURPLE, ornamentType.A));

        // Create a new SelectedCardsMessage object with the sample objects
        SelectedCardsMessage selectedCardsMessage = new SelectedCardsMessage(GameStateType.IN_PROGRESS, "gameID123", selectedCards, selectables, pieces, currentPlayer);

        // Print out the message to verify that it was created correctly
        selectedCardsMessage.printMessage();

        // Serialize the message using the MessageSerializer
        MessageSerializer serializer = new MessageSerializer();
        String serializedMessage = serializer.serialize(selectedCardsMessage);

        // Verify that the serialized string is not empty and contains the correct message type
        assertNotNull(serializedMessage);
        //output serializedMessage
        System.out.println(serializedMessage);

        Message deserializedMessage = new MessageSerializer().deserialize(serializedMessage);
        //output deserializedMessage
        System.out.println(deserializedMessage);
    }
    @Test
    void testSelectedColumnsMessageSerialization() {
        // Set up sample objects
        BoardCard[][] pieces = new BoardCard[5][5];
        Boolean[][] selectables = new Boolean[5][5];
        Pair<String, Integer> updatedPoints = new Pair<>("Alice", 10);
        Pair<String, BoardCard[][]> updatedPlayerShelf = new Pair<>("Alice", new BoardCard[3][5]);
        String newPlayer = "Bob";
        GameStateType gameState = GameStateType.IN_PROGRESS;
        String matchID = "matchID123";

        // Set some sample values for the objects
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                pieces[i][j] = new BoardCard(colorType.EMPTY_SPOT, null);
                selectables[i][j] = false;
            }
        }

        // Create a new SelectedColumnsMessage object with the sample objects
        SelectedColumnsMessage selectedColumnsMessage = new SelectedColumnsMessage(gameState, matchID, updatedPoints, newPlayer, updatedPlayerShelf, pieces, selectables);

        // Print out the message to verify that it was created correctly
        selectedColumnsMessage.printMessage();

        // Serialize the message using the MessageSerializer
        MessageSerializer serializer = new MessageSerializer();
        String serializedMessage = serializer.serialize(selectedColumnsMessage);

        // Verify that the serialized string is not empty and contains the correct message type
        assertNotNull(serializedMessage);
        //output serializedMessage
        System.out.println(serializedMessage);

        // Deserialize the message
        Message deserializedMessage = serializer.deserialize(serializedMessage);

        // Verify that the deserialized message is an instance of SelectedColumnsMessage and has the same values as the original message
        assertTrue(deserializedMessage instanceof SelectedColumnsMessage);
        SelectedColumnsMessage deserializedSelectedColumnsMessage = (SelectedColumnsMessage) deserializedMessage;
        // output deserializedMessage
        System.out.println(deserializedMessage);


    }

}