package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FourOfFourGoalStrategyTest {
    private CommonGoalStrategy goal;

    @BeforeEach
    void setUp() {
        goal = new FourOfFourGoalStrategy();
    }

    @Test
    void testGoalCompleted() {
        /* TESTING FourOfFour SPECIFICI: NON ELIMINARE*/
        /* Caso funzionante senza 4-adiac (guarda il foglio): */
        colorType[] firstLine = {colorType.BLUE,colorType.PURPLE,colorType.GREEN,colorType.LIGHT_BLUE,colorType.YELLOW};
        colorType[] secondLine = {colorType.BLUE,colorType.BLUE,colorType.PURPLE,colorType.GREEN,colorType.LIGHT_BLUE};
        colorType[] thirdLine = {colorType.PURPLE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE,colorType.GREEN};
        colorType[] sixthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        /*
        Caso killer senza 4-adiac (guarda il foglio):
        colorType[] firstLine = {colorType.GREEN,colorType.PURPLE,colorType.GREEN,colorType.LIGHT_BLUE,colorType.YELLOW};
        colorType[] secondLine = {colorType.BLUE,colorType.BLUE,colorType.PURPLE,colorType.GREEN,colorType.LIGHT_BLUE};
        colorType[] thirdLine = {colorType.PURPLE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE,colorType.BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        */
        /*
        Caso funzionante con 4-adiac (guarda il foglio):
        colorType[] firstLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.LIGHT_BLUE,colorType.YELLOW};
        colorType[] secondLine = {colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.GREEN,colorType.LIGHT_BLUE};
        colorType[] thirdLine = {colorType.PURPLE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE,colorType.YELLOW};
        colorType[] sixthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        */
        /*
        Caso killer con 4-adiac (guard il foglio):
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.BLUE,colorType.LIGHT_BLUE,colorType.YELLOW};
        colorType[] secondLine = {colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.GREEN,colorType.LIGHT_BLUE};
        colorType[] thirdLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] fourthLine = {colorType.GREEN,colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] sixthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        */
        /*
        Caso funzionante senza 4-adiac con 3-adiac vicine (guarda il foglio):
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.BLUE,colorType.YELLOW};
        colorType[] secondLine = {colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] thirdLine = {colorType.LIGHT_BLUE,colorType.BLUE,colorType.GREEN,colorType.YELLOW,colorType.BLUE};
        colorType[] fourthLine = {colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.BLUE};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] sixthLine = {colorType.GREEN,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE};
        */
        /*
        Caso funionante con 16 carte che restituiscono 3 combinazioni più due di un altro colore (sono partito dal caso killer 4-adiac):
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] secondLine = {colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] thirdLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] fourthLine = {colorType.GREEN,colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] sixthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE};
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