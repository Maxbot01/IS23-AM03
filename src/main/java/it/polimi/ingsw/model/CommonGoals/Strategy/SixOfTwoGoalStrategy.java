package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;

/**
 * Strategy of SixOfTwo: it looks for 6 couples of 2 boardCards of the same color (the color can vary between couples)
 */
public class SixOfTwoGoalStrategy implements CommonGoalStrategy{
    /**
     * Algorithm of SixOfTwo
     * @param Mat
     * @return boolean
     */
    public boolean goalCompleted(BoardCard[][] Mat){
        ArrayList<Pair<Pair<Integer,Integer>,Pair<Integer,Integer>>> combinations = new ArrayList<>();

        for(int i = 0; i < Mat.length; i++){
            for(int j = 0; j < Mat[0].length; j++){
                if(Mat[i][j].getColor() != colorType.EMPTY_SPOT) {
                    if (j+1 < Mat[0].length) {
                        if(Mat[i][j+1].getColor() != colorType.EMPTY_SPOT) {
                            if (Mat[i][j].getColor().equals(Mat[i][j + 1].getColor())) {
                                Pair<Integer, Integer> val1 = new Pair<>(i, j);
                                Pair<Integer, Integer> val2 = new Pair<>(i, j + 1);
                                Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> tmp = new Pair<>(val1, val2);
                                if(!dupletIsPresent(combinations,tmp)) {
                                    combinations.add(tmp);
                                }
                            }
                        }
                    }
                    if(i+1 < Mat.length) {
                        if(Mat[i+1][j].getColor() != colorType.EMPTY_SPOT) {
                            if (Mat[i][j].getColor().equals(Mat[i + 1][j].getColor())) {
                                Pair<Integer, Integer> val1 = new Pair<>(i, j);
                                Pair<Integer, Integer> val2 = new Pair<>(i + 1, j);
                                Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> tmp = new Pair<>(val1, val2);
                                if(!dupletIsPresent(combinations,tmp)) {
                                    combinations.add(tmp);
                                }
                            }
                        }
                    }
                }
            }
        }
        return calculateNumberOfCombinations(combinations);
    }
    /**
     * Private method used to calculate if the number of combinations reaches the desired total
     * @param combinations
     * @return boolean
     */
    private boolean calculateNumberOfCombinations(ArrayList<Pair<Pair<Integer,Integer>,Pair<Integer,Integer>>>
                                                      combinations){
        if(combinations.size() >= 6) {
            for(int i = 0; i < combinations.size(); i++){
                Pair<Pair<Integer,Integer>,Pair<Integer,Integer>> one = combinations.get(i);
                for(int j = 0; j != i && j < combinations.size(); j++){
                    Pair<Pair<Integer,Integer>,Pair<Integer,Integer>> two = combinations.get(j);
                    if(noOverlap(one,two)){
                        for(int z = 0; z != i && z != j && z < combinations.size(); z++){
                            Pair<Pair<Integer,Integer>,Pair<Integer,Integer>> three = combinations.get(z);
                            if(noOverlap(one,three) && noOverlap(two,three)){
                                for(int w = 0; w != i && w != j && w != z && w < combinations.size(); w++){
                                    Pair<Pair<Integer,Integer>,Pair<Integer,Integer>> four = combinations.get(w);
                                    if(noOverlap(one,four) && noOverlap(two,four) && noOverlap(three,four)){
                                        for(int x = 0; x != i && x != j && x != z && x != w && x < combinations.size(); x++){
                                            Pair<Pair<Integer,Integer>,Pair<Integer,Integer>> five = combinations.get(x);
                                            if(noOverlap(one,five) && noOverlap(two,five) && noOverlap(three,five) && noOverlap(four,five)){
                                                for(int y = 0; y != i && y != j && y != z && y != w && y != x && y < combinations.size(); y++){
                                                    Pair<Pair<Integer,Integer>,Pair<Integer,Integer>> six = combinations.get(y);
                                                    if(noOverlap(one,six) && noOverlap(two,six) && noOverlap(three,six) && noOverlap(four,six) && noOverlap(five,six)){
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /** Private method used to check if 2 couples of 2 couples of coordinates overlap
     * @param one
     * @param two
     * @return boolean
     */
    private boolean noOverlap(Pair<Pair<Integer,Integer>,Pair<Integer,Integer>> one, Pair<Pair<Integer,Integer>,
            Pair<Integer,Integer>> two){

        if((one.getFirst().getFirst().equals(two.getFirst().getFirst()) && one.getFirst().getSecond().equals(two.getFirst().getSecond())) ||
            (one.getFirst().getFirst().equals(two.getSecond().getFirst()) && one.getFirst().getSecond().equals(two.getSecond().getSecond())) ||
            (one.getSecond().getFirst().equals(two.getFirst().getFirst()) && one.getSecond().getSecond().equals(two.getFirst().getSecond())) ||
            (one.getSecond().getFirst().equals(two.getSecond().getFirst()) && one.getSecond().getSecond().equals(two.getSecond().getSecond()))){
            return false;
        }else{
            return true;
        }
    }
    /**
     * Private method used to check if a couple of 2 couples of coordinate are in a given list
     * @param group
     * @param tmp
     * @return boolean
     */
    private boolean dupletIsPresent(ArrayList<Pair<Pair<Integer,Integer>,Pair<Integer,Integer>>> group,
                                    Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> tmp){
        for(int i = 0; i < group.size(); i++){
            if(tmp.getFirst().getFirst().equals(group.get(i).getFirst().getFirst()) &&
                tmp.getFirst().getSecond().equals(group.get(i).getFirst().getSecond()) &&
                tmp.getSecond().getFirst().equals(group.get(i).getSecond().getFirst()) &&
                tmp.getSecond().getSecond().equals(group.get(i).getSecond().getSecond())){
               return true;
            }
        }
        return false;
    }

    //toStringCommonGoal
    public String toStringCommonGoal(){
        return "SixOfTwoGoalStrategy";
    }
    public int getIndex(){
        return 4;
    }
}
