package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;


import java.util.ArrayList;

public class FourLines3DiffGoalStrategy implements CommonGoalStrategy{
    public boolean calculateGoalPoints(BoardCard[][] Mat){

        ArrayList<colorType> different = new ArrayList<>();
        int correctLines = 0;
        int completed = 0;

        for(int i = 0; i < Mat.length && completed == 0; i++){
            int valid = 1;
            different.removeAll(different);
            for(int j = 0; j < Mat[0].length && valid == 1; j++){
                if(Mat[i][j] != null) {
                    if (!different.contains(Mat[i][j].getColor())) {
                        different.add(Mat[i][j].getColor());
                        if(different.size() > 3){
                            valid = 0;
                        }
                    }
                }else{
                    valid = 0;
                }
            }
            if(valid == 1){
                correctLines++;
                if(correctLines == 4){
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
