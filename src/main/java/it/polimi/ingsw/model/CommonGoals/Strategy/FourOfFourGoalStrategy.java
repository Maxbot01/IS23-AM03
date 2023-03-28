package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.helpers.Quartet;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;

/**
 * Strategy of FourOfFour: it looks for 4 combinations of 4 boardCards of the same color (it can vary between combinations),
 * arranged in any manner, granted that they are connected
 */
public class FourOfFourGoalStrategy implements CommonGoalStrategy {

    /**
     * Algorithm of FourOfFour
     * @param Mat
     * @return boolean
     */
    public boolean goalCompleted(BoardCard[][] Mat) {
        int rows = Mat.length;
        int cols = Mat[0].length;
        int completed = 0;
        ArrayList<Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>>>
                combinations = new ArrayList<>();
        int correctLines = 0;
        ArrayList<Pair<Integer,Integer>> savedNumbOfCoord = new ArrayList<>();
        ArrayList<Pair<Integer,Integer>> savedTotalCoord = new ArrayList<>();
/*
inserisco un arraylist di savedCoord in cui metto tutte le coordinate che ho visto, così da ottenere
il numero totale per quel colore, e non considerarla nei for annidati, così da evitarmi di rifare il calcolo
delle combinazioni
 */
        for(int i = 0; i < rows && completed == 0; i++){
            for(int j = 0; j < cols && completed == 0; j++){
                if(Mat[i][j].getColor() != colorType.EMPTY_SPOT){
                    Pair<Integer,Integer> tmp = new Pair<>(i,j);
                    if(!pairIsPresent(tmp,savedTotalCoord)) {
/* resetto il numero di carte viste e il numero di combinazioni trovate per il colore precedente */
                        savedNumbOfCoord.removeAll(savedNumbOfCoord);
                        combinations.removeAll(combinations);

                        startSearchOfCombinations(Mat, Mat[i][j].getColor(), i, j, savedTotalCoord, combinations, savedNumbOfCoord);

                        /* stampo i risultati che mi interessano:
                        System.out.println("\nNumero di combinazioni trovate per " + i + "-" + j + ": " + combinations.size() + "\nNumero di" +
                                " BoardCards relative" + " alle combinazioni: " + savedNumbOfCoord.size() + "\n");

                        */
                        if (savedNumbOfCoord.size() >= 4) {
                        /*
                            System.out.println("Prima di fare la add, correctLines vale: " + correctLines);
                         */
                            correctLines += calculateQuartets(combinations, savedNumbOfCoord);
                        /*
                            System.out.println("Dopo aver fatto la add, correctLines vale: " + correctLines);
                         */
                            if (correctLines >= 4) {
                                completed = 1;
                            }
                        }
                    }
                }
            }
        }
        if(completed == 1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Private method used to look for all the possible combinations from a starting boardCard
     * @param mat
     * @param chosenColor
     * @param x
     * @param y
     * @param xPrec
     * @param yPrec
     * @param considered
     * @param combinations
     * @param savedNumbOfCoord
     */
    private void findCombinations(BoardCard[][] mat, colorType chosenColor, int x, int y, int xPrec, int yPrec,
                ArrayList<Pair<Integer,Integer>> considered, ArrayList<Quartet<Pair<Integer,Integer>,
                Pair<Integer,Integer>,Pair<Integer,Integer>, Pair<Integer,Integer>>> combinations,
                ArrayList<Pair<Integer,Integer>> savedNumbOfCoord){
/*
W-S-E-N mi servono er le combinazioni a T
 */
        int W = 0;
        int S = 0;
        int E = 0;
        int N = 0;
/*
aggiungo l'elemento a savedNumbOfCoord per tenere traccia del numero di tessere totali considerate per quel colore, nel main verrà resettato per ogni
elemento di partenza studiato
 */
        Pair<Integer,Integer> val = new Pair<>(x,y);
        if(!pairIsPresent(val,savedNumbOfCoord)) {
            savedNumbOfCoord.add(val);
        }
/*
aggiungo l'elemento nuovo alla lista di considered così da mantenere le coordinate viste e la size della
combinazione fino a quel momento
*/
        considered.add(new Pair<>(x,y));
/*
se la size di considered è 4 allora salvo la combinazione nel quartetto di pair (ovvero delle coordinate)
combinations, così da averla per il controllo finale, solo se non è già presente
 */
        if(considered.size() == 4){
            Pair<Integer,Integer> first = new Pair<>(considered.get(0).getFirst(),considered.get(0).getSecond());
            Pair<Integer,Integer> second = new Pair<>(considered.get(1).getFirst(),considered.get(1).getSecond());
            Pair<Integer,Integer> third = new Pair<>(considered.get(2).getFirst(),considered.get(2).getSecond());
            Pair<Integer,Integer> fourth = new Pair<>(considered.get(3).getFirst(),considered.get(3).getSecond());
            Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>, Pair<Integer,Integer>> tmp = new Quartet<>(first,second,third,fourth);
            if(!quartetIsPresent(tmp,combinations)) {
                combinations.add(tmp);
            }
/*
se la size non è 4 mando ricorsivamente la funzione nelle 4 direzioni se possibile, creando un branch alla volta
in cui cercare una combinazione
 */
        }else{
            if(y-1 >= 0 && mat[x][y-1].getColor().equals(chosenColor) && y-1 != yPrec) { /* W card */
                findCombinations(mat,chosenColor,x,y-1,x,y,considered,combinations,savedNumbOfCoord);
                W = 1;
            }
            if(x+1 < mat.length && mat[x+1][y].getColor().equals(chosenColor) && x+1 != xPrec){ /* S card */
                findCombinations(mat,chosenColor,x+1,y,x,y,considered,combinations,savedNumbOfCoord);
                S = 1;
            }
            if(y+1 < mat[0].length && mat[x][y+1].getColor().equals(chosenColor) && y+1 != yPrec){ /* E card */
                findCombinations(mat,chosenColor,x,y+1,x,y,considered,combinations,savedNumbOfCoord);
                E = 1;
            }
            if(x-1 >= 0 && mat[x-1][y].getColor().equals(chosenColor) && x-1 != xPrec){ /* N card */
                findCombinations(mat,chosenColor,x-1,y,x,y,considered,combinations,savedNumbOfCoord);
                N = 1;
            }
/*
controllo la possibilità di sequenze a T (ovvero 3 cards in fila e 1 card adiacente all'elemento centrale della
riga di 3)
 */
            if(W == 1 && S == 1 && E == 1){
                Pair<Integer,Integer> first = new Pair<>(x,y-1);
                Pair<Integer,Integer> second = new Pair<>(x,y);
                Pair<Integer,Integer> third = new Pair<>(x,y+1);
                Pair<Integer,Integer> fourth = new Pair<>(x+1,y);
                Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>, Pair<Integer,
                        Integer>> tmp = new Quartet<>(first,second,third,fourth);
                if(!quartetIsPresent(tmp,combinations)) {
                    combinations.add(tmp);
                }
            }
            if(N == 1 && S == 1 && E == 1){
                Pair<Integer,Integer> first = new Pair<>(x-1,y);
                Pair<Integer,Integer> second = new Pair<>(x,y);
                Pair<Integer,Integer> third = new Pair<>(x+1,y);
                Pair<Integer,Integer> fourth = new Pair<>(x,y+1);
                Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>, Pair<Integer,
                        Integer>> tmp = new Quartet<>(first,second,third,fourth);
                if(!quartetIsPresent(tmp,combinations)) {
                    combinations.add(tmp);
                }
            }
            if(W == 1 && N == 1 && E == 1){
                Pair<Integer,Integer> first = new Pair<>(x,y-1);
                Pair<Integer,Integer> second = new Pair<>(x,y);
                Pair<Integer,Integer> third = new Pair<>(x,y+1);
                Pair<Integer,Integer> fourth = new Pair<>(x-1,y);
                Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>, Pair<Integer,
                        Integer>> tmp = new Quartet<>(first,second,third,fourth);
                if(!quartetIsPresent(tmp,combinations)) {
                    combinations.add(tmp);
                }
            }
            if(W == 1 && S == 1 && N == 1){
                Pair<Integer,Integer> first = new Pair<>(x-1,y);
                Pair<Integer,Integer> second = new Pair<>(x,y-1);
                Pair<Integer,Integer> third = new Pair<>(x,y);
                Pair<Integer,Integer> fourth = new Pair<>(x+1,y);
                Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>, Pair<Integer,
                        Integer>> tmp = new Quartet<>(first,second,third,fourth);
                if(!quartetIsPresent(tmp,combinations)) {
                    combinations.add(tmp);
                }
            }
        }
/*
alla fine rimuovo l'ultimo elemento così da non finire in iterazioni infinite (loop), evitando anche duplicati,
tornando indietro per cercare possibili altre combinazioni con gli elementi precedenti
 */
        considered.remove(considered.size()-1);
    }

    /**
     * Private method used to check how many non-overlapping combinations have been found
     * @param combinations
     * @param savedNumbOfCoord
     * @return int
     */
    private int calculateQuartets(ArrayList<Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,
            Integer>,Pair<Integer,Integer>>> combinations, ArrayList<Pair<Integer,Integer>> savedNumbOfCoord){
        int ris;

        if(savedNumbOfCoord.size() < 8){
            return ris = 1;
        }else if(savedNumbOfCoord.size() < 12){
            ris = 1;
            for(int i = 0; i < combinations.size() && ris == 1; i++){
                for(int j = 0; j != i && j < combinations.size() && ris == 1; j++){
                    if(noOverlap(combinations.get(i),combinations.get(j))){
                        ris = 2;
                    }
                }
            }
        }else if(savedNumbOfCoord.size() < 16){
            ris = 2;
            for(int i = 0; i < combinations.size() && ris == 2; i++){
                for(int j = 0; j != i && j < combinations.size() && ris == 2; j++){
                    if(noOverlap(combinations.get(i),combinations.get(j))){
                        for(int z = 0; z != j && z != i && z < combinations.size() && ris == 2; z++){
                            if(noOverlap(combinations.get(i),combinations.get(z)) &&
                                noOverlap(combinations.get(j),combinations.get(z))){
                                ris = 3;
                            }
                        }
                    }
                }
            }
        }else {
            ris = 3;
            for(int i = 0; i < combinations.size() && ris == 3; i++){
                for(int j = 0; j != i && j < combinations.size() && ris == 3; j++){
                    if(noOverlap(combinations.get(i),combinations.get(j))){
                        for(int z = 0; z != j && z != i && z < combinations.size() && ris == 3; z++){
                            if(noOverlap(combinations.get(i),combinations.get(z)) &&
                                    noOverlap(combinations.get(j),combinations.get(z))){
                                for(int w = 0; w != z && w != j && w != i && w < combinations.size() && ris == 3; w++){
                                    if(noOverlap(combinations.get(i),combinations.get(w)) &&
                                            noOverlap(combinations.get(j),combinations.get(w)) &&
                                            noOverlap(combinations.get(z),combinations.get(w))){
                                        ris = 4;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ris;
    }

    /**
     * Private method used to check that two combinations don't overlap
     * @param firstQ
     * @param secondQ
     * @return boolena
     */
    private boolean noOverlap(Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer, Integer>,Pair<
            Integer,Integer>> firstQ, Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,
            Integer>,Pair<Integer,Integer>> secondQ){
/*
per ogni pair di interi nel primo quartetto cerco un pair uguale nel secondo, se lo trovo noOverlap è falso
 */
        if((firstQ.getFirst().getFirst().equals(secondQ.getFirst().getFirst()) && firstQ.getFirst().getSecond().equals(secondQ.getFirst().getSecond())) ||
            (firstQ.getFirst().getFirst().equals(secondQ.getSecond().getFirst()) && firstQ.getFirst().getSecond().equals(secondQ.getSecond().getSecond())) ||
            (firstQ.getFirst().getFirst().equals(secondQ.getThird().getFirst()) && firstQ.getFirst().getSecond().equals(secondQ.getThird().getSecond())) ||
            (firstQ.getFirst().getFirst().equals(secondQ.getFourth().getFirst()) && firstQ.getFirst().getSecond().equals(secondQ.getFourth().getSecond()))){
            return false;
        }
        if((firstQ.getSecond().getFirst().equals(secondQ.getFirst().getFirst()) && firstQ.getSecond().getSecond().equals(secondQ.getFirst().getSecond())) ||
            (firstQ.getSecond().getFirst().equals(secondQ.getSecond().getFirst()) && firstQ.getSecond().getSecond().equals(secondQ.getSecond().getSecond())) ||
            (firstQ.getSecond().getFirst().equals(secondQ.getThird().getFirst()) && firstQ.getSecond().getSecond().equals(secondQ.getThird().getSecond())) ||
            (firstQ.getSecond().getFirst().equals(secondQ.getFourth().getFirst()) && firstQ.getSecond().getSecond().equals(secondQ.getFourth().getSecond()))){
            return false;
        }
        if((firstQ.getThird().getFirst().equals(secondQ.getFirst().getFirst()) && firstQ.getThird().getSecond().equals(secondQ.getFirst().getSecond())) ||
            (firstQ.getThird().getFirst().equals(secondQ.getSecond().getFirst()) && firstQ.getThird().getSecond().equals(secondQ.getSecond().getSecond())) ||
            (firstQ.getThird().getFirst().equals(secondQ.getThird().getFirst()) && firstQ.getThird().getSecond().equals(secondQ.getThird().getSecond())) ||
            (firstQ.getThird().getFirst().equals(secondQ.getFourth().getFirst()) && firstQ.getThird().getSecond().equals(secondQ.getFourth().getSecond()))){
            return false;
        }
        if((firstQ.getFourth().getFirst().equals(secondQ.getFirst().getFirst()) && firstQ.getFourth().getSecond().equals(secondQ.getFirst().getSecond())) ||
            (firstQ.getFourth().getFirst().equals(secondQ.getSecond().getFirst()) && firstQ.getFourth().getSecond().equals(secondQ.getSecond().getSecond())) ||
            (firstQ.getFourth().getFirst().equals(secondQ.getThird().getFirst()) && firstQ.getFourth().getSecond().equals(secondQ.getThird().getSecond())) ||
            (firstQ.getFourth().getFirst().equals(secondQ.getFourth().getFirst()) && firstQ.getFourth().getSecond().equals(secondQ.getFourth().getSecond()))){
            return false;
        }
        return true;
    }

    /**
     * Private method used for each boardCard of the player's shelf to add the found combinations to the collection
     * @param mat
     * @param chosenColor
     * @param x
     * @param y
     * @param savedTotalCoord
     * @param combinations
     * @param savedNumbOfCoord
     */
    private void startSearchOfCombinations(BoardCard[][] mat, colorType chosenColor, int x, int y, ArrayList<Pair<Integer,Integer>> savedTotalCoord,
                                           ArrayList<Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>>>
                                           combinations, ArrayList<Pair<Integer,Integer>> savedNumbOfCoord) {

        ArrayList<Pair<Integer,Integer>> considered = new ArrayList<>();
        savedTotalCoord.add(new Pair<>(x,y));
        /*System.out.println("Elementi di savedTotalCoord: i=" + savedTotalCoord.get(savedTotalCoord.size()-1).getFirst() + " j=" +
                savedTotalCoord.get(savedTotalCoord.size()-1).getSecond());
        */
        findCombinations(mat,chosenColor,x,y,x,y,considered,combinations,savedNumbOfCoord);

        if(y-1 >= 0 && mat[x][y-1].getColor().equals(chosenColor)) { /* W card */
            Pair<Integer,Integer> tmp = new Pair<>(x,y-1);
            if(!pairIsPresent(tmp,savedTotalCoord)) {
                startSearchOfCombinations(mat, chosenColor, x, y - 1, savedTotalCoord, combinations, savedNumbOfCoord);
            }
        }
        if(x+1 < mat.length && mat[x+1][y].getColor().equals(chosenColor)){ /* S card */
            Pair<Integer,Integer> tmp = new Pair<>(x+1,y);
            if(!pairIsPresent(tmp,savedTotalCoord)) {
                startSearchOfCombinations(mat, chosenColor, x + 1, y, savedTotalCoord, combinations, savedNumbOfCoord);
            }
        }
        if(y+1 < mat[0].length && mat[x][y+1].getColor().equals(chosenColor)){ /* E card */
            Pair<Integer,Integer> tmp = new Pair<>(x,y+1);
            if(!pairIsPresent(tmp,savedTotalCoord)) {
                startSearchOfCombinations(mat, chosenColor, x, y + 1, savedTotalCoord, combinations, savedNumbOfCoord);
            }
        }
        if(x-1 >= 0 && mat[x-1][y].getColor().equals(chosenColor)){ /* N card */
            Pair<Integer,Integer> tmp = new Pair<>(x-1,y);
            if(!pairIsPresent(tmp,savedTotalCoord)) {
                startSearchOfCombinations(mat, chosenColor, x - 1, y, savedTotalCoord, combinations, savedNumbOfCoord);
            }
        }
    }

    /**
     * Private method to check if the pair of coordinates is in a given list
     * @param tmp
     * @param savedTotalCoord
     * @return boolean
     */
    private boolean pairIsPresent(Pair<Integer,Integer> tmp, ArrayList<Pair<Integer,Integer>> savedTotalCoord){
        int present = 0;
        for(int i = 0; i < savedTotalCoord.size() && present == 0; i++){
            if(savedTotalCoord.get(i).getFirst().equals(tmp.getFirst()) && savedTotalCoord.get(i).getSecond().equals(tmp.getSecond())){
                present = 1;
            }
        }
        return present == 1;
    }

    /**
     * Private method used to check if the combination is in a given list
     * @param tmp
     * @param combinations
     * @return boolean
     */
    private boolean quartetIsPresent(Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>> tmp, ArrayList<
            Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>>> combinations){
        int present = 0;

        for(int i = 0; i < combinations.size() && present == 0; i++){
            if(combinations.get(i).getFirst().getFirst().equals(tmp.getFirst().getFirst()) && combinations.get(i).getFirst().getSecond().equals(tmp.getFirst().getSecond()) &&
                combinations.get(i).getSecond().getFirst().equals(tmp.getSecond().getFirst()) && combinations.get(i).getSecond().getSecond().equals(tmp.getSecond().getSecond()) &&
                combinations.get(i).getThird().getFirst().equals(tmp.getThird().getFirst()) && combinations.get(i).getThird().getSecond().equals(tmp.getThird().getSecond()) &&
                combinations.get(i).getFourth().getFirst().equals(tmp.getFourth().getFirst()) && combinations.get(i).getFourth().getSecond().equals(tmp.getFourth().getSecond())){
                present = 1;
            }
        }
        return present == 1;
    }
}
