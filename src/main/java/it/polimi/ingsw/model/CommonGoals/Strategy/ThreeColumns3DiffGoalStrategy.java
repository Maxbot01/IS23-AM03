package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;

/**
 * Strategy of ThreeColumns3Diff: it looks for 3 full columns with a maximum of 3 different colors inside every single column
 */
public class ThreeColumns3DiffGoalStrategy implements CommonGoalStrategy{
    /**
     * Algorithm of ThreeColumns3Diff
     * @param Mat
     * @return boolean
     */
    public boolean goalCompleted(BoardCard[][] Mat){
        ArrayList<colorType> different = new ArrayList<>();
        int completed = 0;
        int correctLines = 0;


        for(int j = 0; j < Mat[0].length && completed == 0; j++){
            different.removeAll(different);
            int valid = 1;
            for(int i = 0; i < Mat.length && valid == 1; i++){
                if(Mat[i][j].getColor() != colorType.EMPTY_SPOT) {
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
                if(correctLines == 3){
                    completed = 1;
                }
            }
        }
        if(completed == 1){
            return true;
        }else{
            return false;
        }

    }
    public String toStringCommonGoal(){
        return "ThreeColumns3DiffGoalStrategy";
    }
    public int getIndex(){
        return 5;
    }
}
