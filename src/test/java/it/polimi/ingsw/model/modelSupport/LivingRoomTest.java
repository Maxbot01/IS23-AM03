package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.exceptions.NoMoreCardsException;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LivingRoomTest {

    // stupid way to test whether the starting angles are empty spot
    // EXPECTED: FALSE
    @Test
    void Empty_Angles_getPieces() {
        LivingRoom l = new LivingRoom(4);
        BoardCard[][] mat = l.getPieces();
        assertTrue(mat[0][0].getColor() == colorType.EMPTY_SPOT );
        assertTrue(mat[0][8].getColor() == colorType.EMPTY_SPOT );
        assertTrue(mat[8][0].getColor() == colorType.EMPTY_SPOT );
        assertTrue(mat[8][8].getColor() == colorType.EMPTY_SPOT );


    }

    // tests whether there are TOMBSTONES in the initial Board
    // EXPECTED: FALSE
    @Test
    void No_Starting_TOMB_getPieces() {
        LivingRoom l = new LivingRoom(4);
        BoardCard[][] mat = l.getPieces();
        for(int i=0; i<9;i++){
            for(int j=0; j<9;j++){
                assertFalse(mat[i][j].getColor() == colorType.TOMBSTONE);
            }
        }

    }

    // tests whether a starting Board may trigger refillBoard method
    //  EXPECTED: FALSE
    @Test
    void No_Starting_refillBoard() throws NoMoreCardsException {
        LivingRoom l = new LivingRoom(4);
        assertEquals(l.getPieces(),l.refillBoard());
    }


    // tests whether a completely empty Board triggers refillBoard method
    // EXPECTED: TRUE
    @Test
    void emptyRefill() throws NoMoreCardsException {
        LivingRoom l = new LivingRoom(4);
        BoardCard[][] mat = l.getPieces();
        for(int i=0; i<9;i++){
            for(int j=0; j<9;j++){
               if(!mat[i][j].equals(colorType.EMPTY_SPOT)){
                   BoardCard c = new BoardCard(colorType.TOMBSTONE);
                   mat[i][j] = c;
               }
            }
        }
        mat = l.refillBoard();
        int count = 0;
        for(int i=0; i<9;i++){
            for(int j=0; j<9;j++) {
                if(!mat[i][j].getColor().equals(colorType.TOMBSTONE)){
                    count++;
                }
            }
        }
        assertEquals(81,count);


    }


    // tests the actual starting selectable
    @Test
    void starting_Selectable() {
        LivingRoom l = new LivingRoom(4);
        Boolean[][] matBool = l.calculateSelectable();
        assertTrue(matBool[0][3]);
        assertTrue(matBool[0][4]);
        assertTrue(matBool[1][3]);
        assertTrue(matBool[1][5]);
        assertTrue(matBool[2][2]);
        assertTrue(matBool[2][6]);
        assertTrue(matBool[3][1]);
        assertTrue(matBool[3][8]);
        assertTrue(matBool[4][0]);
        assertTrue(matBool[4][8]);
        assertTrue(matBool[5][0]);
        assertTrue(matBool[5][7]);
        assertTrue(matBool[6][2]);
        assertTrue(matBool[6][6]);
        assertTrue(matBool[7][3]);
        assertTrue(matBool[7][5]);
        assertTrue(matBool[8][4]);
        assertTrue(matBool[8][5]);


    }



    // tests whether updateBoard method creates TOMBSTONE cards in a randomly selected position
    // EXPECTED: TRUE
    @Test
    void updateBoard_creates_TOMBS() throws UnselectableCardException {
        LivingRoom l = new LivingRoom(4);
        Random random = new Random();
        int i = 0;
        int j = 0;
        do {
            i = random.nextInt(8);
            j = random.nextInt(8);
        } while (!l.cardIsSelectable(i,j));
            ArrayList<Pair<Integer, Integer>> a = new ArrayList<>();
            Pair<Integer, Integer> p = new Pair(i, j);
            a.add(p);
            l.updateBoard(a);
            BoardCard[][] mat = l.getPieces();assertTrue(mat[i][j].getColor() == colorType.TOMBSTONE);



    }

    // tests whether getBoardCard returns the card in a randomly selected position
    // EXPECTED: TRUE
    @Test
    void getBoardCardAt() throws UnselectableCardException {
        Random random = new Random();
        int i = random.nextInt(8);
        int j = random.nextInt(8);
        LivingRoom l = new LivingRoom(3);
        BoardCard[][] mat = l.getPieces();
        Pair<Integer,Integer> p = new Pair(i,j);
        assertTrue(mat[i][j].getColor() == l.getBoardCardAt(p).getColor());
    }
}

