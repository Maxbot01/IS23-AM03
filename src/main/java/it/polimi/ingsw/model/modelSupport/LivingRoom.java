package it.polimi.ingsw.model.modelSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LivingRoom{

    final static int DIM = 9;
    final static int TOTCARDS = 132;
    final static int COLORS = 6;
    final static int NUMXCOLOR = 22;

    private BoardCard [][] pieces;
    private List<BoardCard> stack = new ArrayList<BoardCard>();
    private int indexOfStackCard;



    LivingRoom(int numOfPLayers) {
        //preparo un Arraylist da cui prendere le carte di vari colori
        List<BoardCard> temp = new ArrayList<BoardCard>();
        colorType[] colors = colorType.values();
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
            stack.set(i, piece);
            temp.remove(chosen);
        }

        //Popolo la LivingRoom
        pieces = new BoardCard[DIM][DIM];
        int k = 0;
        for (int i = 0; i < DIM; i++) {
            //caso 4 giocatori
            if (numOfPLayers == 4) {
                if (i == 0) {
                    for (int j = 3; j < 5; j++) {
                        pieces[i][j] = stack.get(k);
                        k++;
                        pieces[DIM - i-1][DIM - j-1] = stack.get(k); //TRICK: la matrice ha una simmetria, posso riempire due posizioni alla volta
                        k++;
                    }
                }
                if (i == 1) {
                    for (int j = 3; j < 6; j++) {
                        pieces[i][j] = stack.get(k);
                        k++;
                        pieces[DIM - i-1][DIM - j-1] = stack.get(k);
                        k++;
                    }
                }
                if (i == 2) {
                    for (int j = 2; j < 7; j++) {
                        pieces[i][j] = stack.get(k);
                        k++;
                        pieces[DIM - i-1][DIM - j-1] = stack.get(k);
                        k++;
                    }
                }
                if (i == 3) {
                    for (int j = 1; j < 9; j++) {
                        pieces[i][j] = stack.get(k);
                        k++;
                        pieces[DIM - i-1][DIM - j-1] = stack.get(k);
                        k++;
                    }
                }
                if (i == 4) {
                    for (int j = 0; j < 9; j++) {
                        pieces[i][j] = stack.get(k);
                        k++;
                    }
                }
            }
            // fine caso 4 giocatori
            // inizio caso 3 giocatori
            else if (numOfPLayers == 3) {
                if (i == 0) {
                    int j = 3;
                    pieces[i][j] = stack.get(k);
                    k++;
                    pieces[DIM - i-1][DIM - j-1] = stack.get(k);
                    k++;
                }
            }
            if (i == 1) {
                for (int j = 3; j < 5; j++) {
                    pieces[i][j] = stack.get(k);
                    k++;
                    pieces[DIM - i-1][DIM - j-1] = stack.get(k);
                    k++;
                }
            }
            if (i == 2) {
                for (int j = 2; j < 7; j++) {
                    pieces[i][j] = stack.get(k);
                    k++;
                    pieces[DIM - i-1][DIM - j-1] = stack.get(k);
                    k++;
                }
            }
            if (i == 3) {
                for (int j = 2; j < 9; j++) {
                    pieces[i][j] = stack.get(k);
                    k++;
                    pieces[DIM - i-1][DIM - j-1] = stack.get(k);
                    k++;
                }
            }
            if (i == 4) {
                for (int j = 1; j < 8; j++) {
                    pieces[i][j] = stack.get(k);
                    k++;
                }
            }

            // fine caso 3 giocatori
            // inizio caso 2 giocatori

            else if (numOfPLayers == 2) {

                // riga i==0 vuota

                if (i == 1) {
                    for (int j = 3; j < 5; j++) {
                        pieces[i][j] = stack.get(k);
                        k++;
                        pieces[DIM - i-1][DIM - j-1] = stack.get(k);
                        k++;
                    }
                }
                if (i == 2) {
                    for (int j = 3; j < 6; j++) {
                        pieces[i][j] = stack.get(k);
                        k++;
                        pieces[DIM - i-1][DIM - j-1] = stack.get(k);
                        k++;
                    }
                }
                if (i == 3) {
                    for (int j = 2; j < 8; j++) {
                        pieces[i][j] = stack.get(k);
                        k++;
                        pieces[DIM - i-1][DIM - j-1] = stack.get(k);
                        k++;
                    }
                }
                if (i == 4) { //WTF?????????????????????????????????????????????????????????
                    for (int j = 1; j < 8; j++) {
                        pieces[i][j] = stack.get(k);
                        k++;
                    }
                }
            }
        }
        indexOfStackCard = k;


    }

        int getIndexOfStackCard(){
            return indexOfStackCard;
        }

        BoardCard[][] getPieces() {
            return pieces;
        }
        BoardCard[][] refillBoard(int numOfPlayers) {
            //CHECK dei refill requirements
            int startRefill = 1;
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    if (!(pieces[i][j] == (null)) && adiacent(i, j)) {//TODO: creare funzione adiacent
                        startRefill = 0;
                    }
                }
            }
            if (startRefill == 1) {
                int k = getIndexOfStackCard();
                for (int i = 0; i < DIM; i++) {
                    //caso 4 giocatori
                    if (numOfPlayers == 4) {
                        if (i == 0) {
                            for (int j = 3; j < 5; j++) {
                                if (pieces[i][j] == (null)) {
                                    pieces[i][j] = stack.get(k);
                                    k++;
                                }
                                if (pieces[DIM - i][DIM - j] == (null)) {
                                    pieces[DIM - i - 1][DIM - j - 1] = stack.get(k);
                                    k++;
                                }
                            }
                        }
                        if (i == 1) {
                            for (int j = 3; j < 6; j++) {
                                if (pieces[i][j] == (null)) {
                                    pieces[i][j] = stack.get(k);
                                    k++;
                                }
                                if (pieces[DIM - i][DIM - j] == (null)) {
                                    pieces[DIM - i - 1][DIM - j - 1] = stack.get(k);
                                    k++;
                                }
                            }
                        }
                        if (i == 2) {
                            for (int j = 2; j < 7; j++) {
                                if (pieces[i][j] == (null)) {
                                    pieces[i][j] = stack.get(k);
                                    k++;
                                }
                                if (pieces[DIM - i][DIM - j] == (null)) {
                                    pieces[DIM - i - 1][DIM - j - 1] = stack.get(k);
                                    k++;
                                }
                            }
                        }
                        if (i == 3) {
                            for (int j = 1; j < 9; j++) {
                                if (pieces[i][j] == (null)) {
                                    pieces[i][j] = stack.get(k);
                                    k++;
                                }
                                if (pieces[DIM - i][DIM - j] == (null)) {
                                    pieces[DIM - i - 1][DIM - j - 1] = stack.get(k);
                                    k++;
                                }
                            }
                        }
                        if (i == 4) {
                            for (int j = 0; j < 9; j++) {
                                pieces[i][j] = stack.get(k);
                                k++;
                            }
                        }
                    }
                    // fine caso 4 giocatori
                    // inizio caso 3 giocatori
                    else if (numOfPlayers == 3) {
                        if (i == 0) {
                            int j = 3;
                            if (pieces[i][j] == (null)) {
                                pieces[i][j] = stack.get(k);
                                k++;
                            }
                            if (pieces[DIM - i][DIM - j] == (null)) {
                                pieces[DIM - i - 1][DIM - j - 1] = stack.get(k);
                                k++;
                            }
                        }
                    }
                    if (i == 1) {
                        for (int j = 3; j < 5; j++) {
                            if (pieces[i][j] == (null)) {
                                pieces[i][j] = stack.get(k);
                                k++;
                            }
                            if (pieces[DIM - i][DIM - j] == (null)) {
                                pieces[DIM - i - 1][DIM - j - 1] = stack.get(k);
                                k++;
                            }
                        }
                    }
                    if (i == 2) {
                        for (int j = 2; j < 7; j++) {
                            if (pieces[i][j] == (null)) {
                                pieces[i][j] = stack.get(k);
                                k++;
                            }
                            if (pieces[DIM - i][DIM - j] == (null)) {
                                pieces[DIM - i - 1][DIM - j - 1] = stack.get(k);
                                k++;
                            }
                        }
                    }
                    if (i == 3) {
                        for (int j = 2; j < 9; j++) {
                            if (pieces[i][j] == (null)) {
                                pieces[i][j] = stack.get(k);
                                k++;
                            }
                            if (pieces[DIM - i][DIM - j] == (null)) {
                                pieces[DIM - i - 1][DIM - j - 1] = stack.get(k);
                                k++;
                            }
                        }
                    }
                    if (i == 4) {
                        for (int j = 1; j < 8; j++) {
                            pieces[i][j] = stack.get(k);
                            k++;
                        }
                    }

                    // fine caso 3 giocatori
                    // inizio caso 2 giocatori

                    else if (numOfPlayers == 2) {

                        // riga i==0 vuota

                        if (i == 1) {
                            for (int j = 3; j < 5; j++) {
                                if (pieces[i][j] == (null)) {
                                    pieces[i][j] = stack.get(k);
                                    k++;
                                }
                                if (pieces[DIM - i][DIM - j] == (null)) {
                                    pieces[DIM - i - 1][DIM - j - 1] = stack.get(k);
                                    k++;
                                }
                            }
                        }
                        if (i == 2) {
                            for (int j = 3; j < 6; j++) {
                                if (pieces[i][j] == (null)) {
                                    pieces[i][j] = stack.get(k);
                                    k++;
                                }
                                if (pieces[DIM - i][DIM - j] == (null)) {
                                    pieces[DIM - i - 1][DIM - j - 1] = stack.get(k);
                                    k++;
                                }
                            }
                        }
                        if (i == 3) {
                            for (int j = 2; j < 8; j++) {
                                if (pieces[i][j] == (null)) {
                                    pieces[i][j] = stack.get(k);
                                    k++;
                                }
                                if (pieces[DIM - i][DIM - j] == (null)) {
                                    pieces[DIM - i - 1][DIM - j - 1] = stack.get(k);
                                    k++;
                                }
                            }
                        }
                        if (i == 4) {
                            for (int j = 1; j < 8; j++) {
                                if (pieces[i][j].equals(null)) {
                                    pieces[i][j] = stack.get(k);
                                    k++;
                                }
                            }
                        }
                    }
                }
                indexOfStackCard = k;
            }
            return pieces;
        }
        Boolean[][] calculateSelectable() {
            Boolean[][] selectable = new Boolean[DIM][DIM];
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    selectable[i][j] = !(pieces[i][j] == (null)) && freecorner(i, j); //TODO: creare funzione freecorner
                }
            }
            return selectable;
        }

}


