package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;

public class FourOfFourGoalStrategy implements CommonGoalStrategy{
    public boolean calculateGoalPoints(BoardCard[][] Mat){
        int complete = 0;

        if(complete == 1){
            return true;
        }else{
            return false;
        }
    };
}
