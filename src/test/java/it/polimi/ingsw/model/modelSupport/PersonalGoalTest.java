package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class PersonalGoalTest {
    @Test
    void TestcalculatePoints1() {
        // Create a sample 6x5 matrix of BoardCard objects
        Shelf shelf = new Shelf();
        BoardCard[][] board = new BoardCard[6][5];
        //provo uno ad uno tutti i possibili personalGoal e verifico se calcola tutti i punteggi

        PersonalGoal personalGoal1 = new PersonalGoal(0);
        board[5][2] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        board[3][1] = new BoardCard(colorType.YELLOW, ornamentType.A);
        board[2][3] = new BoardCard(colorType.WHITE, ornamentType.A);
        board[1][4] = new BoardCard(colorType.GREEN, ornamentType.A);
        board[0][0] = new BoardCard(colorType.PURPLE, ornamentType.A);
        board[0][2] = new BoardCard(colorType.BLUE, ornamentType.A);

        assertEquals(personalGoal1.calculatePoints(board), 12);
    }

        @Test
        void TestcalculatePoints2() {
            // Create a sample 6x5 matrix of BoardCard objects
            Shelf shelf = new Shelf();
            BoardCard[][] board = shelf.getShelfCards();

            PersonalGoal personalGoal2 = new PersonalGoal(1);

            board[5][4] = new BoardCard(colorType.BLUE, ornamentType.A);
            board[4][3] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
            board[3][4] = new BoardCard(colorType.WHITE, ornamentType.A);
            board[2][0] = new BoardCard(colorType.GREEN, ornamentType.A);
            board[2][2] = new BoardCard(colorType.YELLOW, ornamentType.A);
            board[1][1] = new BoardCard(colorType.PURPLE, ornamentType.A);

            assertEquals(personalGoal2.calculatePoints(board), 12);
        }

        @Test
        void TestcalculatePoints3() {
            // Create a sample 6x5 matrix of BoardCard objects
            Shelf shelf = new Shelf();
            BoardCard[][] board = shelf.getShelfCards();
            PersonalGoal personalGoal3 = new PersonalGoal(2);
            board[5][0] = new BoardCard(colorType.WHITE, ornamentType.A);
            board[3][1] = new BoardCard(colorType.GREEN, ornamentType.A);
            board[3][4] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
            board[2][2] = new BoardCard(colorType.PURPLE, ornamentType.A);
            board[1][0] = new BoardCard(colorType.BLUE, ornamentType.A);
            board[1][3] = new BoardCard(colorType.YELLOW, ornamentType.A);

            assertEquals(personalGoal3.calculatePoints(board), 12);

        }
            @Test
            void TestcalculatePoints4() {
                // Create a sample 6x5 matrix of BoardCard objects
                Shelf shelf = new Shelf();
                BoardCard[][] board = shelf.getShelfCards();
                PersonalGoal personalGoal4 = new PersonalGoal(3);
                board[4][1] = new BoardCard(colorType.WHITE, ornamentType.A);
                board[4][2] = new BoardCard(colorType.GREEN, ornamentType.A);
                board[3][3] = new BoardCard(colorType.PURPLE, ornamentType.A);
                board[2][0] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
                board[2][2] = new BoardCard(colorType.BLUE, ornamentType.A);
                board[0][4] = new BoardCard(colorType.YELLOW, ornamentType.A);
                assertEquals(personalGoal4.calculatePoints(board), 12);
            }

    @Test
    void TestcalculatePoints5() {
        // Create a sample 6x5 matrix of BoardCard objects
        Shelf shelf = new Shelf();
        BoardCard[][] board = shelf.getShelfCards();
        PersonalGoal personalGoal5 = new PersonalGoal(4);
        board[5][0] = new BoardCard(colorType.YELLOW, ornamentType.A);
        board[5][3] = new BoardCard(colorType.GREEN, ornamentType.A);
        board[4][4] = new BoardCard(colorType.PURPLE, ornamentType.A);
        board[3][1] = new BoardCard(colorType.BLUE, ornamentType.A);
        board[3][2] = new BoardCard(colorType.WHITE, ornamentType.A);
        board[1][1] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        assertEquals(personalGoal5.calculatePoints(board), 12);
    }

    @Test
    void TestcalculatePoints6() {
        // Create a sample 6x5 matrix of BoardCard objects
        Shelf shelf = new Shelf();
        BoardCard[][] board = shelf.getShelfCards();
        PersonalGoal personalGoal6 = new PersonalGoal(5);
        board[5][0] = new BoardCard(colorType.PURPLE, ornamentType.A);
        board[4][1] = new BoardCard(colorType.YELLOW, ornamentType.A);
        board[4][3] = new BoardCard(colorType.BLUE, ornamentType.A);
        board[2][3] = new BoardCard(colorType.WHITE, ornamentType.A);
        board[0][2] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        board[0][4] = new BoardCard(colorType.GREEN, ornamentType.A);
        assertEquals(personalGoal6.calculatePoints(board), 12);
    }

    @Test
    void TestcalculatePoints7() {
        // Create a sample 6x5 matrix of BoardCard objects
        Shelf shelf = new Shelf();
        BoardCard[][] board = shelf.getShelfCards();

        PersonalGoal personalGoal7 = new PersonalGoal(6);
        board[5][2] = new BoardCard(colorType.WHITE, ornamentType.A);
        board[4][4] = new BoardCard(colorType.YELLOW, ornamentType.A);
        board[3][0] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        board[2][1] = new BoardCard(colorType.PURPLE, ornamentType.A);
        board[1][3] = new BoardCard(colorType.BLUE, ornamentType.A);
        board[0][0] = new BoardCard(colorType.GREEN, ornamentType.A);
        assertEquals(personalGoal7.calculatePoints(board), 12);
    }
    @Test
    void TestcalculatePoints8() {
        // Create a sample 6x5 matrix of BoardCard objects
        Shelf shelf = new Shelf();
        BoardCard[][] board = shelf.getShelfCards();
        PersonalGoal personalGoal8 = new PersonalGoal(7);
        board[5][3] = new BoardCard(colorType.YELLOW, ornamentType.A);
        board[4][3] = new BoardCard(colorType.WHITE, ornamentType.A);
        board[3][0] = new BoardCard(colorType.PURPLE, ornamentType.A);
        board[2][2] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        board[1][1] = new BoardCard(colorType.GREEN, ornamentType.A);
        board[0][4] = new BoardCard(colorType.BLUE, ornamentType.A);
        assertEquals(personalGoal8.calculatePoints(board), 12);
    }
    @Test
    void TestcalculatePoints9() {
        // Create a sample 6x5 matrix of BoardCard objects
        Shelf shelf = new Shelf();
        BoardCard[][] board = shelf.getShelfCards();
        PersonalGoal personalGoal9 = new PersonalGoal(8);
        board[5][0] = new BoardCard(colorType.BLUE, ornamentType.A);
        board[4][1] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        board[4][4] = new BoardCard(colorType.PURPLE, ornamentType.A);
        board[3][4] = new BoardCard(colorType.WHITE, ornamentType.A);
        board[2][2] = new BoardCard(colorType.GREEN, ornamentType.A);
        board[0][2] = new BoardCard(colorType.YELLOW, ornamentType.A);
        assertEquals(personalGoal9.calculatePoints(board), 12);
    }
    @Test
    void TestcalculatePoints10() {
        // Create a sample 6x5 matrix of BoardCard objects
        Shelf shelf = new Shelf();
        BoardCard[][] board = shelf.getShelfCards();
        PersonalGoal personalGoal10 = new PersonalGoal(9);
        board[5][3] = new BoardCard(colorType.PURPLE, ornamentType.A);
        board[4][1] = new BoardCard(colorType.BLUE, ornamentType.A);
        board[3][3] = new BoardCard(colorType.GREEN, ornamentType.A);
        board[2][0] = new BoardCard(colorType.WHITE, ornamentType.A);
        board[1][1] = new BoardCard(colorType.YELLOW, ornamentType.A);
        board[0][4] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        assertEquals(personalGoal10.calculatePoints(board), 12);
    }

    @Test
    void TestcalculatePoints11() {
        // Create a sample 6x5 matrix of BoardCard objects
        Shelf shelf = new Shelf();
        BoardCard[][] board = shelf.getShelfCards();

        PersonalGoal personalGoal11 = new PersonalGoal(10);
        board[5][3] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        board[4][4] = new BoardCard(colorType.GREEN, ornamentType.A);
        board[3][2] = new BoardCard(colorType.BLUE, ornamentType.A);
        board[2][0] = new BoardCard(colorType.YELLOW, ornamentType.A);
        board[1][1] = new BoardCard(colorType.WHITE, ornamentType.A);
        board[0][2] = new BoardCard(colorType.PURPLE, ornamentType.A);
        assertEquals(personalGoal11.calculatePoints(board), 12);
    }

    @Test
    void TestcalculatePoints12() {
        // Create a sample 6x5 matrix of BoardCard objects
        Shelf shelf = new Shelf();
        BoardCard[][] board = shelf.getShelfCards();
        PersonalGoal personalGoal12 = new PersonalGoal(11);
        board[5][0] = new BoardCard(colorType.GREEN, ornamentType.A);
        board[4][4] = new BoardCard(colorType.YELLOW, ornamentType.A);
        board[3][3] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        board[2][2] = new BoardCard(colorType.BLUE, ornamentType.A);
        board[1][1] = new BoardCard(colorType.PURPLE, ornamentType.A);
        board[0][2] = new BoardCard(colorType.WHITE, ornamentType.A);
        assertEquals(personalGoal12.calculatePoints(board), 12);

    }


    //Now we try to get two personalGoals wrong
    @Test
    void WRONGTestcalculatePoints12() {
        // Create a sample 6x5 matrix of BoardCard objects
        Shelf shelf = new Shelf();
        BoardCard[][] board = shelf.getShelfCards();
        PersonalGoal personalGoal12 = new PersonalGoal(11);
        board[5][0] = new BoardCard(colorType.GREEN, ornamentType.A);
        board[4][4] = new BoardCard(colorType.GREEN, ornamentType.A);  //WRONG
        board[3][3] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        board[2][2] = new BoardCard(colorType.BLUE, ornamentType.A);
        board[1][1] = new BoardCard(colorType.WHITE, ornamentType.A); //WRONG
        board[0][2] = new BoardCard(colorType.WHITE, ornamentType.A);
        assertEquals(personalGoal12.calculatePoints(board), 6);  // 4 outta 6 give: 6pts

    }


}







