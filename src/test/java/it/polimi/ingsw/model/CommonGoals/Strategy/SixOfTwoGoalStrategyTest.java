package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SixOfTwoGoalStrategyTest {
    private CommonGoalStrategy goal;

    @BeforeEach
    void setUp() {
        goal = new SixOfTwoGoalStrategy();
    }

    /**
     * Tests whether SixOfTwoGoalStrategy is correct
     */
    @Test
    void testGoalCompleted() {
        /* TESTING SixOfTwo SPECIFICI: NON ELIMINARE */
        /* Caso non funzionante: */
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        colorType[] secondLine = {colorType.WHITE,colorType.LIGHT_BLUE,colorType.WHITE,colorType.LIGHT_BLUE,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE};
        /* Caso funzionante:
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        colorType[] secondLine = {colorType.GREEN,colorType.LIGHT_BLUE,colorType.WHITE,colorType.LIGHT_BLUE,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE};
        */

        ArrayList<colorType[]> testColors = new ArrayList<>();
        testColors.add(firstLine);
        testColors.add(secondLine);
        testColors.add(thirdLine);
        testColors.add(fourthLine);
        testColors.add(fifthLine);
        testColors.add(sixthLine);
        BoardCard[][] prova = new BoardCard[6][5];
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 5; j++){
                BoardCard piece = new BoardCard(testColors.get(i)[j], ornamentType.A);
                prova[i][j] = piece;
            }
        }

        System.out.println("Stampo 'prova:'");
        for(int i = 0; i < 6; i++){
            for(int j = 0;j < 5; j++){
                System.out.print(prova[i][j].getColor() + "  ");
            }
            System.out.print("\n");
        }
        assertFalse(goal.goalCompleted(prova));
    }
}