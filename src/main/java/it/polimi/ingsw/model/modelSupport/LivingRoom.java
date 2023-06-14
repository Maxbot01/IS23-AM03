package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import it.polimi.ingsw.model.modelSupport.exceptions.NoMoreCardsException;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;


public class LivingRoom implements Serializable {

    private final static int DIM = 9;
    private final static int TOTCARDS = 132;
    private final static int COLORS = 6;
    private final static int NUMORNAMENTS= 3;
    private final static int ORNAMENTXCOLOR = 7;


    private final static BoardCard TOMBSTONE_CARD = new BoardCard(colorType.TOMBSTONE, ornamentType.A);
    private final static BoardCard EMPTY_SPOT_CARD = new BoardCard(colorType.EMPTY_SPOT,ornamentType.A);
    /**
     * Integer matrix that represents the "footprint" of the generic two-players game, {x,z} where x is the starting column and z the number of items in the row
     */
    private final static Integer[][] fp2 = {{0,0}, {3,2}, {3,3}, {2,6}, {1,7}, {1,6}, {3,3}, {4,2}, {0,0}};
    /**
     * Integer matrix that represents the "footprint" of the generic three-players game, {x,z} where x is the starting column and z the number of items in the row
     */
    private final static Integer[][] fp3 = {{3,1}, {3,2}, {2,5}, {2,7}, {1,7}, {0,7}, {2,5}, {4,2}, {0,0}};
    /**
     * Integer matrix that represents the "footprint" of generic the four-players game, {x,z} where x is the starting column and z the number of items in the row
     */
    private final static Integer[][] fp4 = {{3,2}, {3,3}, {2,5}, {1,7}, {0,9}, {0,8}, {2, 5}, {3,3}, {4,2}};

    private final static ArrayList<Integer[][]> posItms = new ArrayList<>();

    /**
     * BoardCard matrix that represents the board, null means no cards in match, a tombstone card is put when the position doesn't have a card
     */
    private final BoardCard [][] pieces;
    /**
     * BoardCard bag with shuffled cards
     */
    private final List<BoardCard> bag = new ArrayList<>();
    /**
     * Index of the last-selected card in the bag
     */
    private int indexOfStackCard;
    /**
     * Create the shuffled bag, fills the livingroom
     * @param numOfPLayers number of players, needed to know how to fill the board
     */
    public LivingRoom(int numOfPLayers) {
        //preparo due vettori da cui prendere le tipologie carte da aggiungere al bag
        colorType[] colors = {colorType.PURPLE, colorType.BLUE, colorType.LIGHT_BLUE, colorType.YELLOW, colorType.WHITE, colorType.GREEN};
        ornamentType[] ornaments = {ornamentType.A, ornamentType.B, ornamentType.C};

        //preparo il deck ordinato con le prime 126 carte
        for (int j = 0; j < COLORS; j++) {
            for (int i = 0; i < NUMORNAMENTS; i++) {
                int orn = 0;
                for (int k = 0; k < ORNAMENTXCOLOR; k++) {
                    BoardCard card = new BoardCard(colors[j], ornaments[orn]);
                    this.bag.add(card);
                }
            }
        }

        //aggiungo "manualmente" le 6 carte rimanenti (dando ornamento A)

        for(int i=0; i<COLORS; i++) {
            BoardCard card = new BoardCard(colors[i], ornaments[0]);
            this.bag.add(card);
        }

        Collections.shuffle(this.bag);

        //Fill the livingroom
        posItms.add(fp2);
        posItms.add(fp3);
        posItms.add(fp4);

        Integer[][] fp = posItms.get(numOfPLayers - 2);
        this.pieces = new BoardCard[DIM][DIM];
        //Insert the cards in the living room
        this.indexOfStackCard = 0;
        for(int i = 0; i < DIM; i++){
            for(int j = 0; j < DIM; j++){
                if(j >= fp[i][0] && j < fp[i][0] + fp[i][1]){
                    pieces[i][j] = this.bag.get(this.indexOfStackCard);
                    this.indexOfStackCard++;
                }else{
                    this.pieces[i][j] = EMPTY_SPOT_CARD;
                }
            }
        }

    }
    public BoardCard[][] getPieces() {
        return pieces;
    }
    /**
     * Refills the board only if needed and returns the updated (or not if not needed) livingroom
     */
    public BoardCard[][] refillBoard() throws NoMoreCardsException{
        //CHECK dei refill requirements
        int startRefill = 1;
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (isPresent(i,j) && adiacent(i, j)) {
                    startRefill = 0;
                }
            }
        }

        //insert where there's tombstones, in other places there are already usable cards
        if (startRefill == 1) {
            for(int i = 0; i < DIM; i++){
                for(int j = 0; j < DIM; j++){
                    if(this.pieces[i][j].getColor() == colorType.TOMBSTONE){
                        if(this.indexOfStackCard > bag.size()){
                            //no more cards are usable
                            throw new NoMoreCardsException();
                        }
                        this.pieces[i][j] = this.bag.get(this.indexOfStackCard);
                        this.indexOfStackCard++;
                    }
                }
            }
        }
        return this.pieces;
    }
    /**
     * Calculates all the selectable items in the board
     */
    public Boolean[][] calculateSelectable() {
        Boolean[][] selectable = new Boolean[DIM][DIM];
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    selectable[i][j] = isPresent(i,j) && freeCorner(i,j);
            }
       }
       return selectable;
    }
    public void updateBoard(ArrayList<Pair<Integer ,Integer>> selected ) throws UnselectableCardException {
        for (Pair<Integer, Integer> coordinates : selected) {
            int i = coordinates.getFirst();
            int j = coordinates.getSecond();
            if (!(isPresent(i, j) && freeCorner(i, j))) {//TODO: This check can be removed, be sure first
                throw new UnselectableCardException("No card available at position "+i+" "+j);
            }
        }
        for (Pair<Integer, Integer> coordinates : selected) {
            int i = coordinates.getFirst();
            int j = coordinates.getSecond();
            this.pieces[i][j] = TOMBSTONE_CARD;
        }
    }
    /**
     * Returns the Card at the given coordinates
     * @param coordinates
     * @return BoardCard
     * @throws UnselectableCardException
     */
    public BoardCard getBoardCardAt(Pair<Integer,Integer> coordinates) throws UnselectableCardException{
        int i = coordinates.getFirst();
        int j = coordinates.getSecond();
        if(!isPresent(i,j))
            throw new UnselectableCardException("No card present at position "+i+" "+j);
        else return this.pieces[i][j];
    }
    public boolean cardIsSelectable(int i, int j){
        return isPresent(i,j) && freeCorner(i,j);
    }
    /**
     * Checks if an item in the board has adjacent items
     * @param i i coordinate
     * @param j j coordinate
     * @return boolean
     */
    private boolean adiacent(int i, int j) {
        if (i == 0) {
            return isPresent(i, j - 1) || isPresent(i, j + 1) || isPresent(i + 1, j);

        } else if (i == (DIM - 1)) {
            return isPresent(i, j - 1) || isPresent(i, j + 1) || isPresent(i - 1, j);

        } else if (j == 0) {
            return isPresent(i - 1, j) || (isPresent(i + 1, j) || isPresent(i, j + 1));

        } else if (j == (DIM - 1)) {
            return isPresent(i, j - 1) || isPresent(i, j + 1) || isPresent(i - 1, j);

        } else {
                return isPresent(i, j - 1) || isPresent(i, j + 1) || isPresent(i - 1, j) || isPresent(i + 1, j);

        }

    }


    /**
     * function to check whether a card has a free corner
     * @param i
     * @param j
     * @return a boolen to indicate whether the card can be taken or not
     */
    private boolean freeCorner(int i, int j){
        if (i == 0 || i == DIM - 1 || j == 0 || j == DIM - 1) return true;
        else {
            return !isPresent(i, j - 1) || !isPresent(i, j + 1) || !isPresent(i - 1, j) || !isPresent(i + 1, j);
        }
    }

    /**
     * function to check whether there's an actual card in the given coordinates
     * @param i
     * @param j
     * @return boolean
     */
    private boolean isPresent(int i, int j){
        return (pieces[i][j].getColor() != colorType.TOMBSTONE) && (pieces[i][j].getColor() != colorType.EMPTY_SPOT);
    }

    private boolean isAngle(int i, int j){
        return ((i+j) % DIM - 1 == 0 && (i == 0 || i == DIM - 1));
    }

    private boolean inRow(Pair<Integer,Integer> coordA, Pair<Integer,Integer> coordB){
        int xA = coordA.getFirst();
        int yA = coordA.getSecond();
        int xB = coordB.getFirst();
        int yB = coordB.getSecond();

        if(xA == xB && (yA == yB+1 || yB == yA+1)){
            return true;
        }
        else return yA == yB && (xA == xB + 1 || xB == xA + 1);

    }
    private boolean inRow(Pair<Integer,Integer> coordA, Pair<Integer,Integer> coordB, Pair<Integer,Integer> coordC){
        int xA = coordA.getFirst();
        int yA = coordA.getSecond();
        int xB = coordB.getFirst();
        int yB = coordB.getSecond();
        int xC = coordC.getFirst();
        int yC = coordC.getSecond();
        if(!((xA==xB && xB==xC) || (yA == yB && yB == yC)))
            return false;
        else {
            return inRow(coordA,coordB) && inRow(coordB,coordC);
        }

    }
}




