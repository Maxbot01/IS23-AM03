package it.polimi.ingsw.model.messageModel.matchStateMessages;

import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.messageModel.matchStateMessages.MatchStateMessage;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.Player;

import java.util.ArrayList;

public class SelectedCardsMessage extends MatchStateMessage {

    private ArrayList<BoardCard> selectedCards;
    private boolean[][] selectables;
    private BoardCard[][] pieces;
    private Player currentPlayer;

    SelectedCardsMessage(GameStateType gameState, String matchID, ArrayList<BoardCard> selectedCards, boolean[][] selectables, BoardCard[][] pieces, Player currentPlayer) {
        super(gameState, matchID);
        this.selectedCards = selectedCards;
        this.selectables = selectables;
        this. pieces = pieces;
        this.currentPlayer = currentPlayer;
    }
}
