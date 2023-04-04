package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FiveXGoalStrategyTest {
    private CommonGoalStrategy goal;

    @BeforeEach
    void setUp() {
        goal = new FiveXGoalStrategy();
    }
    @Test
    void testGoalCompleted() {
        /* TESTING FiveX SPECIFICI: NON ELIMINARE */
        /* Caso funzionante: */
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] secondLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.WHITE,colorType.BLUE,colorType.WHITE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
        /* Caso non funzionante:
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] secondLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.WHITE,colorType.BLUE,colorType.WHITE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.YELLOW,colorType.WHITE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
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
        assertTrue(goal.goalCompleted(prova));
    }
}