package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameLobby;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;

import java.util.List;

public interface UpdateHandler {
    void updatedLivingRoom(BoardCard[][] cards);

    String requestUsername();

    String requestPassword();

    void launchGameManager(List<GameLobby> availableGames);

    void launchGameLobby(); //It's sent only if the cli user is the host
    void startGameSequence() throws UnselectableCardException;
}
