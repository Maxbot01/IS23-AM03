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
import java.util.HashMap;
import java.util.List;

public interface UpdateHandler {
    /**
     * It initializes the game main parameters
     * @param players
     * @param commonGoals
     * @param personalGoals
     */
    void initializeGame(List<String> players, CommonGoals commonGoals, HashMap<String, PersonalGoal> personalGoals);
    /**
     * It contains all the available command the CLI user can call while waiting for his turn
     */
    void waitingCommands(); // The controller continues to call this method until the playing player changes to the CLI user
    /**
     * Updates the CLI parameters for the match
     * @param livingRoom
     * @param selectables
     * @param playersShelves
     */
    void updatedMatchDetails(BoardCard[][] livingRoom, Boolean[][] selectables, ArrayList<Pair<String,BoardCard[][]>> playersShelves,
                             GameStateType gameState, HashMap<String,Integer> playersPoints);
    /**
     * Requests Username from Command Line
     * @return String
     */
    void requestCredentials();
    /**
     * Calls the command section of GameManager, enabling the user to write which command to execute
     * @param availableGames
     */
    void launchGameManager(List<GameLobby> availableGames);
    /**
     * Calls the command section of GameLobby, enabling the user to write which command to execute.
     * It is sent only to the
     */
    void launchGameLobby(String gameId, ArrayList<String> players, String host); // The check of whether the user is the host is done inside the method, there's no need to give info
    /**
     * Calls the game sequence where the CLI user chooses the cards from the living room
     * @throws UnselectableCardException
     */
    void chooseCards();

    /**
     * Calls the game sequence where the CLI user chooses the column of his shelf
     */
    void chooseColumn();
    /**
     * It prints the living room and shelves on the terminal
     */
    void printLivingRoomAndShelves();
    /**
     * Prints error type on command line
     * @param error
     */
    void showErrorMessage(String error);

    /**
     * Prints the playing player
     * @param playingPlayer
     */
    void showPlayingPlayer(String playingPlayer);
}
