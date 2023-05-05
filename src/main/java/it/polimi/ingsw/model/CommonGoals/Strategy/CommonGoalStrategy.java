package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;

public interface CommonGoalStrategy {
    /**
     * Interface method from which to call the goals' algorithms
     * @param Mat
     * @return boolean
     */
    boolean goalCompleted(BoardCard[][] Mat);
}
