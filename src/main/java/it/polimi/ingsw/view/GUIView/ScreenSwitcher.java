package it.polimi.ingsw.view.GUIView;

import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The ScreenSwitcher class represents the main application class responsible for switching between screens.
 */
public class ScreenSwitcher extends Application {
    private static Stage primaryStage;

    // Scenes:
    static Scene loginScene;
    public static Scene gameManagerViewScene;
    public static Scene gameLobbyViewScene;
    static Scene gameViewScene;

    // Views:
    public static LoginView loginView;
    public static GameManagerView gameManagerView;
    public static GameLobbyView gameLobbyView;
    private ConnectionView connectionView;
    public static GameView gameView;

    /**
     * Returns the primary stage.
     *
     * @return the primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        // Create the ConnectionView screen
        connectionView = new ConnectionView(this);
        Scene connectionScene = new Scene(connectionView.createContent(), 700, 600);

        // Create the LoginView screen
        loginView = new LoginView(this);
        loginScene = new Scene(loginView.createContent(), 700, 600);

        // Create the GameManagerView screen
        gameManagerView = new GameManagerView(this);

        // Create the GameLobbyView screen
        gameLobbyView = new GameLobbyView(this);

        // Create the GameView screen
        gameView = new GameView(this);

        // Set the initial scene
        primaryStage.setTitle("MyShelfie");
        primaryStage.setScene(connectionScene);
        primaryStage.show();
    }

    /**
     * Creates the game manager scene.
     */
    public static void createGameManagerScene() {
        gameManagerViewScene = new Scene(gameManagerView.createContent(), 700, 600);
    }

    /**
     * Creates the game lobby view.
     *
     * @param isHost   indicates if the current player is the host
     * @param players  the list of players in the game
     * @param host     the host of the game
     */
    public static void createGameLobbyView(boolean isHost, ArrayList<String> players, String host) {
        gameLobbyViewScene = new Scene(gameLobbyView.createContent(players, host, isHost), 700, 600);
    }

    /**
     * Creates the game view.
     *
     * @param players         the list of players in the game
     * @param commonGoals     the common goals
     * @param personalGoals   the personal goals
     * @param livingRoom      the living room grid
     * @param selectables     the selectable positions
     * @param playersShelves  the players' shelves
     * @param playersPoints   the players' points
     * @param gameState       the game state
     */
    public static void createGameView(List<String> players, CommonGoals commonGoals, HashMap<String, PersonalGoal> personalGoals, BoardCard[][] livingRoom, Boolean[][] selectables, ArrayList<Pair<String, BoardCard[][]>> playersShelves, HashMap<String, Integer> playersPoints, GameStateType gameState) {
        gameViewScene = new Scene(gameView.createContent(players, commonGoals, personalGoals, livingRoom, selectables, playersShelves, playersPoints, gameState), 700, 600);
    }

    /**
     * Shows the login view.
     */
    public static void showLoginView() {
        Platform.runLater(() -> primaryStage.setScene(loginScene));
    }

    /**
     * Shows the game manager view.
     *
     * @param availableGames the available games
     */
    public static void showGameManagerView(HashMap<String, List<String>> availableGames) {
        Platform.runLater(() -> {
            gameManagerView.setGameList(availableGames);
            primaryStage.setScene(gameManagerViewScene);
        });
    }

    /**
     * Shows the game lobby view.
     */
    public static void showGameLobbyView() {
        Platform.runLater(() -> primaryStage.setScene(gameLobbyViewScene));
    }

    /**
     * Shows the game view.
     */
    public static void showGameView() {
        Platform.runLater(() -> primaryStage.setScene(gameViewScene));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
