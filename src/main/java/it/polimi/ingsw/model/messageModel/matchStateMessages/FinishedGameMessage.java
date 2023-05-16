package it.polimi.ingsw.model.messageModel.matchStateMessages;

import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;

import java.util.ArrayList;

public class FinishedGameMessage extends MatchStateMessage{

    public ArrayList<Pair<String, Integer>> finalScoreBoard;
    public String winnerNickname;

    public FinishedGameMessage(GameStateType gameState, String matchID, ArrayList<Pair<String, Integer>> finalScoreBoard, String winnerNickname) {
        super(gameState, matchID);
        this.finalScoreBoard = finalScoreBoard;
        this.winnerNickname = winnerNickname;
    }
}
