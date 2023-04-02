package it.polimi.ingsw.model.modelSupport;


import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.exceptions.ColumnNotSelectable;
import it.polimi.ingsw.model.modelSupport.exceptions.ShelfFullException;

import java.util.ArrayList;


/**
 * A class representing a shelf that can hold BoardCard objects.
 */
public class Shelf {

    private BoardCard[][] shelfCards;
    private static final int ROWS_LEN = 6;
    private static final int COLUMNS_LEN = 5;

    private final static BoardCard EMPTY_SPOT_CARD = new BoardCard(colorType.EMPTY_SPOT);

    /**
     * Constructs a new Shelf object with a 2D array of BoardCard objects. Initially all EMPTY_SPOT card types
     */
    public Shelf() {
        this.shelfCards = new BoardCard[ROWS_LEN][COLUMNS_LEN];
        for(int i = 0; i < ROWS_LEN; i++){
            for(int j= 0; j < COLUMNS_LEN; j++){
                this.shelfCards[i][j] = EMPTY_SPOT_CARD;
            }
        }
    }

    /**
     * Adds a list of BoardCard objects to a column in the shelf.
     * @param selCards the list of cards to add to the column
     * @param colIndex the index of the column to add the cards to
     * @throws ColumnNotSelectable if the column index is invalid, the list of cards is too long, or the column is already full
     * @throws ShelfFullException if the shelf is already full
     */
    public void insertInColumn(ArrayList<BoardCard> selCards, Integer colIndex) throws ColumnNotSelectable, ShelfFullException {
        // check if the column index is valid
        if (colIndex < 0 || colIndex >= COLUMNS_LEN) {
            throw new ColumnNotSelectable("Invalid column index");
        }
        // check if the list of cards to add is too long
        if (selCards.size() > 3) {
            throw new ColumnNotSelectable("Cannot select more than 3 cards");
        }
        // check if the column is already full
        if (columnIsFull(colIndex)) {
            throw new ColumnNotSelectable("Selected column is already full");
        }
        // find the first empty row in the column
        int row = 0;
        while (row < ROWS_LEN && shelfCards[COLUMNS_LEN-row][colIndex].getColor() != colorType.EMPTY_SPOT) {
            row++;
        }
        // check if there is enough space in the column for the selected cards
        if (row + selCards.size() > ROWS_LEN) {
            throw new ColumnNotSelectable("Cannot add cards to column: not enough space");
        }
        for (BoardCard selCard : selCards) {
            shelfCards[5-row][colIndex] = selCard;
            row++;
        }

        // check if the shelf is already full
        if (shelfIsFull()) {
            throw new ShelfFullException("Shelf is already full");
        }
    }

    /**
     * Checks whether the shelf is full.
     * @return true if the shelf is full, false otherwise
     */
    private boolean shelfIsFull() {
        for (int i = 0; i < ROWS_LEN; i++) {
            for (int j = 0; j < COLUMNS_LEN; j++) {
                if (shelfCards[i][j].getColor() == colorType.EMPTY_SPOT) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks whether a given column in the shelf is full.
     * @param colIndex the index of the column to check
     * @return true if the column is full, false otherwise
     */
    private boolean columnIsFull(int colIndex) {
        for (int i = 0; i < ROWS_LEN; i++) {
            if (shelfCards[i][colIndex].getColor() == colorType.EMPTY_SPOT){
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the current state of the shelf as a 2D array of BoardCard objects.
     * @return a 2D array of BoardCard objects representing the current state of the shelf
     */
    public BoardCard[][] getShelfCards() {
        return shelfCards;
    }

    /**
     * Returns the BoardCard object at the specified position in the shelf.
     * @param row the row index of the position
     * @param column the column index of the position
     * @return the BoardCard object at the specified position
     */
    public BoardCard getCardAtPosition(int row, int column) {
        return shelfCards[row][column];
    }

}

