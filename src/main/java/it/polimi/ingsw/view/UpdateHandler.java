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
     * It initializes the game's main parameters, it is also used in case of disconnection
     * @param players
     * @param commonGoals
     * @param personalGoals
     * @param livingRoom
     * @param selectables
     * @param playersShelves
     * @param playersPoints
     * @param gameState
     */
    void initializeGame(List<String> players, CommonGoals commonGoals, HashMap<String, PersonalGoal> personalGoals,
                           BoardCard[][] livingRoom, Boolean[][] selectables, ArrayList<Pair<String,BoardCard[][]>> playersShelves,
                           HashMap<String, Integer> playersPoints, GameStateType gameState);
    /**
     * It contains all the available command the CLI user can call while waiting for his turn
     */
    void updateMatchAfterSelectedCards(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState);
    void updateMatchAfterSelectedColumn(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState, Pair<String, Integer>
                                        updatedPlayerPoints, Pair<String, BoardCard[][]> updatedPlayerShelf);

    //void waitingCommands(); // The controller continues to call this method until the playing player changes to the CLI user
    /**
     * Requests Username from Command Line
     * @return String
     */
    void requestCredentials();
    /**
     * Calls the command section of GameManager, enabling the user to write which command to execute
     * @param availableGames
     */
    void launchGameManager(HashMap<String, List<String>> availableGames);
    void addNewGame(Pair<String, List<String>> newGame);
    void addNewLobbyPlayer(String addedPlayer);
    void gameCommands();
    void updateChairedPlayer(String chairedPlayer);
    /**
     * Calls the command section of GameLobby, enabling the user to write which command to execute.
     * It is sent only to the
     */
    void launchGameLobby(String gameId, ArrayList<String> players, String host); // The check of whether the user is the host is done inside the method, there's no need to give info
    /**
     * Calls the game sequence where the CLI user chooses the cards from the living room
     */
    void chooseCards();
    /**
     * Calls the game sequence where the CLI user chooses the column of his shelf
     */
    void chooseColumn();
    /**
     * Calls the commands available after a match has ended
     */
    void endCommands();
    /**
     * Prints the livingRoom/Board
     */
    void printLivingRoom();
    /**
     * Prints all the shelves
     */
    void printShelves();
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
    /**
     * Prints the final scoreboard
     * @param finalScoreBoard
     * @param winner
     */
    void printScoreBoard(ArrayList<Pair<String, Integer>> finalScoreBoard, String winner,GameStateType gameState);

    //void readInput() throws InterruptedException; CLIInputThread
}
