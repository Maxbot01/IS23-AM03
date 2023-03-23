package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;

public class EightTilesGoalStrategy implements CommonGoalStrategy{
    public boolean goalCompleted(BoardCard[][] Mat){
        ArrayList<colorType> colors = new ArrayList<>();
        int completed = 0;
        int [] numOfColor = {0,0,0,0,0,0};

        for(int i = 0; i < Mat.length && completed == 0; i++){
            for(int j = 0; j < Mat[0].length && completed == 0; j++) {
                if(Mat[i][j] != null){
                    if(!colors.contains(Mat[i][j].getColor())){
                        colors.add(Mat[i][j].getColor());
                    }
                    numOfColor[colors.indexOf(Mat[i][j].getColor())]++;
                    if(numOfColor[colors.indexOf(Mat[i][j].getColor())] == 8){
                        completed = 1;
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
}
