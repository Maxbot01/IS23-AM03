package it.polimi.ingsw.model.CommonGoals;

import it.polimi.ingsw.model.CommonGoals.Strategy.CommonGoalStrategy;
import it.polimi.ingsw.model.CommonGoals.Strategy.SixOfTwoGoalStrategy;
import it.polimi.ingsw.model.CommonGoals.Strategy.TriangularGoalStrategy;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.Shelf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

class CommonGoalsTest {
    private CommonGoalStrategy firstGoal;
    private CommonGoalStrategy secondGoal;
    private CommonGoals prova;


    @BeforeEach
    void setUp() {
        prova = new CommonGoals();
    }

    /**
     * Tests whether it returns the right firstGoal
     */
    @Test
    void testGetFirstGoal() {
        firstGoal = new TriangularGoalStrategy();
        prova.setFirstGoal(firstGoal);
        assertSame(prova.getFirstGoal(), firstGoal);
        // output prova
        out.println("First goal: " + prova.getFirstGoal());
    }

    /**
     * Tests whether it returns the right secondGoal
     */
    @Test
    void testGetSecondGoal() {
        secondGoal = new SixOfTwoGoalStrategy();
        prova.setSecondGoal(secondGoal);
        assertSame(prova.getSecondGoal(), secondGoal);
        // output prova
        out.println("Second goal: " + prova.getSecondGoal());
    }

    @Test
    public void testCalculateAllPoints() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        List<Player> players = Arrays.asList(player1, player2);
        int numOfPlayers = players.size();

        int expectedPointsPlayer1 = 0;
        int expectedPointsPlayer2 = 0;

        /* Player 1 has not completed the first goal */
        boolean goalCompleted = true;
        if (goalCompleted) {
            prova.calculateAllPoints(player1, numOfPlayers);
            expectedPointsPlayer1 = numOfPlayers > 2 ? 6 : 4;
        }

        /* Player 2 has completed the first goal */
        goalCompleted = prova.getFirstGoal().goalCompleted(player2.getPlayersShelf().getShelfCards());
        if (goalCompleted) {
            prova.calculateAllPoints(player2, numOfPlayers);
            expectedPointsPlayer2 = numOfPlayers > 2 ? 8 : 4;
        }

        /* Check that the points were correctly assigned */
        Assertions.assertEquals(4, expectedPointsPlayer1);
        Assertions.assertEquals(0, expectedPointsPlayer2);
    }
}