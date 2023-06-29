package it.polimi.ingsw.view.GUIView;

import it.polimi.ingsw.controller.client.ClientManager;
import it.polimi.ingsw.model.helpers.Pair;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.*;

import static it.polimi.ingsw.controller.client.ClientMain.stub;

/**
 * The view for managing games.
 */
public class GameManagerView {
    private ScreenSwitcher screenSwitcher;
    private ListView<Game> gameListView;
    private final ObservableList<Game> gameList;
    private VBox gameManagerView;

    /**
     * Constructs a GameManagerView with the specified ScreenSwitcher.
     *
     * @param screenSwitcher the ScreenSwitcher used for switching screens
     */
    public GameManagerView(ScreenSwitcher screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
        gameList = FXCollections.observableArrayList();
        //setGameList(availableGames);
    }

    /**
     * Adds a new game to the game list.
     *
     * @param newGame the new game to add
     */
    public void addNewGameToList(Pair<String, List<String>> newGame) {
        Platform.runLater(() -> {
            Game tmp = new Game(newGame.getFirst(), newGame.getSecond());
            boolean found = false;
            for (int i = 0; i < gameList.size() && !found; i++) {
                if (gameList.get(i).getId().equals(tmp.getId())) {
                    this.gameList.set(i, tmp);
                    found = true;
                }
            }
            if (!found) {
                this.gameList.add(tmp);
            }
            this.gameListView.setCellFactory(createGameCellFactory());
        });
    }

    /**
     * Creates the content for the game manager view.
     *
     * @return The VBox containing the game manager view.
     */
    public VBox createContent() {
        gameManagerView = new VBox();

        // Create the game list view
        gameListView = new ListView<>(gameList);
        gameListView.setCellFactory(createGameCellFactory());
        gameManagerView.getChildren().add(gameListView);
        gameListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Create the button container
        HBox buttonContainer = new HBox();

        // Create the root VBox to hold the gameManagerView
        VBox root = new VBox();

        //Title
        Label titleLabel = new Label("Available Games");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-family: 'Curlz MT'");

        //Background for game list
        BackgroundImage gameListBackground = new BackgroundImage(new Image("sfondo_parquet.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        //Background settings
        gameManagerView.setStyle("-fx-background-color: transparent");
        gameListView.setStyle("-fx-background-color: transparent");
        root.setBackground(new Background(gameListBackground));

        // Create the "Create Game" button
        Button createButton = new Button("Create Game");
        createButton.setOnAction(event -> createNewGame());

        // Add the buttons to the button container
        buttonContainer.getChildren().addAll(createButton);

        // Set HBox properties to align buttons at the bottom
        HBox.setHgrow(buttonContainer, Priority.ALWAYS);
        buttonContainer.setFillHeight(true);

        // Add the button container to the VBox
        gameManagerView.getChildren().add(buttonContainer);

        // Set VBox properties to expand the game list view
        VBox.setVgrow(gameListView, Priority.ALWAYS);

        // Create the label for "no current games"
        Label noGamesLabel = new Label("No current games");
        noGamesLabel.getStyleClass().add("no-games-label");
        noGamesLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold");

        // Create a BooleanBinding to track if the gameList is empty
        BooleanBinding isGameListEmpty = Bindings.isEmpty(gameList);
        noGamesLabel.visibleProperty().bind(isGameListEmpty);
        noGamesLabel.managedProperty().bind(isGameListEmpty);

        // Add the label and the gameManagerView to the root VBox
        root.getChildren().addAll(titleLabel, noGamesLabel, gameManagerView);

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
                if(numberOfPlayers >= 2 && numberOfPlayers <= 4) {
                    System.out.println("Creating a new game with " + numberOfPlayers + " players");
                    ClientManager.gameManagerController.onCreateGame(numberOfPlayers, ClientManager.userNickname, stub);
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid input: Please enter a valid number of players.",ButtonType.OK);
                    alert.setHeaderText("Number of Players Error");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input: Please enter a valid number of players.", ButtonType.OK);
                alert.setHeaderText("Number of Players Error");
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
                    //it's called for every line in the game list, it checks whether the line is empty or not
                    protected void updateItem(Game item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                            setStyle("-fx-background-color: transparent");
                        } else {
                            setText(item.toString());
                            setStyle("-fx-background-color: rgb(255,206,135); -fx-font-size: 16px");
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

    /**
     * Displays an error message in a dialog box.
     *
     * @param errorMessage The error message to be displayed.
     */
    public void showError(String errorMessage) {
        Platform.runLater(() -> {
            Alert alert;
            if (errorMessage.equals("The selected lobby is full")) {
                alert = new Alert(Alert.AlertType.INFORMATION, errorMessage, ButtonType.OK);
                alert.setTitle("Invalid Action");
                alert.setHeaderText("Lobby Selection");
            } else {
                alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
                alert.setTitle("Invalid Action");
                alert.setHeaderText("Credentials Error");
            }
            alert.showAndWait();
        });
    }

    private void updateGameListWithRandomData() {
        ObservableList<Game> randomGames = generateRandomGameList();
        gameList.setAll(randomGames);
    }

    /**
     * Sets the game list with the provided available games.
     * If the game list is empty, it creates new Game objects based on the available games and adds them to the list.
     *
     * @param availableGames The available games represented as a HashMap, where the keys are game IDs and the values are lists of player names.
     */
    public void setGameList(HashMap<String, List<String>> availableGames) {
        ArrayList<Game> addedGames = new ArrayList<>();
        if (this.gameList.isEmpty()) {
            for (String gameId : availableGames.keySet()) {
                addedGames.add(new Game(gameId, availableGames.get(gameId)));
            }
        }
        this.gameList.addAll(addedGames);
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
