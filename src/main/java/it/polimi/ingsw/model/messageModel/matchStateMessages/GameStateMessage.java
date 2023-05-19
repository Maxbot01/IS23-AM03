package it.polimi.ingsw.model.messageModel.matchStateMessages;

import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class GameStateMessage extends MatchStateMessage{

    public BoardCard[][] pieces;
    public Boolean[][] selecectables;

    public BoardCard[] currentlySelected;
    public String firstGoal;
    public String secondGoal;
    public HashMap<String, PersonalGoal> personalGoals;
    public ArrayList<String> players;
    public String chairedPlayer;

    private ArrayList<Pair<String, BoardCard[][]>> playersShelves;

    public GameStateMessage(GameStateType gameState, String matchID, BoardCard[][] pieces, Boolean[][] selecectables, BoardCard[] currentlySelected, CommonGoals commonGoals, HashMap<String, PersonalGoal> personalGoals, ArrayList<String> players, String chairedPlayer, ArrayList<Pair<String, BoardCard[][]>> playersShelves){
        super(gameState, matchID);
        this.pieces = pieces;
        this.selecectables = selecectables;
        this.currentlySelected = currentlySelected;
        this.firstGoal = commonGoals.getFirstGoal().toString();
        this.secondGoal = commonGoals.getSecondGoal().toString();
        this.personalGoals = personalGoals;
        this.players = players;
        this.chairedPlayer = chairedPlayer;
        this.playersShelves = playersShelves;
    }
}
