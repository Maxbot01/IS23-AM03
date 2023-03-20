package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;

public class FourCornersGoalStrategy implements CommonGoalStrategy{
    public boolean calculateGoalPoints(BoardCard[][] Mat){
        int cols = Mat[0].length;
        int rows = Mat.length;
/* Check dei quattro angoli */
        if(Mat[0][0] != null && Mat[0][cols-1] != null && Mat[rows-1][0] != null &&
                Mat[rows-1][cols-1] != null) {
            if (Mat[0][0].getColor().equals(Mat[0][cols - 1].getColor()) &&
                    Mat[0][0].getColor().equals(Mat[rows - 1][0].getColor()) &&
                    Mat[0][0].getColor().equals(Mat[rows - 1][cols - 1].getColor())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    };
}
