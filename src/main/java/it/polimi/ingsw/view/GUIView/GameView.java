package it.polimi.ingsw.view.GUIView;

import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GameView {
    private final ScreenSwitcher screenSwitcher;
    @FXML
    ImageView boardimage;

    /*public Scene createContent(List<String> players, CommonGoals commonGoals, HashMap<String, PersonalGoal> personalGoals,
                               BoardCard[][] livingRoom, Boolean[][] selectables, ArrayList<Pair<String,BoardCard[][]>> playersShelves,
                               HashMap<String, Integer> playersPoints, GameStateType gameState) {
        // Create the root container
        BorderPane root = new BorderPane();

        // Create the players' shelves
        VBox shelvesContainer = new VBox();
        for (Pair<String, BoardCard[][]> playerShelf : playersShelves) {
            String playerName = playerShelf.getFirst();
            BoardCard[][] playerShelfCards = playerShelf.getSecond();
            HBox shelfRow = new HBox();
            shelfRow.setAlignment(Pos.CENTER);

            // Create labels for player names
            Label playerNameLabel = new Label(playerName);
            playerNameLabel.setStyle("-fx-font-weight: bold;");

            // Create the player's shelf grid
            GridPane shelfGrid = new GridPane();
            shelfGrid.setAlignment(Pos.CENTER);
            shelfGrid.setHgap(10);
            shelfGrid.setVgap(10);
            for (int i = 0; i < playerShelfCards.length; i++) {
                for (int j = 0; j < playerShelfCards[i].length; j++) {
                    BoardCard card = playerShelfCards[i][j];
                    ImageView cardImageView = new ImageView(new Image("imgs/boards/Cornici1.1.png"));
                    shelfGrid.add(cardImageView, j, i);
                }
            }

            shelfRow.getChildren().addAll(playerNameLabel, shelfGrid);
            shelvesContainer.getChildren().add(shelfRow);
        }

        // Create the living room grid
        GridPane livingRoomGrid = new GridPane();
        livingRoomGrid.setAlignment(Pos.CENTER);
        livingRoomGrid.setHgap(10);
        livingRoomGrid.setVgap(10);
        for (int i = 0; i < livingRoom.length; i++) {
            for (int j = 0; j < livingRoom[i].length; j++) {
                BoardCard card = livingRoom[i][j];
                ImageView cardImageView = new ImageView(new Image("imgs/boards/Cornici1.1.png"));
                livingRoomGrid.add(cardImageView, j, i);
            }
        }

        // Create the common goals list view
        ListView<String> commonGoalsListView = new ListView<>();
        commonGoalsListView.getItems().addAll((Collection<? extends String>) commonGoals.getFirstGoal());
        commonGoalsListView.setPrefHeight(200);

        // Create the personal goals list view
        ListView<String> personalGoalsListView = new ListView<>();
        //personalGoalsListView.getItems().addAll((String) personalGoals.values().stream().map(PersonalGoal::getSelectedGoal).collect(Collectors.toList()));
        personalGoalsListView.setPrefHeight(200);

        // Create the players' points labels
        VBox pointsContainer = new VBox();
        for (String playerName : players) {
            Label playerPointsLabel = new Label(playerName + ": " + playersPoints.getOrDefault(playerName, 0));
            pointsContainer.getChildren().add(playerPointsLabel);
        }

        // Create the game state label
        Label gameStateLabel = new Label("Game State: " + gameState.toString());
        gameStateLabel.setStyle("-fx-font-weight: bold;");

        // Add all components to the root container
        root.setTop(shelvesContainer);
        root.setCenter(livingRoomGrid);
        root.setRight(commonGoalsListView);
        root.setLeft(personalGoalsListView);
        root.setBottom(pointsContainer);
        root.setBottom(gameStateLabel);

        return new Scene(root, 700, 600);
    }*/


    public GameView(ScreenSwitcher screenSwitcher){
        this.screenSwitcher = screenSwitcher;
    }

}
