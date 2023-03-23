package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;

public class FourOfFourGoalStrategy implements CommonGoalStrategy{

    public boolean goalCompleted(BoardCard[][] Mat){
        int complete = 0;
        int rows = Mat.length;
        int cols = Mat[0].length;
        BoardCard[][] Copy = new BoardCard[rows][cols];
        int combinations = 0;
        ArrayList<Pair<Integer,Integer>> coordinates = new ArrayList<>();
        ArrayList<Pair<Integer,Integer>> threeAdiac = new ArrayList<>();
        int correctLines = 0;

        /* copia di Mat in Copy */
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols-1; j++){
                Copy[i][j] = Mat[i][j];
            }
        }

        for(int i = 0; i < rows && complete == 0; i++){
            for(int j = 0; j < cols && complete == 0; j++){
                if(Copy[i][j] != null) {
                    findCombination(i, j, Copy, coordinates, threeAdiac);
                    combinations = coordinates.size();
                    if(combinations / 4 == 0){
                        /* elimina le tessere nella copy con coordinates */
                        for (Pair<Integer,Integer> position: coordinates) {
                            Mat[position.getFirst()][position.getSecond()] = null;
                        }
                        /* svuoto le liste di coordinate della sequenza e delle adiacenze */
                        coordinates.removeAll(coordinates);
                        threeAdiac.removeAll(threeAdiac);
                    } else if (combinations / 4 == 1) {
                        correctLines++;
                        /* elimina le tessere nella copy con coordinates */
                        for (Pair<Integer,Integer> position: coordinates) {
                            Mat[position.getFirst()][position.getSecond()] = null;
                        }
                        /* svuoto le liste di coordinate della sequenza e delle adiacenze */
                        coordinates.removeAll(coordinates);
                        threeAdiac.removeAll(threeAdiac);
                    } else if (combinations / 4 == 2) {
                        correctLines += eightAndTwelveAlgorithm(threeAdiac, Mat);
                    } else if (combinations / 4 == 3) {
                        correctLines += eightAndTwelveAlgorithm(threeAdiac, Mat);
                    } else if (combinations / 4 == 4) {
                        correctLines +=;
                    }
                    if (correctLines >= 4) {
                        complete = 1;
                    }
                }
            }
        }
        if(complete == 1){
            return true;
        }else{
            return false;
        }
    }
    private void findCombination(int x, int y, BoardCard[][] mat, ArrayList<Pair<Integer,Integer>> coordinates,
                                ArrayList<Pair<Integer,Integer>> threeAdiac){
        int rows = mat.length;
        int cols = mat[0].length;
        colorType colore = mat[x][y].getColor();
        int cont = 0;

        Pair<Integer,Integer> startingCard = new Pair<>(x,y);
        coordinates.add(startingCard);

        if(y-1 >= 0){ /* card -> W */
            if(colore.equals(mat[x][y-1].getColor())){
                findCombination(x, y-1, mat, coordinates, threeAdiac);
                cont++;
            }
        }
        if(x+1 < rows){ /* card -> S */
            if(colore.equals(mat[x+1][y].getColor())){
                findCombination(x+1, y, mat, coordinates, threeAdiac);
                cont++;
            }
        }
        if(y+1 < cols){ /* card -> E */
            if(colore.equals(mat[x][y+1].getColor())){
                findCombination(x, y+1, mat, coordinates, threeAdiac);
                cont++;
            }
        }
        if(x-1 >= 0){ /* card -> N */
            if(colore.equals(mat[x-1][y].getColor())){
                findCombination(x-1, y, mat, coordinates, threeAdiac);
                cont++;
            }
        }
        if(cont >= 3){
            threeAdiac.add(startingCard);
        }
    }
    /* coord corrisponde a threeAdiac */
    private int eightAndTwelveAlgorithm(ArrayList<Pair<Integer,Integer>> coord, BoardCard[][] mat){
        ArrayList<Pair<Integer,Integer>> seen = new ArrayList<>();
        int success = 0;

/* Nel caso di n/4 = 8 basta fare un singolo controllo sulle threeAdiac, non ciclare tutte quelle trovate */
        Pair<Integer,Integer> cross = new Pair<>(coord.get(0).getFirst(), coord.get(0).getSecond());

        if(mat[cross.getFirst()][cross.getSecond()].getColor().equals(
                mat[cross.getFirst()][cross.getSecond()-1].getColor())){
            seen.add(cross);
            calculateBranchSize(mat, cross.getFirst(), cross.getSecond()-1, seen);
            if(seen.size() >= 4){
                success = 1;
            }
        }
        if(success == 0 && mat[cross.getFirst()][cross.getSecond()].getColor().equals(
                mat[cross.getFirst()+1][cross.getSecond()].getColor())){
            seen.add(cross);
            calculateBranchSize(mat, cross.getFirst()+1, cross.getSecond(), seen);
            if(seen.size() >= 4){
                success = 1;
            }
        }
        if(success == 0 && mat[cross.getFirst()][cross.getSecond()].getColor().equals(
                mat[cross.getFirst()][cross.getSecond()+1].getColor())){
            seen.add(cross);
            calculateBranchSize(mat, cross.getFirst(), cross.getSecond()+1, seen);
            if(seen.size() >= 4){
                success = 1;
            }
        }
        if(success == 0 && mat[cross.getFirst()][cross.getSecond()].getColor().equals(
                mat[cross.getFirst()-1][cross.getSecond()].getColor())){
            seen.add(cross);
            calculateBranchSize(mat, cross.getFirst()-1, cross.getSecond(), seen);
            if(seen.size() >= 4){
                success = 1;
            }
        }
/* CASO PER N/4=12 ----------------------------------------------- */

        for(int z = 0; z < coord.size() && success == 0; z++){
            /* Nel caso di n/4 = 8 basta fare un singolo controllo sulle threeAdiac, non ciclare tutte quelle trovate */
            /* Pair<Integer,Integer> */ cross = new Pair<>(coord.get(z).getFirst(), coord.get(z).getSecond());
            int branchCont = 0;

            if(mat[cross.getFirst()][cross.getSecond()].getColor().equals(
                    mat[cross.getFirst()][cross.getSecond()-1].getColor())){
                /* seen.add(cross); NON DEVO AGGIUNGERE cross per il 12 */
                calculateBranchSize(mat, cross.getFirst(), cross.getSecond()-1, seen);
                if(seen.size() >= 4){
                    branchCont++;
                }
            }
            if(branchCont < 2 && mat[cross.getFirst()][cross.getSecond()].getColor().equals(
                    mat[cross.getFirst()+1][cross.getSecond()].getColor())){
                /* seen.add(cross); */
                calculateBranchSize(mat, cross.getFirst()+1, cross.getSecond(), seen);
                if(seen.size() >= 4){
                    branchCont++;
                }
            }
            if(branchCont < 2 && mat[cross.getFirst()][cross.getSecond()].getColor().equals(
                    mat[cross.getFirst()][cross.getSecond()+1].getColor())){
                /* seen.add(cross); */
                calculateBranchSize(mat, cross.getFirst(), cross.getSecond()+1, seen);
                if(seen.size() >= 4){
                    branchCont++;
                }
            }
            if(branchCont < 2 && mat[cross.getFirst()][cross.getSecond()].getColor().equals(
                    mat[cross.getFirst()-1][cross.getSecond()].getColor())){
                /* seen.add(cross); */
                calculateBranchSize(mat, cross.getFirst()-1, cross.getSecond(), seen);
                if(seen.size() >= 4){
                    branchCont++;
                }
            }
            if(branchCont == 2){
                success = 1;
            }
        }
        if(success == 1){
            return 3;
        }else{
            return 2;
        }

/* ---------------------------------------- */

        if(success == 1){
            return 2;
        }else{
            return 1;
        }
    }
    private void calculateBranchSize(BoardCard[][] mat, int i, int j, ArrayList<Pair<Integer,Integer>> seen){
        int rows = mat.length;
        int cols = mat[0].length;
        colorType startColor = mat[i][j].getColor();


        if(j-1 >= 0 && mat[i][j-1].getColor().equals(startColor)){
            Pair<Integer,Integer> one = new Pair<>(i,j-1);
            if(!seen.contains(one)){
                seen.add(one);
                calculateBranchSize(mat, i, j-1, seen);
            }
        }
        if(i+1 < rows && mat[i+1][j].getColor().equals(startColor)){
            Pair<Integer,Integer> two = new Pair<>(i+1,j);
            if(!seen.contains(two)){
                seen.add(two);
                calculateBranchSize(mat, i+1, j, seen);
            }
        }
        if(j+1 < cols && mat[i][j+1].getColor().equals(startColor)){
            Pair<Integer,Integer> three = new Pair<>(i,j+1);
            if(!seen.contains(three)){
                seen.add(three);
                calculateBranchSize(mat, i, j+1, seen);
            }
        }
        if(i-1 >= 0 && mat[i-1][j].getColor().equals(startColor)){
            Pair<Integer,Integer> four = new Pair<>(i-1,j);
            if(!seen.contains(four)){
                seen.add(four);
                calculateBranchSize(mat, i-1, j, seen);
            }
        }
        
    }
}

