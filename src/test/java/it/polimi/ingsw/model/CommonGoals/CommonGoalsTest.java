package it.polimi.ingsw.model.CommonGoals;

import it.polimi.ingsw.model.CommonGoals.Strategy.CommonGoalStrategy;
import it.polimi.ingsw.model.CommonGoals.Strategy.SixOfTwoGoalStrategy;
import it.polimi.ingsw.model.CommonGoals.Strategy.TriangularGoalStrategy;
import it.polimi.ingsw.model.modelSupport.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

class CommonGoalsTest {
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
        CommonGoalStrategy firstGoal = new TriangularGoalStrategy();
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
        CommonGoalStrategy secondGoal = new SixOfTwoGoalStrategy();
        prova.setSecondGoal(secondGoal);
        assertSame(prova.getSecondGoal(), secondGoal);
        // output prova
        out.println("Second goal: " + prova.getSecondGoal());
    }

    /**
     * Tests whether the addition of players with a certain goal completed is correct and if the assigned point are correct
     */
    @Test
    public void testCalculateAllPoints() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player2");
        Player player4 = new Player("player2");
        List<Player> players = Arrays.asList(player1, player2, player3, player4);
        ArrayList<Player> reachedGoalByEachPlayer = new ArrayList<>();
        List<Boolean> goalCompletedForEachPlayer = Arrays.asList(true,false,true,false);
        int [] expectedPoints = {8,6,4,2};

        /* Test points with 4 players */
        for(int i = 0; i < players.size(); i++){
            assertEquals(8-i*2, expectedPoints[i]);
        }
        /* Test points with 2 players */
        for(int i = 0; i < players.size()-2; i++){
            assertEquals(8-i*4, expectedPoints[i*2]);
        }

        /* Test for correct addition of a player after the goal is completed */
        for(int i = 0; i < goalCompletedForEachPlayer.size(); i++){
            if(goalCompletedForEachPlayer.get(i).equals(true)){
                reachedGoalByEachPlayer.add(players.get(i));
            }
        }
        for(int i = 0; i < goalCompletedForEachPlayer.size(); i++){
            if(goalCompletedForEachPlayer.get(i).equals(false)){
                assertFalse(reachedGoalByEachPlayer.contains(players.get(i)));
            }
            if(goalCompletedForEachPlayer.get(i).equals(true)){
                assertTrue(reachedGoalByEachPlayer.contains(players.get(i)));
            }
        }

    }
}