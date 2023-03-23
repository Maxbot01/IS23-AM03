package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import java.util.*;
public class TwoOf5DiffGoalStrategy implements CommonGoalStrategy{
    public boolean goalCompleted(BoardCard[][] Mat){
/* si può fare come twoofsix, in maniera più leggibile e semplice */

/* Ho creato un set a partire dall'enum e per iterarci con un indice l'ho resa una lista */
        ArrayList<colorType> colors = new ArrayList<>();
        int completed = 0;
        int correctLines = 0;

        for(int i = 0; i < Mat.length && completed == 0; i++){
            int valid = 1;
            colors.removeAll(colors);
            for(int j = 0; j < Mat[0].length; j++){
                if(Mat[i][j] != null){
                    if(!colors.contains(Mat[i][j].getColor())){
                        colors.add(Mat[i][j].getColor());
                    }else{
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
