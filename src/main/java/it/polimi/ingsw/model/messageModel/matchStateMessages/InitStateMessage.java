package it.polimi.ingsw.model.messageModel.matchStateMessages;

import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitStateMessage extends MatchStateMessage {

    public BoardCard[][] pieces;
    public Boolean[][] selecectables;
    public CommonGoals commonGoals;
    public HashMap<String, PersonalGoal> personalGoals;
    public List<String> players;
    public String chairedPlayer;
    public HashMap<String, Integer> playersPoints; //TODO: questo non serve


    public ArrayList<Pair<String, BoardCard[][]>> playersShelves;

    public InitStateMessage(GameStateType gameState, String matchID, BoardCard[][] pieces, Boolean[][] selecectables, CommonGoals commonGoals,
                            HashMap<String, PersonalGoal> personalGoals, List<String> players, String chairedPlayer, ArrayList<Pair<String, BoardCard[][]>>
                            playersShelves){
        super(gameState, matchID);
        this.gameState = gameState;
        this.matchID = matchID;
        this.pieces = pieces;
        this.selecectables = selecectables;
        this.commonGoals = commonGoals;
        this.personalGoals = personalGoals;
        this.players = players;
        this.chairedPlayer = chairedPlayer;
        this.playersShelves = playersShelves;
    }
/*
    public void printMessage() {
        System.out.println("InitStateMessage:");
        System.out.println("GameStateType: " + super.gameState);
        System.out.println("MatchID: " + super.gameState);
        System.out.println("Pieces:");
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if(pieces[i][j].getColor() != colorType.EMPTY_SPOT){
                    System.out.print(pieces[i][j].getColor() + " ");
                }else{
                    System.out.print(" null ");
                }
            }
            System.out.println();
        }
        System.out.println("Selecectables:");
        for (int i = 0; i < selecectables.length; i++) {
            for (int j = 0; j < selecectables[i].length; j++) {
                if(selecectables[i][j]){
                    System.out.print("[] ");
                }else{
                    System.out.print(" * ");
                }
            }
            System.out.println();
        }
        System.out.println("CommonGoals 1: " + commonGoals.getFirstGoal().toString());
        System.out.println("CommonGoals 2: " + commonGoals.getSecondGoal().toString());
        System.out.println("PersonalGoals:");
        for (Map.Entry<Player, PersonalGoal> entry : personalGoals.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("Players: " + players);
        System.out.println("ChairedPlayer: " + chairedPlayer);
        System.out.println("PlayersShelves:");
        for (Pair<Player, BoardCard[][]> pair : playersShelves) {
            System.out.println(pair.getFirst().getNickname() + ":");
            for (int i = 0; i < pair.getSecond().length; i++) {
                for (int j = 0; j < pair.getSecond()[i].length; j++) {
                    System.out.print(pair.getSecond()[i][j].getColor() + " ");
                }
                System.out.println();
            }
        }
    }
*/


}
