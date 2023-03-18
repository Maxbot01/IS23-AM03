package it.polimi.ingsw.model.messageModel.matchStateMessages;

import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.messageModel.matchStateMessages.MatchStateMessage;

public class InitStateMessage extends MatchStateMessage {

    private BoardCard[][] pieces;
    private boolean[][] selecectables;
    private CommonGoals commonGoals;
    private hashMap<Player, PersonalGoalType> personalGoals;
    private List<Player> players;
    private Player chairedPlayer;


    public InitStateMessage(GameStateType gameState, String matchID, BoardCard[][] pieces, boolean[][] selecectables, CommonGoals commonGoals, hashMap<Player, PersonalGoalType> personalGoals, List<Player> players, Player chairedPlayer){
        super(gameState, matchID);
        this.pieces = pieces;
        this.selecectables = selecectables;
        this.commonGoals = commonGoals;
        this.personalGoals = personalGoals;
        this.players = players;
        this.chairedPlayer = chairedPlayer;
    }
}
