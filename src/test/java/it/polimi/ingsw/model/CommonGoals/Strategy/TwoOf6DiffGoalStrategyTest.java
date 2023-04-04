package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TwoOf6DiffGoalStrategyTest {
    private CommonGoalStrategy goal;

    @BeforeEach
    void setUp() {
        goal = new TwoOf6DiffGoalStrategy();
    }

    @Test
    void testGoalCompleted() {
        /* TESTING TwoOf6Diff SPECIFICI: NON ELIMINARE */
        /* Caso funzionante: */
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        colorType[] secondLine = {colorType.LIGHT_BLUE,colorType.LIGHT_BLUE,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        colorType[] thirdLine = {colorType.WHITE,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.WHITE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fifthLine = {colorType.PURPLE,colorType.PURPLE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] sixthLine = {colorType.YELLOW,colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};

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