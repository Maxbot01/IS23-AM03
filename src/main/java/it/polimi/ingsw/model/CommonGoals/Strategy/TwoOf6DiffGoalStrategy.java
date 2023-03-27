package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;

public class TwoOf6DiffGoalStrategy implements CommonGoalStrategy{
    public boolean goalCompleted(BoardCard[][] Mat){
        ArrayList<colorType> colors = new ArrayList<>();
        int completed = 0;
        int correctLines = 0;

        for(int j = 0; j < Mat[0].length && completed == 0; j++){
            int valid = 1;
            colors.removeAll(colors);
            for(int i = 0; i < Mat.length && valid == 1; i++){
                if(Mat[i][j].getColor() != colorType.EMPTY_SPOT) {
                    if (!colors.contains(Mat[i][j].getColor())) {
                        colors.add(Mat[i][j].getColor());
                    } else {
                        valid = 0;
                    }
                }else{
                    valid = 0;
                }
            }
            if(valid == 1){
                correctLines++;
                if(correctLines == 2){
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
