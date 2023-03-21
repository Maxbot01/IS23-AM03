package it.polimi.ingsw.model.messageModel.matchStateMessages;

import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public void printMessage() {
        System.out.println("InitStateMessage:");
        System.out.println("GameStateType: " + super.gameState);
        System.out.println("MatchID: " + super.gameState);
        System.out.println("Pieces:");
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if(pieces[i][j] != null){
                    System.out.print(pieces[i][j].getColor() + " ");
                }
            }
            System.out.println();
        }
        System.out.println("Selecectables:");
        for (int i = 0; i < selecectables.length; i++) {
            for (int j = 0; j < selecectables[i].length; j++) {
                System.out.print(selecectables[i][j] + " ");
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
                    System.out.print(pair.getSecond()[i][j] + " ");
                }
                System.out.println();
            }
        }
    }



}
