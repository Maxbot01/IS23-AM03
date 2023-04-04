package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FiveDiagonalGoalStrategyTest {
    private CommonGoalStrategy goal;

    @BeforeEach
    void setUp() {
        goal = new FiveDiagonalGoalStrategy();
    }

    @Test
    void testGoalCompleted() {
        /* TESTING FiveDiagonal SPECIFICI: NON ELIMINARE */
        /* Caso funzionante: */
        colorType[] firstLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.GREEN};
        colorType[] secondLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.BLUE};
        colorType[] thirdLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.LIGHT_BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.YELLOW,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
        /* Caso non funzionante:
        colorType[] firstLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT};
        colorType[] secondLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.WHITE};
        colorType[] thirdLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.WHITE,colorType.BLUE};
        colorType[] fourthLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.EMPTY_SPOT,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
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