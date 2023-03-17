package it.polimi.ingsw.model.messageModel;

public class InitStateMessage extends MatchStateMessage{

    private BoardCard[][] pieces;
    private boolean[][] selecectables;
    private CommonGoals commonGoals;
    private hashMap<Player, PersonalGoalType> personalGoals;
    private List<Player> players;
    private Player chairedPlayer;


    public InitStateMessage(BoardCard[][] pieces, boolean[][] selecectables, CommonGoals commonGoals, hashMap<Player, PersonalGoalType> personalGoals, List<Player> players, Player chairedPlayer){
        this.pieces = pieces;
        this.selecectables = selecectables;
        this.commonGoals = commonGoals;
        this.personalGoals = personalGoals;
        this.players = players;
        this.chairedPlayer = chairedPlayer;
    }
}
