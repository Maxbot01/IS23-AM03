package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

public class FourCornersGoalStrategy implements CommonGoalStrategy{
    public boolean goalCompleted(BoardCard[][] Mat){
        int cols = Mat[0].length;
        int rows = Mat.length;
/* Check dei quattro angoli */
        if(Mat[0][0].getColor() != colorType.EMPTY_SPOT && Mat[0][cols-1].getColor() != colorType.EMPTY_SPOT &&
            Mat[rows-1][0].getColor() != colorType.EMPTY_SPOT && Mat[rows-1][cols-1].getColor() != colorType.EMPTY_SPOT) {
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
    }
}
