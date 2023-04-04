package it.polimi.ingsw.model.CommonGoals;

import it.polimi.ingsw.model.CommonGoals.Strategy.CommonGoalStrategy;
import it.polimi.ingsw.model.CommonGoals.Strategy.SixOfTwoGoalStrategy;
import it.polimi.ingsw.model.CommonGoals.Strategy.TriangularGoalStrategy;
import it.polimi.ingsw.model.modelSupport.Shelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    /**
     * Tests whether it returns the right secondGoal
     */
    @Test
    void testGetSecondGoal() {
        secondGoal = new SixOfTwoGoalStrategy();
        prova.setSecondGoal(secondGoal);
        assertSame(prova.getSecondGoal(), secondGoal);
    }

    @Test
    void testCalculateAllPoints() {

    }
}