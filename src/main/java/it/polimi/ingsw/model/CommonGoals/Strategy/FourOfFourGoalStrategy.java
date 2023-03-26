package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.helpers.Quartet;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import javax.swing.*;
import java.util.ArrayList;

public class FourOfFourGoalStrategy implements CommonGoalStrategy {

    public boolean goalCompleted(BoardCard[][] Mat) {
        int rows = Mat.length;
        int cols = Mat[0].length;
        int completed = 0;
        ArrayList<Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,Integer>>>
                combinations = new ArrayList<>();
        ArrayList<Pair<Integer,Integer>> considered = new ArrayList<>();
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
                if(Mat[i][j] != null){
                    Pair<Integer,Integer> tmp = new Pair<>(i,j);
                    if(!savedTotalCoord.contains(tmp)) {
                        savedNumbOfCoord.removeAll(savedNumbOfCoord);
                        findCombinations(Mat, Mat[i][j].getColor(), i, j, i, j, considered, combinations, savedNumbOfCoord, savedTotalCoord);
                        /* stampo i risultati che mi interessano */
                        System.out.println("\nNumero di combinazioni trovate: " + combinations.size() + "\nNumero di BoardCards relative alle combinazioni: " +
                                savedNumbOfCoord.size() + "\n");
                        if (savedNumbOfCoord.size() >= 4) {
                            System.out.println("Prima di fare la add, correctLines vale: " + correctLines);
                            correctLines += calculateQuartets(combinations, savedNumbOfCoord);
                            System.out.println("Dopo aver fatto la add, correctLines vale: " + correctLines);
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
    private void findCombinations(BoardCard[][] mat, colorType chosenColor, int x, int y, int xPrec, int yPrec,
                ArrayList<Pair<Integer,Integer>> considered, ArrayList<Quartet<Pair<Integer,Integer>,
                Pair<Integer,Integer>,Pair<Integer,Integer>, Pair<Integer,Integer>>> combinations,
                ArrayList<Pair<Integer,Integer>> savedNumbOfCoord, ArrayList<Pair<Integer,Integer>> savedTotalCoord){
/*
W-S-E-N mi servono er le combinazioni a T
 */
        int W = 0;
        int S = 0;
        int E = 0;
        int N = 0;

        savedNumbOfCoord.add(new Pair<>(x,y));
        savedTotalCoord.add(new Pair<>(x,y));
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
            if(!combinations.contains(tmp)) {
                combinations.add(tmp);
            }
/*
se la size non è 4 mando ricorsivamente la funzione nelle 4 direzioni se possibile, creando un branch alla volta
in cui cercare una combinazione
 */
        }else{
            if(y-1 >= 0 && mat[x][y-1].getColor().equals(chosenColor) && y-1 != yPrec) { /* W card */
                findCombinations(mat,chosenColor,x,y-1,x,y,considered,combinations,savedNumbOfCoord,savedTotalCoord);
                W = 1;
            }
            if(x+1 < mat.length && mat[x+1][y].getColor().equals(chosenColor) && x+1 != xPrec){ /* S card */
                findCombinations(mat,chosenColor,x+1,y,x,y,considered,combinations,savedNumbOfCoord,savedTotalCoord);
                S = 1;
            }
            if(y+1 < mat[0].length && mat[x][y+1].getColor().equals(chosenColor) && y+1 != yPrec){ /* E card */
                findCombinations(mat,chosenColor,x,y+1,x,y,considered,combinations,savedNumbOfCoord,savedTotalCoord);
                E = 1;
            }
            if(x-1 >= 0 && mat[x-1][y].getColor().equals(chosenColor) && x-1 != xPrec){ /* N card */
                findCombinations(mat,chosenColor,x-1,y,x,y,considered,combinations,savedNumbOfCoord,savedTotalCoord);
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
                if(!combinations.contains(tmp)) {
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
                if(!combinations.contains(tmp)) {
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
                if(!combinations.contains(tmp)) {
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
                if(!combinations.contains(tmp)) {
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
    private boolean noOverlap(Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer, Integer>,Pair<
            Integer,Integer>> firstQ, Quartet<Pair<Integer,Integer>,Pair<Integer,Integer>,Pair<Integer,
            Integer>,Pair<Integer,Integer>> secondQ){
/*
per ogni pair di interi nel primo quartetto cerco un pair uguale nel secondo, se lo trovo noOverlap è falso
 */
        if(firstQ.getFirst().equals(secondQ.getFirst()) || firstQ.getFirst().equals(secondQ.getSecond()) ||
                firstQ.getFirst().equals(secondQ.getThird()) || firstQ.getFirst().equals(secondQ.getFourth())){
            return false;
        }
        if(firstQ.getSecond().equals(secondQ.getFirst()) || firstQ.getSecond().equals(secondQ.getSecond()) ||
                firstQ.getSecond().equals(secondQ.getThird()) || firstQ.getSecond().equals(secondQ.getFourth())){
            return false;
        }
        if(firstQ.getThird().equals(secondQ.getFirst()) || firstQ.getThird().equals(secondQ.getSecond()) ||
                firstQ.getThird().equals(secondQ.getThird()) || firstQ.getThird().equals(secondQ.getFourth())){
            return false;
        }
        if(firstQ.getFourth().equals(secondQ.getFirst()) || firstQ.getFourth().equals(secondQ.getSecond()) ||
                firstQ.getFourth().equals(secondQ.getThird()) || firstQ.getFourth().equals(secondQ.getFourth())){
            return false;
        }
        return true;
    }
}
