package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import it.polimi.ingsw.model.modelSupport.exceptions.ColumnNotSelectable;
import it.polimi.ingsw.model.modelSupport.exceptions.ShelfFullException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a shelf that can hold BoardCard objects.
 */
public class Shelf implements Serializable {
    private BoardCard[][] shelfCards;
    private static final int ROWS_LEN = 6;
    private static final int COLUMNS_LEN = 5;
    private static int[] groupCounts3 = new int[6];
    private static int[] groupCounts4 = new int[6];
    private static int[] groupCounts5 = new int[6];
    private static int[] groupCounts6 = new int[6];
    private final static BoardCard EMPTY_SPOT_CARD = new BoardCard(colorType.EMPTY_SPOT, ornamentType.A);
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
     * Returns the maximum number of empty spots in any column.
     * @return an integer value representing the maximum number of empty spots in any column
     */
    public int getEmptyColumn(){
        int max = 0;
        for(int i = 0; i < COLUMNS_LEN; i++){
            if(emptySpotsInColumn(i) > max){
                max = emptySpotsInColumn(i);
            }
        }
        return max;
    }
    /**
     * Returns the number of empty spots in a specified column.
     *
     * @param i the index of the column to count the number of empty spots in
     * @return an integer value representing the number of empty spots in the specified column
     */
    private int emptySpotsInColumn(int i) {
        int emptySpots = 0;
        for (int j = 0; j < ROWS_LEN; j++) {
            if (shelfCards[j][i].getColor() == colorType.EMPTY_SPOT) {
                emptySpots++;
            }
        }
        return emptySpots;
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
            throw new ColumnNotSelectable("The selected column is already full");
        }
        // find the first empty row in the column
        int row = 0;
        while (row < ROWS_LEN && shelfCards[COLUMNS_LEN-row][colIndex].getColor() != colorType.EMPTY_SPOT) {
            row++;
        }
        // check if there is enough space in the column for the selected cards
        if (row + selCards.size() > ROWS_LEN) {
            throw new ColumnNotSelectable("Cannot add cards to column, not enough space");
        }
        for (BoardCard selCard : selCards) {
            shelfCards[5-row][colIndex] = selCard;
            row++;
        }

        // check if the shelf is already full
        if (shelfIsFull()) {
            throw new ShelfFullException("Shelf is full");
        }
    }
    /**
     * Checks whether the shelf is full.
     * @return true if the shelf is full, false otherwise
     */
    public boolean shelfIsFull() {
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
    public boolean columnIsFull(int colIndex) {
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


    /**
     * Computes the score performed via groups of similarly colored cards
     * @return int, adjacent points
     */
    public int calculateAdiacentPoints(){
        BoardCard[][] board = new BoardCard[6][5]; // a new shelf is created, allowing the algorithm to modify it
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                colorType selColor = shelfCards[i][j].getColor();
                ornamentType selOrnament = shelfCards[i][j].getOrnament();
                BoardCard tmp = new BoardCard(selColor, selOrnament);
                board[i][j] = tmp; //shelf is populated
            }
        }
        generateVectors(board); //4 vectors of Adjacent counters are created, respectively for groups of 6, 5, 4, 3 cards

        //score is computed by summing the contents of every Counters-Array and moltiplying the result fot the weight of the group
        int sum6 = calculateSum(groupCounts6, 8);
        int sum5 = calculateSum(groupCounts5, 5);
        int sum4 = calculateSum(groupCounts4, 3);
        int sum3 = calculateSum(groupCounts3, 2);
        int sum = sum3 + sum4 +sum5 + sum6;
        return sum;
    }

    //Computes the internal sum of an array and moltiplies it for the array-weight
    private int calculateSum(int[] counts, int weight){
        int sum = 0;
        for(int i=0; i<counts.length; i++) {
            sum = sum + counts[i] * weight;
        }
        return sum;
    }

    // Generates the counters-Array for every group cardinality.
    // Every cell corresponds to a color and his content indicates the number of groups of that specific color.
    private void generateVectors(BoardCard[][] board){
        int i = 0;
        int dim = 6;
        // counters are set for every color
        // every block of code computes one array
        // (possible implementation with a for cycle)
        for (colorType color: colorType.values()) {
            groupCounts6[i] = countAdjacentCardGroups(board, color, dim);
            setEmptySpotForCardGroups(board,color, dim);
            i++;
            if(i>5){break;}
        }
        dim--;
        i = 0;
        for (colorType color: colorType.values()) { // we start from the largest groups, then we reduce them
            groupCounts5[i] = countAdjacentCardGroups(board, color, dim); // TRICK: after finding a group, it gets removed (possible implementation with back-propagation)
            setEmptySpotForCardGroups(board, color, dim); //only 6 color are needed (TOMBSTONE nor EMPTY_SPOT)
            i++;
            if (i > 5) {break;}
        }
        dim--;
        i = 0;
        for (colorType color: colorType.values()) {
            groupCounts4[i] = countAdjacentCardGroups(board, color, dim);
            setEmptySpotForCardGroups(board, color, dim);
            i++;
            if (i > 5) {break;}
        }
        dim--;
        i = 0;
        for (colorType color: colorType.values()) {
            groupCounts3[i] = countAdjacentCardGroups(board, color, dim);
            setEmptySpotForCardGroups(board, color, dim);
            i++;
            if (i > 5) {break;}
        }

    }
    /**
     * Computes for every color the amount of groups having dim cardinality
     * @param board
     * @param color
     * @param dim
     * @return int
     */
    public static int countAdjacentCardGroups(BoardCard[][] board, colorType color, int dim) {
        int count = 0;
        boolean[][] visited = new boolean[ROWS_LEN][COLUMNS_LEN];

        for (int row = 0; row < ROWS_LEN; row++) {
            for (int col = 0; col < COLUMNS_LEN; col++) {
                if (!visited[row][col] && board[row][col].getColor() == color) {
                    int groupCount = countCardGroupSize(board, row, col, color, visited);
                    if (groupCount >= dim) {
                        count++;
                    }
                }
            }
        }

        return count;
    }
    /**
     * Group searcher, recursively moves from one BoardCard to the next trying to increment the size of the group
     * @param board
     * @param row
     * @param col
     * @param color
     * @param visited
     * @return int, size of the found group
     */

    private static int countCardGroupSize(BoardCard[][] board, int row, int col, colorType color,
                                          boolean[][] visited) {
        if (row < 0 || row >= ROWS_LEN || col < 0 || col >= COLUMNS_LEN || visited[row][col]
                || board[row][col].getColor() != color) {
            return 0; // if the color differs, 0 is returned
        }

        //else, the recursion continues

        visited[row][col] = true;
        int size = 1; // BASE CASE!

        size += countCardGroupSize(board, row - 1, col, color, visited);
        size += countCardGroupSize(board, row + 1, col, color, visited);
        size += countCardGroupSize(board, row, col - 1, color, visited);
        size += countCardGroupSize(board, row, col + 1, color, visited);

        return size;
    }
     // Group eraser, card of the specific color are searched again and groups larger than dim are deleted.
     // This prevents to double count subgroups.

    private static void setEmptySpotForCardGroups(BoardCard[][] board, colorType color, int dim) {
        boolean[][] visited = new boolean[ROWS_LEN][COLUMNS_LEN];

        for (int row = 0; row < ROWS_LEN; row++) {
            for (int col = 0; col < COLUMNS_LEN; col++) {
                if (!visited[row][col] && board[row][col].getColor() == color) {
                    int groupCount = countCardGroupSize(board, row, col, color, visited);
                    if (groupCount >= dim) {
                        setEmptySpotForCardGroup(board, row, col, color); //whenever the cardinality is sufficient, the deletion starts
                    }
                }
            }
        }
    }
    //Adjacent similarly colored card are recursively set as empty
    private static void setEmptySpotForCardGroup(BoardCard[][] board, int row, int col, colorType color) {
        if (row < 0 || row >= ROWS_LEN || col < 0 || col >= COLUMNS_LEN || board[row][col].getColor() != color) {
            return; // if color differs, no need to erase further
        }
        board[row][col].setColor(colorType.EMPTY_SPOT);
        setEmptySpotForCardGroup(board, row - 1, col, color);
        setEmptySpotForCardGroup(board, row + 1, col, color);
        setEmptySpotForCardGroup(board, row, col - 1, color);
        setEmptySpotForCardGroup(board, row, col + 1, color);
    }

}

