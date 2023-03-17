package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.Game;

// Bisognerebbe chiamarla in questo modo:
// BoardCard[][] cards = new BoardCard[6][5];
 // Shelf shelf = new Shelf(cards);

public class Shelf extends Game {
    private BoardCard[][] ShelfCards;
    private static final int ROWSLEN = 6 ;
    private static final int COLUMNSLEN = 5 ;

    public Shelf(BoardCard[][] ShelfCards) {
        this.ShelfCards = ShelfCards;
    }

    public boolean isFull() {
        for (int i = 0; i < ROWSLEN; i++) {
            for (int j = 0; j < COLUMNSLEN; j++) {
                if (ShelfCards[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
