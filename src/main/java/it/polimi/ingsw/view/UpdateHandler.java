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

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface UpdateHandler {
    /**
     * It initializes the game parameters at the start of the match or, in case of reconnection it sets them based on the last changes
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
                           HashMap<String, Integer> playersPoints, GameStateType gameState) throws IOException;
    /**
     * It updates the game parameters relative to the selection of cards from the living room by the playing player
     * @param livingRoom
     * @param selectables
     * @param gameState
     */
    void updateMatchAfterSelectedCards(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState);

    /**
     * It updates the game parameters relative to the selection of the column by the playing player
     * @param livingRoom
     * @param selectables
     * @param gameState
     * @param updatedPlayerPoints
     * @param updatedPlayerShelf
     */
    void updateMatchAfterSelectedColumn(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState, Pair<String, Integer>
                                        updatedPlayerPoints, Pair<String, BoardCard[][]> updatedPlayerShelf);

    /**
     * Method relative to the acquisition of the client's credentials
     */
    void requestCredentials();
    /**
     * Method relative to the activation of the available commands that the player has during the game selection phase
     * @param availableGames
     */
    void launchGameManager(HashMap<String, List<String>> availableGames);

    /**
     * Whenever a new game lobby is created it updates the available games
     * @param newGame
     */
    void addNewGame(Pair<String, List<String>> newGame);

    /**
     * Whenever a new player enters the lobby it updates the lobby's players
     * @param addedPlayer
     */
    void addNewLobbyPlayer(String addedPlayer);

    /**
     * Method relative to the activation of the available commands that the player has during the game
     * @throws RemoteException
     */
    void gameCommands();

    /**
     * It updates the playing player
     * @param playingPlayer
     */
    void updatePlayingPlayer(String playingPlayer);

    /**
     * Method relative to the activation of the available commands that the player has during the game's lobby phase
     * @param gameId
     * @param players
     * @param host
     */
    void launchGameLobby(String gameId, ArrayList<String> players, String host); // The check of whether the user is the host is done inside the method, there's no need to give info
    /**
     * Method relative to the card and order selection phase
     */
    void chooseCards();
    /**
     * Method relative to the column selection phase
     */
    void chooseColumn();
    /**
     * Method relative to the activation of the available commands that the player has after he has left the game
     */
    void endCommands();
    /**
     * Shows the current living room / board
     */
    void printLivingRoom();
    /**
     * Shows the chat messages. It can show the full chat or the last five messages based on the player's choice
     * @param messages
     */
    void printChat(ArrayList<Pair<String, String>> messages);
    /**
     * Shows all the players' current shelves
     */
    void printShelves();
    /**
     * Shows error messages
     * @param error
     */
    void showErrorMessage(String error);
    /**
     * Shows the final scoreboard, winner and the players' shelves
     * @param finalScoreBoard
     * @param winner
     * @param gameState
     */
    void printScoreBoard(ArrayList<Pair<String, Integer>> finalScoreBoard, String winner,GameStateType gameState);
    //void readInput() throws InterruptedException; CLIInputThread
}
