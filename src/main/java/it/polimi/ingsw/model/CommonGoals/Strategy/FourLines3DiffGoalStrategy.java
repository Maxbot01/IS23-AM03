package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;

/**
 * Strategy of FourLines3Diff: it looks for 3 full lines with a maximum of 3 different colors inside every single line
 */
public class FourLines3DiffGoalStrategy implements CommonGoalStrategy{
    /**
     * Algorithm of FourLines3Diff
     * @param Mat
     * @return boolean
     */
    public boolean goalCompleted(BoardCard[][] Mat){

        ArrayList<colorType> different = new ArrayList<>();
        int correctLines = 0;
        int completed = 0;

        for(int i = 0; i < Mat.length && completed == 0; i++){
            int valid = 1;
            different.removeAll(different);
            for(int j = 0; j < Mat[0].length && valid == 1; j++){
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
    }
    public String toStringCommonGoal() {
        return "FourLines3DiffGoalStrategy";
    }
    public int getIndex(){
        return 7;
    }
}
