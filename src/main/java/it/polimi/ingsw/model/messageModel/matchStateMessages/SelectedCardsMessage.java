package it.polimi.ingsw.model.messageModel.matchStateMessages;

import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.matchStateMessages.MatchStateMessage;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;
import java.util.Map;

public class SelectedCardsMessage extends MatchStateMessage {

    private ArrayList<BoardCard> selectedCards;
    private Boolean[][] selectables;
    private BoardCard[][] pieces;
    private Player currentPlayer;

    public SelectedCardsMessage(GameStateType gameState, String matchID, ArrayList<BoardCard> selectedCards, Boolean[][] selectables, BoardCard[][] pieces, Player currentPlayer) {
        super(gameState, matchID);
        this.selectedCards = selectedCards;
        this.selectables = selectables;
        this. pieces = pieces;
        this.currentPlayer = currentPlayer;
    }

    public void printMessage() {
        System.out.println("InitStateMessage:");
        System.out.println("GameStateType: " + super.gameState);
        System.out.println("MatchID: " + super.matchID);
        System.out.println("Pieces:");
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if(pieces[i][j].getColor() != colorType.EMPTY_SPOT){
                    System.out.print(pieces[i][j].getColor() + " ");
                }
            }
            System.out.println();
        }
        System.out.println("Selecectables:");
        for (int i = 0; i < selectables.length; i++) {
            for (int j = 0; j < selectables[i].length; j++) {
                if(selectables[i][j]){
                    System.out.print(" [] ");
                }else{
                    System.out.print(" * ");
                }
            }
            System.out.println();
        }

    }
}