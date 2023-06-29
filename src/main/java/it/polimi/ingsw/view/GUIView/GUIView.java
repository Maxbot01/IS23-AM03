package it.polimi.ingsw.view.GUIView;

import it.polimi.ingsw.controller.client.ClientManager;
import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUIView extends View {

    private GameStateType gameState;
    private String gameID;
    private BoardCard[][] livingRoom;
    private Boolean[][] selectables;
    private CommonGoals commonGoals;
    private PersonalGoal personalGoal;
    private HashMap<String,List<String>> availableGames; //different from CLI  because it's hashmap not arraylist
    private Player userPlayer; // Remember that userPlayer does not have a personal goal or points
    private  String host;
    private String playingPlayer;
    private ArrayList<BoardCard> selectedCards;
    public ArrayList<Pair<String,Pair<String,String>>> chatMessages;
    private boolean firstInitialization = true;
    private Pair<String, BoardCard[][]> updatedPlayerShelf;


    /**
     * Overrides the method to initialize the game.
     * Creates the game view, sets the game state, and displays the game view.
     *
     * @param players        the list of player names
     * @param commonGoals    the common goals
     * @param personalGoals  the personal goals
     * @param livingRoom     the living room cards
     * @param selectables    the selectability of living room cards
     * @param playersShelves the players' shelves
     * @param playersPoints  the players' points
     * @param gameState      the game state type
     */
    @Override
    public void initializeGame(List<String> players, CommonGoals commonGoals, HashMap<String, PersonalGoal> personalGoals,
                               BoardCard[][] livingRoom, Boolean[][] selectables, ArrayList<Pair<String, BoardCard[][]>> playersShelves,
                               HashMap<String, Integer> playersPoints, GameStateType gameState) {
        ScreenSwitcher.createGameView(players, commonGoals, personalGoals, livingRoom, selectables, playersShelves, playersPoints, gameState);
        ScreenSwitcher.gameView.setGameState(gameState.toString());
        ScreenSwitcher.showGameView();
        // MAX, non c'era niente
    }

    /**
     * Overrides the method to update the match after selected cards.
     * Sets the living room cards, selectability, and game state in the game view.
     *
     * @param livingRoom  the updated living room cards
     * @param selectables the updated selectability of living room cards
     * @param gameState   the updated game state type
     */
    @Override
    public void updateMatchAfterSelectedCards(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState) {
        // MAX
        System.err.println("updateMatchAfterSelectedCards");
        ScreenSwitcher.gameView.setLivingRoom(livingRoom);
        ScreenSwitcher.gameView.setGameState(gameState.toString());
        ScreenSwitcher.gameView.setSelectables(selectables);
    }

    /**
     * Overrides the method to update the match after selecting a column.
     * Sets the player's shelf, living room cards, selectability, player points, and game state in the game view.
     *
     * @param livingRoom          the updated living room cards
     * @param selectables         the updated selectability of living room cards
     * @param gameState           the updated game state type
     * @param updatedPlayerPoints the updated player's points
     * @param updatedPlayerShelf  the updated player's shelf
     */
    @Override
    public void updateMatchAfterSelectedColumn(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState,
                                               Pair<String, Integer> updatedPlayerPoints, Pair<String, BoardCard[][]> updatedPlayerShelf) {
        // MAX
        ScreenSwitcher.gameView.setShelf(updatedPlayerShelf);
        ScreenSwitcher.gameView.setGameState(gameState.toString());
        ScreenSwitcher.gameView.setLivingRoom(livingRoom);
        ScreenSwitcher.gameView.setSelectables(selectables);
        ScreenSwitcher.gameView.setUpdatedPlayerPoints(updatedPlayerPoints);
        this.updatedPlayerShelf = updatedPlayerShelf;
    }

    @Override
    public void gameCommands() {}

    /**
     * Overrides the method to update the playing player.
     * Sets the playing player and chaired player in the game view.
     *
     * @param chairedPlayer the name of the chaired player
     */
    @Override
    public void updatePlayingPlayer(String chairedPlayer) {
        if (firstInitialization) {
            ScreenSwitcher.gameView.setChairedPlayer(chairedPlayer);
            ScreenSwitcher.gameView.setPlayingPlayer(chairedPlayer);
            firstInitialization = false;
        } else {
            ScreenSwitcher.gameView.setPlayingPlayer(chairedPlayer);
        }
    }

    /**
     * Overrides the method to request credentials.
     * Displays the login view.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void requestCredentials() throws IOException {
        System.out.println("requestedCred");
        ScreenSwitcher.showLoginView();
    }

    /**
     * Overrides the method to launch the game manager.
     * Creates the game manager scene and displays the game manager view.
     *
     * @param availableGames the available games
     */
    @Override
    public void launchGameManager(HashMap<String, List<String>> availableGames) {
        ScreenSwitcher.createGameManagerScene();
        ScreenSwitcher.showGameManagerView(availableGames);
    }

    /**
     * Overrides the method to add a new game.
     * Adds a new game to the game list in the game manager view.
     *
     * @param newGame the new game to be added
     */
    @Override
    public void addNewGame(Pair<String, List<String>> newGame) {
        ScreenSwitcher.gameManagerView.addNewGameToList(newGame);
    }

    /**
     * Overrides the method to launch the game lobby.
     * Creates the game lobby view and displays the game lobby.
     *
     * @param gameId  the game ID
     * @param players the list of players in the game lobby
     * @param host    the host of the game
     */
    @Override
    public void launchGameLobby(String gameId, ArrayList<String> players, String host) {
        System.out.println("gameLobby");
        boolean isHost = host.equals(ClientManager.userNickname);
        ScreenSwitcher.createGameLobbyView(isHost, players, host);
        ScreenSwitcher.showGameLobbyView();
    }

    /**
     * Overrides the method to add a new player to the game lobby.
     * Adds a new player to the player list in the game lobby view.
     *
     * @param addedPlayer the new player to be added
     */
    @Override
    public void addNewLobbyPlayer(String addedPlayer) {
        ScreenSwitcher.gameLobbyView.addNewPlayerToLobby(addedPlayer);
    }

    @Override
    public void chooseCards() {}

    /**
     * Overrides the method to choose a column.
     * Orders the selected column in the game view.
     */
    @Override
    public void chooseColumn() {
        ScreenSwitcher.gameView.orderSelected();
    }


    @Override
    public void endCommands() {}

    @Override
    public void printLivingRoom() {}

    @Override
    public void printShelves() {}

    /**
     * Overrides the method to show an error message.
     * Determines the current scene and calls the appropriate showError method.
     *
     * @param error the error message to be displayed
     */
    @Override
    public void showErrorMessage(String error) {
        // Check the current scene and call the appropriate showError method
        if (ScreenSwitcher.getPrimaryStage().getScene().equals(ScreenSwitcher.gameManagerViewScene)) {
            ScreenSwitcher.gameManagerView.showError(error);
        } else if (ScreenSwitcher.getPrimaryStage().getScene().equals(ScreenSwitcher.gameLobbyViewScene)) {
            ScreenSwitcher.gameLobbyView.showError(error);
        } else if (ScreenSwitcher.getPrimaryStage().getScene().equals(ScreenSwitcher.loginScene)) {
            ScreenSwitcher.loginView.showError(error);
        }
        // We don't need it for the gameView because it's implemented inside gameView
    }

    /**
     * Overrides the method to print the score board.
     * Sets the game state, updates the player points, disables all buttons, and sets the final score board.
     *
     * @param finalScoreBoard the list of pairs containing player names and their scores
     * @param winner          the name of the winner
     * @param gameState       the game state type
     */
    @Override
    public void printScoreBoard(ArrayList<Pair<String, Integer>> finalScoreBoard, String winner, GameStateType gameState) {
        ScreenSwitcher.gameView.setGameState(gameState.toString());
        for (Pair<String, Integer> player : finalScoreBoard) {
            ScreenSwitcher.gameView.setUpdatedPlayerPoints(player);
            ScreenSwitcher.gameView.disableAllButtons();
        }
        ScreenSwitcher.gameView.disableAllButtons();
        ScreenSwitcher.gameView.setFinalScoreBoard(finalScoreBoard);
    }

    /**
     * Overrides the method to print the chat messages.
     * Updates the chat messages and calls the appropriate updateChatMessages method based on the current scene.
     *
     * @param chat the list of chat messages containing sender, receiver, and message content
     */
    @Override
    public void printChat(ArrayList<Pair<String, Pair<String, String>>> chat) {
        this.chatMessages = chat;
        // TODO: See if there's a better way of checking
        if (ScreenSwitcher.getPrimaryStage().getScene().equals(ScreenSwitcher.gameLobbyViewScene)) {
            ScreenSwitcher.gameLobbyView.updateChatMessages(chat);
        } else {
            ScreenSwitcher.gameView.updateChatMessages(chat);
        }
    }

}
