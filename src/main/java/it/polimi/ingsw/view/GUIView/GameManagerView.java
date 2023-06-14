package it.polimi.ingsw.view.GUIView;

import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.model.GameManager;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.*;

import static it.polimi.ingsw.client.ClientMain.stub;

public class GameManagerView {
    private ScreenSwitcher screenSwitcher;
    private ListView<Game> gameListView;
    private final ObservableList<Game> gameList;

    public GameManagerView(ScreenSwitcher screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
        gameList = FXCollections.observableArrayList();
        //setGameList(availableGames);
    }

    public VBox createContent() {
        VBox gameManagerView = new VBox();

        // Create the game list view
        gameListView = new ListView<>(gameList);
        gameListView.setCellFactory(createGameCellFactory());
        gameManagerView.getChildren().add(gameListView);
        gameListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Create the button container
        HBox buttonContainer = new HBox();

        // Create the "Update Games" button
        Button updateButton = new Button("Refresh games");
        updateButton.setOnAction(event -> updateGameListWithRandomData());

        // Create the "Create Game" button
        Button createButton = new Button("Create Game");
        createButton.setOnAction(event -> createNewGame());

        // Add the buttons to the button container
        buttonContainer.getChildren().addAll(updateButton, createButton);

        // Set HBox properties to align buttons at the bottom
        HBox.setHgrow(buttonContainer, Priority.ALWAYS);
        buttonContainer.setFillHeight(true);

        // Add the button container to the VBox
        gameManagerView.getChildren().add(buttonContainer);

        // Set VBox properties to expand the game list view
        VBox.setVgrow(gameListView, Priority.ALWAYS);

        // Create the root VBox to hold the gameManagerView
        VBox root = new VBox();

        // Create the label for "no current games"
        Label noGamesLabel = new Label("No current games");
        noGamesLabel.setAlignment(Pos.CENTER);
        noGamesLabel.getStyleClass().add("no-games-label");

        // Create a BooleanBinding to track if the gameList is empty
        BooleanBinding isGameListEmpty = Bindings.isEmpty(gameList);
        noGamesLabel.visibleProperty().bind(isGameListEmpty);
        noGamesLabel.managedProperty().bind(isGameListEmpty);

        // Add the label and the gameManagerView to the root VBox
        root.getChildren().addAll(noGamesLabel, gameManagerView);

        // Set VBox properties to expand the gameManagerView
        VBox.setVgrow(gameManagerView, Priority.ALWAYS);

        return root;
    }

    private void createNewGame() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Game");
        dialog.setHeaderText("Enter the number of players in the game");
        dialog.setContentText("Number of players:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(players -> {
            try {
                int numberOfPlayers = Integer.parseInt(players);
                // Perform create game action with the number of players
                System.out.println("Creating a new game with " + numberOfPlayers + " players");
                ClientManager.gameManagerController.onCreateGame(numberOfPlayers, ClientManager.userNickname,stub);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input: Please enter a valid number of players.", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }


    private Callback<ListView<Game>, ListCell<Game>> createGameCellFactory() {
        return new Callback<>() {
            @Override
            public ListCell<Game> call(ListView<Game> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Game item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.toString());
                        }
                    }

                    @Override
                    public void updateSelected(boolean selected) {
                        super.updateSelected(selected);
                        if (selected) {
                            setGraphic(createJoinButton());
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        };
    }


    private Button createJoinButton() {
        Button joinButton = new Button("Join");
        joinButton.setAlignment(Pos.CENTER_RIGHT);
        joinButton.setOnAction(event -> {
            // Handle join button action here
            // You can access the selected game using
            // the ListView's getSelectionModel() method
            Game selectedGame = gameListView.getSelectionModel().getSelectedItem();
            if (selectedGame != null) {
                // Perform join action for the selected game
                System.out.println("Joining game: " + selectedGame.getId());
                ClientManager.gameManagerController.onSelectGame(selectedGame.getId(), ClientManager.userNickname, stub);
            }
        });
        return joinButton;
    }

    private void updateGameListWithRandomData() {
        ObservableList<Game> randomGames = generateRandomGameList();
        gameList.setAll(randomGames);
    }

    public void setGameList(HashMap<String, List<String>> availableGames) {
        ArrayList<Game> addedGames = new ArrayList<>();
        for (String x: availableGames.keySet()){
            addedGames.add(new Game(x, availableGames.get(x)));
        }
        this.gameList.setAll(addedGames);
    }

    private ObservableList<Game> generateRandomGameList() {
        List<Game> games = new ArrayList<>();
        Random random = new Random();

        // Generate 4 random games
        for (int i = 0; i < 4; i++) {
            int gameId = random.nextInt(1000);
            int numUsers = random.nextInt(5) + 1;
            List<String> users = new ArrayList<>();
            for (int j = 0; j < numUsers; j++) {
                users.add("User " + (j + 1));
            }
            Game game = new Game(String.valueOf(gameId), users);
            games.add(game);
        }

        return FXCollections.observableArrayList(games);
    }

    private class Game {
        private String id;
        private List<String> users;

        public Game(String id, List<String> users) {
            this.id = id;
            this.users = users;
        }

        public String getId() {
            return id;
        }

        public List<String> getUsers() {
            return users;
        }

        @Override
        public String toString() {
            StringBuilder usersString = new StringBuilder();
            for (String user : users) {
                usersString.append(user).append(", ");
            }
            // Remove the trailing comma and space
            if (usersString.length() > 2) {
                usersString.setLength(usersString.length() - 2);
            }
            return "Game ID: " + id + ", Users: " + usersString.toString();
        }
    }

}
