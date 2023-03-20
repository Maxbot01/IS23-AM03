package it.polimi.ingsw.model.messageModel.matchStateMessages;

import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.matchStateMessages.MatchStateMessage;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.CommonGoals;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.enums.PersonalGoalType;

import java.util.ArrayList;
import java.util.HashMap;

public class InitStateMessage extends MatchStateMessage {

    private BoardCard[][] pieces;
    private Boolean[][] selecectables;
    private CommonGoals commonGoals;
    private HashMap<Player, PersonalGoal> personalGoals;
    private ArrayList<Player> players;
    private Player chairedPlayer;

    private ArrayList<Pair<Player, BoardCard[][]>> playersShelves;

    public InitStateMessage(GameStateType gameState, String matchID, BoardCard[][] pieces, Boolean[][] selecectables, CommonGoals commonGoals, HashMap<Player, PersonalGoal> personalGoals, ArrayList<Player> players, Player chairedPlayer, ArrayList<Pair<Player, BoardCard[][]>> playersShelves){
        super(gameState, matchID);
        this.pieces = pieces;
        this.selecectables = selecectables;
        this.commonGoals = commonGoals;
        this.personalGoals = personalGoals;
        this.players = players;
        this.chairedPlayer = chairedPlayer;
        this.playersShelves = playersShelves;
    }
}
