package it.polimi.ingsw.model.messageModel.matchStateMessages;

import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;

public class SelectedColumnsMessage extends MatchStateMessage{

    Pair<String, Integer> updatedPoints;
    String newPlayer;

    BoardCard[][] pieces;

    Boolean[][] selectables;

    Pair<String, BoardCard[][]> updatedPlayerShelf;
    public SelectedColumnsMessage(GameStateType gameState, String matchID, Pair<String, Integer> updatedPoints, String newPlayer, Pair<String, BoardCard[][]> updatedPlayerShelf, BoardCard[][] pieces, Boolean[][] selectables) {
        super(gameState, matchID);
        this.updatedPoints = updatedPoints;
        this.newPlayer = newPlayer;
        this.updatedPlayerShelf = updatedPlayerShelf;
        this.pieces = pieces;
        this.selectables = selectables;
    }
}
