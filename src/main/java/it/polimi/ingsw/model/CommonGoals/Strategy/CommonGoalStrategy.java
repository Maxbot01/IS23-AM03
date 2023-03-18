package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;

public interface CommonGoalStrategy {
    public int calculateGoalPoints(BoardCard[][] Mat, Integer numOfPlayers);
}
