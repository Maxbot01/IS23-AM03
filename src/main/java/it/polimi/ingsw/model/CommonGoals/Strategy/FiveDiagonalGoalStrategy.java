package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

/**
 * Strategy of FiveDiagonal: it looks for 5 boardCards of the same color in diagonal
 */
public class FiveDiagonalGoalStrategy implements CommonGoalStrategy{
    /**
     * Algorithm of FiveDiagonal
     * @param Mat
     * @return boolean
     */
    public boolean goalCompleted(BoardCard[][] Mat) {
        int cols = Mat[0].length;
        int rows = Mat.length;
        int valid = 0;

        //checks all four possibilities for valid not being one

        if (Mat[0][0].getColor() != colorType.EMPTY_SPOT) {
            colorType chosen = Mat[0][0].getColor();
            int found = 1;
            for (int i = 1; i < rows - 1 && found == 1; i++) {
                if (Mat[i][i].getColor() != colorType.EMPTY_SPOT) {
                    if (!Mat[i][i].getColor().equals(chosen)) {
                        found = 0;
                    }
                } else {
                    found = 0;
                }
            }
            if (found == 1) {
                valid = 1;
            }
        }
        if (Mat[1][0].getColor() != colorType.EMPTY_SPOT && valid == 0) {
            colorType chosen = Mat[1][0].getColor();
            int found = 1;
            for (int i = 2; i < rows && found == 1; i++) {
                if (Mat[i][i - 1].getColor() != colorType.EMPTY_SPOT) {
                    if (!Mat[i][i - 1].getColor().equals(chosen)) {
                        found = 0;
                    }
                } else {
                    found = 0;
                }
            }
            if (found == 1) {
                valid = 1;
            }
        }
        if (Mat[0][cols - 1].getColor() != colorType.EMPTY_SPOT && valid == 0) {
            colorType chosen = Mat[0][cols - 1].getColor();
            int found = 1;
            for (int i = 1; i < rows - 1 && found == 1; i++) {
                if (Mat[i][cols - (1 + i)].getColor() != colorType.EMPTY_SPOT) {
                    if (!Mat[i][cols - (1 + i)].getColor().equals(chosen)) {
                        found = 0;
                    }
                } else {
                    found = 0;
                }
            }
            if (found == 1) {
                valid = 1;
            }
        }
        if (Mat[1][cols - 1].getColor() != colorType.EMPTY_SPOT && valid == 0) {
            colorType chosen = Mat[1][cols - 1].getColor();
            int found = 1;
            for (int i = 2; i < rows && found == 1; i++) {
                if (Mat[i][cols - i].getColor() != colorType.EMPTY_SPOT) {
                    if (!Mat[i][cols - i].getColor().equals(chosen)) {
                        found = 0;
                    }
                } else {
                    found = 0;
                }
            }
            if (found == 1) {
                valid = 1;
            }
        }
        if (valid == 1) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Returns the name of the CommonGoal
     * @return String
     */
        public String toStringCommonGoal() {
            return "FiveDiagonalGoalStrategy";
        }
        public int getIndex(){
            return 11;
        }

}
