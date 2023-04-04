package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import it.polimi.ingsw.model.modelSupport.exceptions.ColumnNotSelectable;
import it.polimi.ingsw.model.modelSupport.exceptions.ShelfFullException;

import javax.swing.plaf.synth.ColorType;
import java.util.ArrayList;

/**
 * A class representing a shelf that can hold BoardCard objects.
 */
public class Shelf {
    private BoardCard[][] shelfCards;
    private static final int ROWS_LEN = 6;
    private static final int COLUMNS_LEN = 5;
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
     *
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

    public int calculateAdiacentPoints(){
        int ris = 0;
        ArrayList<Pair<Integer,Integer>> savedCoord = new ArrayList<>();
        ArrayList<colorType> colors = new ArrayList<>();
        int [] numberOfCards = {2,2,2,2,2,2};

/*  Sezione per il calcolo delle sequenze massime per ogni colore. Il valore di ognuna è messo in numberOfCards. */
        for(int i = ROWS_LEN-1; i >= 0; i--){
            for(int j = COLUMNS_LEN-1; j >= 0; j--){
                Pair<Integer,Integer> tmp = new Pair<>(i,j);
                colorType chosenColor = shelfCards[i][j].getColor();
                /* System.out.println("Prima dell'if sull'empty nella prima sezione, in cui chosenColor vale " +
                        chosenColor + " la pair di tmp è fatta da: i = "+tmp.getFirst()+" j = "+tmp.getSecond()+"\n"); */
                if(!chosenColor.equals(colorType.EMPTY_SPOT) &&
                    !pairIsPresent(tmp,savedCoord)){
                    /* System.out.println("Ho superato empty e pairispresent in savedcoord\n"); */
                    int cont = findLenght(savedCoord, chosenColor, i, j);
                    /* System.out.println("cont vale: " + cont); */
                    if(!colors.contains(chosenColor)){
                        colors.add(chosenColor);
                        /* System.out.println("ho aggiunto un colore in colors alla posizione: " + colors.indexOf(chosenColor) + "\n"); */
                    }
                    if(cont > numberOfCards[colors.indexOf(chosenColor)]){
                        numberOfCards[colors.indexOf(chosenColor)] = cont;
                        /* System.out.println("cont era maggiore del valore precedente in numberofcards che ora vale: " + numberOfCards[colors.indexOf(chosenColor)] + "\n"); */
                    }
                    /* System.out.println("Fine del ciclo per questa posizione: i = " + i + " j = " + j + "\n"); */
                }
            }
        }
/*  Sezione per il calcolo dei punti. La sezione precedente potrebbe avere una funzione tutta sua. */
        /* System.out.println("Prima della sezione per il calcolo dei punti ris vale: " + ris); */
        for(int i = 0; i < numberOfCards.length; i++){
            if(numberOfCards[i] >= 3){
                /* System.out.println("il numero di carte di questo colore è maggiore uguale a 3\n"); */
                if(numberOfCards[i] >= 4){
                    if(numberOfCards[i] >= 5){
                        if(numberOfCards[i] >= 6){
                            ris += 8; /* punti aggiunti per 6+ cards uguali adiacenti */
                        }else{
                            ris += 5; /* punti aggiunti per 5 cards uguali adiacenti */
                        }
                    }else{
                        ris += 3; /* punti aggiunti per 4 cards uguali adiacenti */
                    }
                }else{
                    ris += 2; /* punti aggiunti per 3 cards uguali adiacenti */
                }
            }
        }
        /* System.out.println("Dopo la sezione per il calcolo dei punti ris vale: " + ris); */
        return ris;
    }
    private int findLenght(ArrayList<Pair<Integer,Integer>> savedCoord, colorType chosenColor, int x, int y){
        int cont = 1;
        Pair<Integer,Integer> tmp = new Pair<>(x,y);
        if(!pairIsPresent(tmp,savedCoord)) {
            savedCoord.add(tmp);
        }
        /* System.out.println("Sono in findLenght per il colore '" + chosenColor + "' in posizione: i = " + x + " j = " + y + "\n"); */
        if(y-1 >= 0 && shelfCards[x][y-1].getColor().equals(chosenColor)) { /* W Card */
            Pair<Integer,Integer> val = new Pair<>(x,y-1);
            if(!pairIsPresent(val,savedCoord)) {
                cont += findLenght(savedCoord, chosenColor, x, y - 1);
            }
        }
        if(x+1 < ROWS_LEN && shelfCards[x+1][y].getColor().equals(chosenColor)) {
            Pair<Integer, Integer> val = new Pair<>(x+1,y);
            if (!pairIsPresent(val, savedCoord)) {
                cont += findLenght(savedCoord, chosenColor, x + 1, y);
            }
        }
        if(y+1 < COLUMNS_LEN && shelfCards[x][y+1].getColor().equals(chosenColor)){
            Pair<Integer,Integer> val = new Pair<>(x,y+1);
            if(!pairIsPresent(val,savedCoord)) {
                cont += findLenght(savedCoord, chosenColor, x, y + 1);
            }
        }
        if(x-1 >= 0 && shelfCards[x-1][y].getColor().equals(chosenColor)){
            Pair<Integer,Integer> val = new Pair<>(x-1,y);
            if(!pairIsPresent(val,savedCoord)) {
                cont += findLenght(savedCoord, chosenColor, x - 1, y);
            }
        }
        /* System.out.println("Sono alla fine di findLenght e cont vale " + cont + "\n"); */
        return cont;
    }
    private boolean pairIsPresent(Pair<Integer,Integer> tmp, ArrayList<Pair<Integer,Integer>> savedTotalCoord){
        int present = 0;
        for(int i = 0; i < savedTotalCoord.size() && present == 0; i++){
            if(savedTotalCoord.get(i).getFirst().equals(tmp.getFirst()) && savedTotalCoord.get(i).getSecond().equals(tmp.getSecond())){
                present = 1;
            }
        }
        return present == 1;
    }
    /* public void initializeShelfForTesting(){ */
        /*
        Caso limite con tante combinazioni da 2 una sola da 3:
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] secondLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.WHITE,colorType.BLUE,colorType.WHITE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
        */
        /*
        Caso limite preso dal testing di SixOfTwo, con una combinazione da 8 cards uguali vicine e una da 4 dello stesso colore
        separata, deve calcolare solo i punti di quella maggiore
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        colorType[] secondLine = {colorType.WHITE,colorType.LIGHT_BLUE,colorType.WHITE,colorType.LIGHT_BLUE,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE};
        */
        /*
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
                BoardCard piece = new BoardCard(testColors.get(i)[j]);
                prova[i][j] = piece;
            }
        }
        this.shelfCards = prova;
        System.out.println("\nStampo 'prova:'");
        for(int i = 0; i < 6; i++){
            for(int j = 0;j < 5; j++){
                System.out.print(prova[i][j].getColor() + "  ");
            }
            System.out.print("\n");
        }
        System.out.println("\n");
    }*/
}

