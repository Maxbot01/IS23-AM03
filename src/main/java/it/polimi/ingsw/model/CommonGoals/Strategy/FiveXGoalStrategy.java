package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

/**
 * Strategy of FiveX: it looks for 5 boardCards of the same color distributed in an X shape
 */
public class FiveXGoalStrategy implements CommonGoalStrategy {
    /**
     * Algorithm of FiveX
     *
     * @param Mat
     * @return boolean
     */
    public boolean goalCompleted(BoardCard[][] Mat) {
        colorType chosen;
        int found = 0;

        for (int i = 1; i < Mat.length - 1 && found == 0; i++) {
            for (int j = 1; j < Mat[0].length - 1 && found == 0; j++) {
                if (Mat[i][j].getColor() != colorType.EMPTY_SPOT && Mat[i - 1][j - 1].getColor() != colorType.EMPTY_SPOT &&
                        Mat[i - 1][j + 1].getColor() != colorType.EMPTY_SPOT && Mat[i + 1][j - 1].getColor() != colorType.EMPTY_SPOT &&
                        Mat[i + 1][j + 1].getColor() != colorType.EMPTY_SPOT) {
                    chosen = Mat[i][j].getColor();
                    if (Mat[i - 1][j - 1].getColor().equals(chosen) && Mat[i - 1][j + 1].getColor().equals(chosen) &&
                            Mat[i + 1][j - 1].getColor().equals(chosen) && Mat[i + 1][j + 1].getColor().equals(chosen)) {
                        found = 1;
                    }
                }
            }
        }
        if (found == 1) {
            return true;
        } else {
            return false;
        }
    }
        public String toStringCommonGoal () {
            return "FiveXGoalStrategy";
        }
        public int getIndex(){
            return 10;
        }
    }

