package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;

import java.util.ArrayList;

public class SixOfTwoGoalStrategy implements CommonGoalStrategy{
    public boolean calculateGoalPoints(BoardCard[][] Mat){
        ArrayList<Pair<Integer,Integer>> verticals = new ArrayList<>();
        int completed = 0;
        int couples = 0;

        for(int i = 0; i < Mat.length-1 && completed == 0; i++){
            for(int j = 0; j < Mat[0].length-1 && completed == 0; j++){
                Pair<Integer,Integer> coordinatesCenter = new Pair<>(i,j);
                Pair<Integer,Integer> coordinatesdx = new Pair<>(i,j+1);

                if(!verticals.contains(coordinatesCenter) && !verticals.contains(coordinatesdx)) {
                    if (Mat[i][j].getColor().equals(Mat[i][j + 1].getColor())) {
                        j++;
                        couples++;
                    } else if (Mat[i][j].getColor().equals(Mat[i + 1][j].getColor())) {
                        couples++;
                        verticals.add(new Pair<>(i+1,j));
                    }
                }
                if(couples == 6){
                    completed = 1;
                }
            }
        }
        if(completed == 1){
            return true;
        }else{
            return false;
        }
    };
}
