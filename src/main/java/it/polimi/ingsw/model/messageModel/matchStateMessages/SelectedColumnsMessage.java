package it.polimi.ingsw.model.messageModel.matchStateMessages;

import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.Player;

import java.util.Arrays;

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

    public void printMessage() {
        System.out.println("Updated Points: " + updatedPoints.getFirst() + " " + updatedPoints.getSecond());
        System.out.println("New Player: " + newPlayer);
        System.out.println("Pieces: " + Arrays.deepToString(pieces));
        System.out.println("Selectables: " + Arrays.deepToString(selectables));
        System.out.println("PlayersShelves:");
        System.out.println(updatedPlayerShelf.getFirst() + ":");
        for (int i = 0; i < updatedPlayerShelf.getSecond().length; i++) {
            for (int j = 0; j < updatedPlayerShelf.getSecond()[i].length; j++) {
                if(updatedPlayerShelf.getSecond()[i][j] != null){
                    System.out.print(updatedPlayerShelf.getSecond()[i][j].getColor() + " ");
                }else{
                    System.out.print("    []    ");
                }
            }
            System.out.println();
        }
        System.out.println("Game State: " + gameState);
        System.out.println("Match ID: " + matchID);
    }


}
