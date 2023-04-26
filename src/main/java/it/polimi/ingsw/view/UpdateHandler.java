package it.polimi.ingsw.view;

import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameLobby;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.LivingRoom;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;

import java.util.ArrayList;
import java.util.List;

public interface UpdateHandler {
    /**
     * It initializes the game main parameters
     * @param players
     * @param commonGoals
     * @param personalGoal
     * @param userPlayer
     */
    void initializeGame(ArrayList<Player> players, CommonGoals commonGoals, PersonalGoal personalGoal, Player userPlayer);
    void waitingCommands(); // The controller continues to call this method until the playing player changes to the CLI user
    /**
     * Updates the CLI parameters for the match
     * @param livingRoom
     * @param selectables
     * @param playersShelves
     */
    void updatedMatchDetails(LivingRoom livingRoom, Boolean[][] selectables, ArrayList<Pair<String,BoardCard[][]>> playersShelves,
                             String gameID, GameStateType gameState);
    /**
     * Requests Username from Command Line
     * @return String
     */
    String requestUsername();
    /**
     * Requests Password from Command Line
     * @return String
     */
    String requestPassword();
    /**
     * Calls the command section of GameManager, enabling the user to write which command to execute
     * @param availableGames
     */
    void launchGameManager(List<GameLobby> availableGames);
    /**
     * Calls the command section of GameLobby, enabling the user to write which command to execute.
     * It is sent only to the
     */
    void launchGameLobby(String gameId); // The check of whether the user is the host is done inside the method, there's no need to give info
    /**
     * Calls the game sequence where the CLI user chooses the cards from the living room and places them in his shelf
     * @throws UnselectableCardException
     */
    void startGameSequence() throws UnselectableCardException;
    /**
     * It prints the living room and shelves on the terminal
     */
    void printLivingRoomAndShelves();
}
