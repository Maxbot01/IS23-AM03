package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;

public interface CommonGoalStrategy {
    public boolean calculateGoalPoints(BoardCard[][] Mat);
}
