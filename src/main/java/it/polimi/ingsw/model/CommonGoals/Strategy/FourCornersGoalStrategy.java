package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

/**
 * Strategy of fourCorners: checks if the four corners of the players' shelf are of the same color (it cannot be empty)
 */
public class FourCornersGoalStrategy implements CommonGoalStrategy{
    /**
     * Algorithm of FourCorners
     * @param Mat
     * @return boolean
     */
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
    public String toStringCommonGoal() {
        return "FourCornersGoalStrategy";
    }
    public int getIndex(){
        return 8;
    }
}
