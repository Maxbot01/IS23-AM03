package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.modelSupport.*;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.exceptions.ColumnNotSelectable;
import it.polimi.ingsw.model.modelSupport.exceptions.ShelfFullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ShelfTest {

    private Shelf shelf;

    @BeforeEach
    void setUp() {
        shelf = new Shelf();
    }

    /**
     * Tests the Shelf constructor.
     */
    @Test
    void testShelfConstructor() {
        BoardCard[][] shelfCards = shelf.getShelfCards();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(shelfCards[i][j].getColor(), colorType.EMPTY_SPOT);
            }
        }

        // output the shelf
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(shelf.getCardAtPosition(i, j).getColor() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Tests GetShelfCards method of the Shelf class.
     */
    @Test
    void testGetShelfCards() {
        BoardCard[][] shelfCards = shelf.getShelfCards();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(shelfCards[i][j].getColor(), colorType.EMPTY_SPOT);
            }
        }

        // output the shelf
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(shelf.getCardAtPosition(i, j).getColor() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Tests GetCardAtPosition method of the Shelf class.
     */
    @Test
    void testGetCardAtPosition() {
        BoardCard[][] shelfCards = shelf.getShelfCards();
        shelfCards[3][0] = new BoardCard(colorType.BLUE);
                assertEquals(shelfCards[3][0].getColor(), colorType.BLUE);

        // output the shelf
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(shelf.getCardAtPosition(i, j).getColor() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Tests the insertion of a list of BoardCard objects into a column of the Shelf object.
     * This test method verifies that when a list of BoardCard objects is inserted into a column of a Shelf object using the
     * insertInColumn() method, the cards are inserted in the correct order and position on the shelf.
     */
    @Test
    void testInsertInColumn() {
        ArrayList<BoardCard> cards = new ArrayList<>();
        cards.add(new BoardCard(colorType.GREEN));
        cards.add(new BoardCard(colorType.BLUE));
        cards.add(new BoardCard(colorType.YELLOW));
        assertDoesNotThrow(() -> shelf.insertInColumn(cards, 0));
        assertEquals(shelf.getCardAtPosition(5, 0).getColor(), colorType.GREEN);
        assertEquals(shelf.getCardAtPosition(4, 0).getColor(), colorType.BLUE);
        assertEquals(shelf.getCardAtPosition(3, 0).getColor(), colorType.YELLOW);

        // output the shelf
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(shelf.getCardAtPosition(i, j).getColor() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Tests that an exception is thrown when attempting to insert cards into a full column of the Shelf object.
     * This test method verifies that when a list of BoardCard objects is inserted into a column of a Shelf object using the
     * insertInColumn() method and that column is already full, a ColumnNotSelectable exception is thrown.
     */
    @Test
    public void testInsertInColumnThrowsExceptionWhenColumnIsFull() {
        // Fill column 0 with cards
        ArrayList<BoardCard> cards = new ArrayList<>();
        BoardCard[][] shelfCards = shelf.getShelfCards();
        shelfCards[5][0] = new BoardCard(colorType.BLUE);
        shelfCards[4][0] = new BoardCard(colorType.BLUE);
        shelfCards[3][0] = new BoardCard(colorType.BLUE);
        shelfCards[2][0] = new BoardCard(colorType.BLUE);
        shelfCards[1][0] = new BoardCard(colorType.BLUE);
        shelfCards[0][0] = new BoardCard(colorType.BLUE);


        // Try to insert more cards into column 0
        ArrayList<BoardCard> extraCards = new ArrayList<>();
        extraCards.add(new BoardCard(colorType.PURPLE));
        extraCards.add(new BoardCard(colorType.GREEN));
        extraCards.add(new BoardCard(colorType.YELLOW));
        try {
            shelf.insertInColumn(extraCards, 0);
            fail("Expected ColumnNotSelectable exception was not thrown");
        } catch (ColumnNotSelectable e) {
            assertEquals("Selected column is already full", e.getMessage());
        } catch (ShelfFullException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        // output the shelf
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(shelf.getCardAtPosition(i, j).getColor() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Tests that an exception is thrown when attempting to insert a list of BoardCard objects that exceeds the maximum
     * allowed length into a column of the Shelf object.
     * This test method verifies that when a list of BoardCard objects is inserted into a column of a Shelf object using the
     * insertInColumn() method and that list contains more than three BoardCard objects, a ColumnNotSelectable exception is
     * thrown.
     */
        @Test
    public void testInsertInColumnThrowsExceptionWhenListIsTooLong() {
        ArrayList<BoardCard> cards = new ArrayList<>();
        cards.add(new BoardCard(colorType.BLUE));
        cards.add(new BoardCard(colorType.YELLOW));
        cards.add(new BoardCard(colorType.GREEN));
        cards.add(new BoardCard(colorType.PURPLE)); // too many cards

        try {
            shelf.insertInColumn(cards, 0);
            fail("Expected ColumnNotSelectable exception was not thrown");
        } catch (ColumnNotSelectable e) {
            assertEquals("Cannot select more than 3 cards", e.getMessage());
        } catch (ShelfFullException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }

        // output the shelf
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(shelf.getCardAtPosition(i, j).getColor() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Tests that an exception is thrown when attempting to insert a list of BoardCard objects into a column of the Shelf
     * object that does not have enough space to accommodate the new cards.
     * This test method verifies that when a list of BoardCard objects is inserted into a column of a Shelf object using the
     * insertInColumn() method and that column already contains the maximum allowed number of cards, a ColumnNotSelectable
     * exception is thrown.
     */
    @Test
    public void testInsertInColumnThrowsExceptionWhenNotEnoughSpaceInColumn() {
        ArrayList<BoardCard> cards = new ArrayList<>();

        BoardCard[][] shelfCards = shelf.getShelfCards();
        shelfCards[5][0] = new BoardCard(colorType.BLUE);
        shelfCards[4][0] = new BoardCard(colorType.BLUE);
        shelfCards[3][0] = new BoardCard(colorType.BLUE);
        shelfCards[2][0] = new BoardCard(colorType.BLUE);
        shelfCards[1][0] = new BoardCard(colorType.BLUE);

        cards.add(new BoardCard(colorType.BLUE));
        cards.add(new BoardCard(colorType.YELLOW));
        cards.add(new BoardCard(colorType.GREEN));

        try {
            shelf.insertInColumn(cards, 0);
            fail("Expected ColumnNotSelectable exception was not thrown");
        } catch (ColumnNotSelectable e) {
            assertEquals("Cannot add cards to column: not enough space", e.getMessage());
        } catch (ShelfFullException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        // output the shelf
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(shelf.getCardAtPosition(i, j).getColor() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Tests that an exception is thrown when attempting to insert a list of BoardCard objects into a column of the Shelf
     * that does not have enough space to accommodate the new cards.
     */
    @Test
    public void testShelfIsFullReturnsTrueWhenShelfIsFull() {
        // Fill up the entire shelf with cards
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                BoardCard[][] shelfCards = shelf.getShelfCards();
                shelfCards[i][j] = new BoardCard(colorType.BLUE);
            }
        }
        // output the shelf
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(shelf.getCardAtPosition(i, j).getColor() + " ");
            }
            System.out.println();
        }
        assertTrue(shelf.shelfIsFull());
    }

    /**
     * Tests emptySpotsInColumn method returns the correct number of empty spots in a column.
     */

    @Test
    public void testEmptySpotsInColumnReturnsCorrectNumberOfEmptySpots() {
        // Fill up the entire shelf with cards
        BoardCard[][] shelfCards = shelf.getShelfCards();
        shelfCards[5][0] = new BoardCard(colorType.BLUE);
        shelfCards[4][0] = new BoardCard(colorType.BLUE);
        shelfCards[3][0] = new BoardCard(colorType.BLUE);
        shelfCards[2][0] = new BoardCard(colorType.BLUE);
        shelfCards[1][0] = new BoardCard(colorType.BLUE);
        shelfCards[0][0] = new BoardCard(colorType.BLUE);
        shelfCards[5][1] = new BoardCard(colorType.BLUE);
        shelfCards[4][1] = new BoardCard(colorType.BLUE);
        shelfCards[3][1] = new BoardCard(colorType.BLUE);
        shelfCards[2][1] = new BoardCard(colorType.BLUE);
        shelfCards[1][1] = new BoardCard(colorType.BLUE);
        shelfCards[5][2] = new BoardCard(colorType.BLUE);
        shelfCards[4][2] = new BoardCard(colorType.BLUE);
        shelfCards[3][2] = new BoardCard(colorType.BLUE);
        shelfCards[2][2] = new BoardCard(colorType.BLUE);
        shelfCards[1][2] = new BoardCard(colorType.BLUE);
        shelfCards[5][3] = new BoardCard(colorType.BLUE);
        shelfCards[4][3] = new BoardCard(colorType.BLUE);
        shelfCards[3][3] = new BoardCard(colorType.BLUE);
        shelfCards[2][3] = new BoardCard(colorType.BLUE);
        shelfCards[1][3] = new BoardCard(colorType.BLUE);
        shelfCards[5][4] = new BoardCard(colorType.BLUE);
        shelfCards[4][4] = new BoardCard(colorType.BLUE);
        shelfCards[3][4] = new BoardCard(colorType.BLUE);


        // output the shelf
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(shelf.getCardAtPosition(i, j).getColor() + " ");
            }
            System.out.println();
        }
        assertEquals(3, shelf.getEmptyColumn());
    }

    /**
     * Tests invalid column number throws exception.
     */
    @Test
    public void testInsertInColumnThrowsExceptionWhenColumnIsInvalid() {
        ArrayList<BoardCard> cards = new ArrayList<>();
        cards.add(new BoardCard(colorType.BLUE));
        cards.add(new BoardCard(colorType.YELLOW));
        cards.add(new BoardCard(colorType.GREEN));

        try {
            shelf.insertInColumn(cards, 5);
            fail("Expected ColumnNotSelectable exception was not thrown");
        } catch (ColumnNotSelectable e) {
            assertEquals("Invalid column index", e.getMessage());
        } catch (ShelfFullException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        // output the shelf
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(shelf.getCardAtPosition(i, j).getColor() + " ");
            }
            System.out.println();
        }
    }
}