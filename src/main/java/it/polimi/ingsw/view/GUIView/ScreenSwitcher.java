package it.polimi.ingsw.view.GUIView;
import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;


public class ScreenSwitcher extends Application {
    private static Stage primaryStage;
    private static Scene gameManagerViewScene;
    private static Scene gameLobbyViewScene;

    private static Scene gameScene;


    private static Scene loginScene;

    static GameManagerView gameManagerView;
    static GameLobbyView gameLobbyView;

    static GameView gameView;
    LoginView loginView;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //create games to display from function call
        // Create the GameManagerView screen
        gameManagerView = new GameManagerView(this);
        gameManagerViewScene = new Scene(gameManagerView.createContent(), 700, 600);

        // Create the GameLobbyView screen
        gameLobbyView = new GameLobbyView(this);
        //gameLobbyViewScene = new Scene(gameLobbyView.createContent(null), 700, 600);

        loginView = new LoginView(this);
        loginScene = new Scene(loginView.createContent(), 700, 600);

        gameView = new GameView(this);

        // Set the initial scene
        primaryStage.setTitle("MyShelfie");
        primaryStage.setScene(gameManagerViewScene);
        primaryStage.show();
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 1234);
                ClientMain client = new ClientMain(socket, false);
                client.run();
                client.stop();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void showLoginView() {
        Platform.runLater(() -> primaryStage.setScene(loginScene));
    }

    public static void showGameManagerView(HashMap<String, List<String>> availableGames) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameManagerView.setGameList(availableGames);
                primaryStage.setScene(gameManagerViewScene);
            }
        });

    }

    public static void showGameLobbyView(List<String> users, String host, boolean isHost) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameLobbyViewScene = gameLobbyView.createContent(users, host, isHost);
                primaryStage.setScene(gameLobbyViewScene);
            }
        });

    }

    public static void showGameScene() {
        Platform.runLater(() -> {

            //gameScene = new Scene(gameScene.createContent(), 900, 800);
            primaryStage.setScene(gameScene);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
