package it.polimi.ingsw.model.modelSupport;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.exceptions.NoMoreCardsException;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LivingRoom{

    private final static int DIM = 9;
    private final static int TOTCARDS = 132;
    private final static int COLORS = 6;
    private final static int NUMXCOLOR = 22;

    private final static BoardCard THOMBSTONE = new BoardCard(colorType.THOMBSTONE);

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
    private final static Integer[][] fp4 = {{3,2}, {3,3}, {1,8}, {0,9}, {0,8}, {0,7}, {2,6}, {3,5}, {4,1}};
    private final static ArrayList<Integer[][]> posItms = new ArrayList<>();

    /**
     * BoardCard matrix that represents the board, null means no cards in match, a thombstone card is put when the position doesn't have a card
     */
    private BoardCard [][] pieces;
    /**
     * BoardCard bag with shuffled cards
     */
    private final List<BoardCard> bag = new ArrayList<BoardCard>();
    /**
     * Index of the last-selected card in the bag
     */
    private int indexOfStackCard;


    /**
     * Create the shuffled bag, fills the livingroom
     * @param numOfPLayers number of players, needed to know how to fill the board
     */
    public LivingRoom(int numOfPLayers) {
        //preparo un Arraylist da cui prendere le carte di vari colori
        List<BoardCard> temp = new ArrayList<BoardCard>();
        colorType[] colors = {colorType.PURPLE, colorType.BLUE, colorType.LIGHT_BLUE, colorType.YELLOW, colorType.WHITE, colorType.GREEN};
        for (int j = 0; j < COLORS; j++) {
            for (int i = 0; i < NUMXCOLOR; i++) {
                BoardCard card = new BoardCard(colors[j]);
                temp.set(i, card);
            }
        }
        //pesco carte dall'ArrayList per creare il mazzo
        Random random = new Random();
        for (int i = 0; i < TOTCARDS; i++) {
            int range = temp.size();
            int chosen = random.nextInt(range);
            BoardCard piece = temp.get(chosen);
            bag.set(i, piece);
            temp.remove(chosen);
        }


        //Fill the livingroom
        posItms.add(fp2);
        posItms.add(fp3);
        posItms.add(fp4);

        /**
         * Integer matrix that represents the "footprint" of the constructed n-players game, {x,z} where x is the starting column and z the number of items in the row
         */
        Integer[][] fp = posItms.get(numOfPLayers - 2);

        //Insert the cards in the living room
        indexOfStackCard = 0;
        for(int i = 0; i < DIM; i++){
            for(int j = 0; j < DIM; j++){
                if(j >= fp[i][0] && j < fp[i][0] + fp[i][1]){
                    pieces[i][j] = bag.get(indexOfStackCard);
                    indexOfStackCard++;
                }else{
                    pieces[i][j] = null;
                }
            }
        }
    }


    public int getIndexOfStackCard(){
        return indexOfStackCard;
    }


    public BoardCard[][] getPieces() {
        return pieces;
    }


    /**
     * Refills the board only if needed and returns the updated (or not if not needed) livingroom
     * @param numOfPlayers players in the game
     */
    public BoardCard[][] refillBoard(int numOfPlayers) throws NoMoreCardsException{
        //CHECK dei refill requirements
        int startRefill = 1;
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (isPresent(i,j) && adiacent(i, j)) {
                    startRefill = 0;
                }
            }
        }

        //insert where there's thombstones, in other places there are already usable cards
        if (startRefill == 1) {
            for(int i = 0; i < DIM; i++){
                for(int j = 0; j < DIM; j++){
                    if(pieces[i][j] == THOMBSTONE){
                        if(indexOfStackCard > bag.size()){
                            //no more cards are usable
                            throw new NoMoreCardsException();
                        }
                        pieces[i][j] = bag.get(indexOfStackCard);
                        indexOfStackCard++;
                    }
                }
            }
        }
        return pieces;
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


    public BoardCard[][] updateBoard(ArrayList<Pair<Integer ,Integer>> selected ) throws UnselectableCardException {
        for (Pair<Integer, Integer> coordinates : selected) {
            int i = coordinates.getFirst();
            int j = coordinates.getSecond();
            if (!(isPresent(i, j) && freeCorner(i, j))) {
                throw new UnselectableCardException();
            }
        }
        for (Pair<Integer, Integer> coordinates : selected) {
            int i = coordinates.getFirst();
            int j = coordinates.getSecond();
            pieces[i][j] = THOMBSTONE;
        }
        return pieces;
    }

    public BoardCard getBoardCardAt(Pair<Integer,Integer> coordinates) throws UnselectableCardException{
        int i = coordinates.getFirst();
        int j = coordinates.getSecond();
        if(!isPresent(i,j))
            throw new UnselectableCardException();
        else return pieces[i][j];

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

        } else if (i == DIM - 1) {
            return isPresent(i, j - 1) || isPresent(i, j + 1) || isPresent(i - 1, j);

        } else if (j == 0) {
            return isPresent(i, j - 1) || (isPresent(i, j + 1) || isPresent(i + 1, j));

        } else if (j == DIM - 1) {
            return isPresent(i, j - 1) || isPresent(i, j + 1) || isPresent(i - 1, j);

        } else {
            return isPresent(i, j - 1) || isPresent(i, j + 1) || isPresent(i - 1, j) || isPresent(i + 1, j);

        }

    }


    //funzione che calcola se la tessera ha ALMENO un lato libero
    private boolean freeCorner(int i, int j){
        if (i == 0 || i == DIM - 1 || j == 0 || j == DIM - 1) return true;
        else {
            return !isPresent(i, j - 1) || !isPresent(i, j + 1) || !isPresent(i - 1, j) || !isPresent(i + 1, j);
        }
    }

    //funzione che calcola se una tessera è presente nella Livingroom  (altrimenti è null o THOMBSTONE)
    private boolean isPresent(int i, int j){
        return (pieces[i][j] != THOMBSTONE) && (pieces[i][j] != null);
    }

}




