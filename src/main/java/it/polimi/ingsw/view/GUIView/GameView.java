package it.polimi.ingsw.view.GUIView;

import it.polimi.ingsw.controller.client.ClientManager;
import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.*;


/**
 * The GameView class represents the view for the game screen.
 */
public class GameView {
    private final ScreenSwitcher screenSwitcher;
    // ImageView per l'immagine dei personal goals
    private String playingPlayer;
    public StringProperty playingPlayerProp;
    private StringProperty chairedPlayerProp;
    private StringProperty gameStateProp;
    private PersonalGoal personalGoal;
    private String chairedPlayer;
    private ObservableList<Pair<String, Pair<String,String>>> chat;
    private ListView<Pair<String, Pair<String,String>>> chatView;
    private boolean chatFirstInitialization;
    private ArrayList<BoardCard> selectedCards = new ArrayList<>();
    public GridPane livingRoomGrid;
    public VBox sxRoot = new VBox();

    public ArrayList<Pair<String, BoardCard[][]>> playersShelves;
    public HBox dxRoot = new HBox();
    public VBox firstPairVBox = new VBox();
    public VBox secondPairVBox = new VBox();

    public HashMap<String, HBox> playerBoxMap = new HashMap<>();
    public HashMap<String, GridPane> playerGridMap = new HashMap<>();

    public Boolean[][] selectables;
    public HashMap<String, HBox> playerPointsBoxMap = new HashMap<>();

    private HashMap<String, Integer> playerPoints;
    private BoardCard[][] livingRoom;
    private BoardCard[][] playerShelf;
    private int maxSpace;
    private String chatDestination;
    private ComboBox<String> receiverBox;
    private Button[][] livingRoomButtons;
    private int selectedColumn;

    /**
     * Constructs a GameView object.
     *
     * @param screenSwitcher the ScreenSwitcher object used for switching between screens
     */
    public GameView(ScreenSwitcher screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
        chat = FXCollections.observableArrayList();
    }


    /**
     * Creates the content for the game view.
     *
     * @param players         The list of players.
     * @param commonGoals     The common goals.
     * @param personalGoals   The personal goals.
     * @param livingRoom      The living room grid.
     * @param selectables     The selectable positions in the living room.
     * @param playersShelves  The players' shelves.
     * @param playersPoints   The points of each player.
     * @param gameState       The current game state.
     * @return The root HBox containing the game view.
     */
    public HBox createContent(List<String> players, CommonGoals commonGoals, HashMap<String, PersonalGoal> personalGoals,
                              BoardCard[][] livingRoom, Boolean[][] selectables, ArrayList<Pair<String, BoardCard[][]>>
                                      playersShelves, HashMap<String, Integer> playersPoints, GameStateType gameState) {
        this.chatFirstInitialization = true;
        this.playingPlayerProp = new SimpleStringProperty();
        this.chairedPlayerProp = new SimpleStringProperty();
        this.gameStateProp = new SimpleStringProperty();
        this.playersShelves = playersShelves;
        this.selectables = selectables;
        this.chatDestination = null;

        //Root of game view
        HBox root = new HBox();

        //SX part of root

        //Game ID Container
        HBox gameIdContainer = new HBox();
        Label gameIdLabelDescr = new Label("Game ID: ");
        Label gameIdLabel = new Label(ClientManager.lobbyController.getID());
        gameIdLabelDescr.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        gameIdLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        gameIdLabel.setTextFill(Color.LIGHTBLUE);
        gameIdContainer.getChildren().addAll(gameIdLabelDescr, gameIdLabel);


        //Game info and commonGoals button HBox
        HBox firstSection = new HBox();
        VBox gameInfo = new VBox();
        HBox gameStateBox = new HBox();
        Label gameStateLabelDescr = new Label("Game State: ");
        gameStateLabelDescr.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        Label gameStateLabel = new Label(gameState.toString());
        gameStateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: LIGHTGREEN");
        gameStateLabel.textProperty().bind(gameStateProp);
        gameStateBox.getChildren().addAll(gameStateLabelDescr, gameStateLabel);
        HBox hostBox = new HBox();
        Label hostLabelDescr = new Label("Game Host: ");
        hostLabelDescr.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        Label hostLabel = new Label(ScreenSwitcher.gameLobbyView.getHost());
        hostLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: ORANGE");
        hostBox.getChildren().addAll(hostLabelDescr, hostLabel);
        HBox chairedPlayerBox = new HBox();
        Label chairedPlayerDescrLabel = new Label("Starting Player: ");
        chairedPlayerDescrLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        Label chairedPlayerLabel = new Label();
        chairedPlayerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: WHITE");
        chairedPlayerBox.getChildren().addAll(chairedPlayerDescrLabel, chairedPlayerLabel);
        chairedPlayerLabel.textProperty().bind(chairedPlayerProp);
        HBox playingPlayerBox = new HBox();
        Label playingPlayerDescrLabel = new Label("Playing Player: ");
        playingPlayerDescrLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        Label playingPlayerLabel = new Label();
        playingPlayerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: YELLOW");
        playingPlayerBox.getChildren().addAll(playingPlayerDescrLabel, playingPlayerLabel);
        playingPlayerLabel.textProperty().bind(playingPlayerProp);
        gameInfo.getChildren().addAll(gameStateBox, hostBox, chairedPlayerBox, playingPlayerBox);

        Button commonGoalsButton = new Button("Common Goals");
        commonGoalsButton.setStyle("-fx-background-color: rgb(229,163,91); -fx-text-fill: WHITE; -fx-font-weight: bold");
        commonGoalsButton.setOnAction(e -> {
            getCommonGoalsImage(commonGoals);
        });
        firstSection.getChildren().addAll(gameInfo, commonGoalsButton);

        //LivingRoom grid pane
        Pair<GridPane, Button[][]> result = createLivingRoomGridCells(livingRoom);
        livingRoomGrid = result.getFirst();
        Button[][] buttons = result.getSecond();

        //Chat section
        VBox chatSection = new VBox();
        Label chatTitle = new Label(" Chat ");
        chatTitle.setStyle("-fx-background-color: rgb(255,224,199);-fx-font-size: 14px;");
        chatTitle.setPadding(new Insets(3));
        //Input and Receiver Section
        HBox inputChatSection = new HBox();
        //ComboBox
        this.receiverBox = new ComboBox<>();
        receiverBox.getItems().add("All");
        for(String s: players) {
            if(!s.equals(ClientManager.userNickname)) {
                receiverBox.getItems().add(s);
            }
        }
        receiverBox.setPromptText("Destination");
        receiverBox.setValue("All");
        receiverBox.setEditable(true);
        receiverBox.valueProperty().addListener((ov, t, t1) -> this.chatDestination = t1);
        //Rest of ChatSection
        Button chatButton = new Button("Send");
        TextField chatMessage = new TextField();
        HBox.setHgrow(chatMessage, Priority.ALWAYS);
        ScrollPane scrollBox = new ScrollPane();
        chatView = new ListView<>(chat);
        chatView.setCellFactory(createChatCellFactory());
        scrollBox.setContent(chatView); // apply "this." in case of scrollbar-at-bottom code
        scrollBox.setFitToHeight(true); // apply "this." in case of scrollbar-at-bottom code
        scrollBox.setFitToWidth(true); // apply "this." in case of scrollbar-at-bottom code
        //This background set refers only to the listview, we also need a style set for the single cell
        chatView.setStyle("-fx-background-color: rgb(229,163,91)"); //LOOK TO THE LEFT, see that square, you can adjust the color from there

        inputChatSection.getChildren().addAll(chatMessage,receiverBox, chatButton);
        chatSection.getChildren().addAll(chatTitle, scrollBox, inputChatSection);

        ClientManager.gameController.onGetChat(true);

        chatButton.setOnAction(event -> {
            if (!chatMessage.getText().isEmpty()) {
                if(!receiverBox.getValue().isEmpty() && !receiverBox.getValue().equals("")){
                    if(!receiverBox.getValue().equals("All")){
                        ArrayList<Pair<String,Pair<String,String>>> privateMessage = new ArrayList<>();
                        privateMessage.add(new Pair<>(ClientManager.gameController.getGameID(),new Pair<>(ClientManager.userNickname, receiverBox.getValue())));
                        updateChatMessages(privateMessage);
                    }
                    ClientManager.lobbyController.onSendChatMessage(chatMessage.getText(),receiverBox.getValue());
                    chatMessage.clear();
                    receiverBox.setValue(null);
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Before clicking on the 'Send' button, please choose a receiver");
                    alert.setTitle("Invalid Action");
                    alert.setHeaderText("No destination");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Before clicking on the 'Send' button, please insert a message");
                alert.setTitle("Invalid Action");
                alert.setHeaderText("No chat input");
                alert.showAndWait();
            }
        });
        // LivingRoom grid pane


        //Size adaptation section
        VBox.setVgrow(gameIdLabel, Priority.ALWAYS);
        HBox.setHgrow(firstSection, Priority.ALWAYS);
        VBox.setVgrow(firstSection, Priority.ALWAYS);
        HBox.setHgrow(gameInfo, Priority.ALWAYS);
        HBox.setHgrow(commonGoalsButton, Priority.ALWAYS);
        VBox.setVgrow(livingRoomGrid, Priority.ALWAYS);
        //GridPane.setVgrow(livingRoomGrid,Priority.ALWAYS);
        VBox.setVgrow(chatSection, Priority.ALWAYS);

        sxRoot.getChildren().addAll(gameIdContainer, firstSection, livingRoomGrid, chatSection);

        //DX part of root

        firstPairVBox.setPadding(new Insets(40));
        secondPairVBox.setPadding(new Insets(40));

        //Players' HBoxes
        for (int i = 0; i < playersShelves.size(); i++) { // There will be at least two players
            if(playersShelves.get(i).getFirst().equals(ClientManager.userNickname)){
                this.playerShelf = playersShelves.get(i).getSecond();
            }
            playerBoxMap.put(playersShelves.get(i).getFirst(), new HBox());
            GridPane playerShelfGrid;
            VBox playerInfo = new VBox();
            HBox playerNameContainer = new HBox();
            Label playerNameDescr = new Label("Player: ");
            playerNameDescr.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
            Label playerName = new Label(playersShelves.get(i).getFirst());
            playerName.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: WHITE");
            playerNameContainer.getChildren().addAll(playerNameDescr, playerName);
            HBox playerPointsContainer = new HBox();
            playerPointsBoxMap.put(playersShelves.get(i).getFirst(), playerPointsContainer);
            Label playerPointsDescr = new Label("Points: ");
            playerPointsDescr.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
            Label playerPoints = new Label(playersPoints.get(playersShelves.get(i).getFirst()).toString());
            //TODO: bind of player points
            playerPoints.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: WHITE");
            playerPointsContainer.getChildren().addAll(playerPointsDescr, playerPoints);
            Button personalGoalButton = new Button("Personal Goal");
            PersonalGoal myPersonalGoal = personalGoals.get(playersShelves.get(i).getFirst());
            personalGoalButton.setOnAction(e -> {
                showPersonalGoalImage(myPersonalGoal);
            });

            if (!playersShelves.get(i).getFirst().equals(ClientManager.userNickname)) {
                personalGoalButton.setVisible(false);
                personalGoalButton.setDisable(true);
            }

            playerShelfGrid = createShelf(playersShelves.get(i).getSecond());

            playerInfo.getChildren().addAll(playerNameContainer, playerPointsContainer, personalGoalButton);
            playerGridMap.put(playersShelves.get(i).getFirst(), playerShelfGrid);
            playerBoxMap.get(playersShelves.get(i).getFirst()).getChildren().addAll(playerShelfGrid, playerInfo);
            if (i < 2) {
                firstPairVBox.getChildren().add(playerBoxMap.get(playersShelves.get(i).getFirst()));
            } else {
                secondPairVBox.getChildren().add(playerBoxMap.get(playersShelves.get(i).getFirst()));
            }
        }
        //TODO: check that it grow from left to right
        if (players.size() > 2) {
            dxRoot.getChildren().addAll(firstPairVBox, secondPairVBox);
        } else {
            Region fillerHbox1 = new Region();
            HBox.setHgrow(fillerHbox1, Priority.ALWAYS);
            HBox.setHgrow(firstPairVBox, Priority.ALWAYS);
            dxRoot.getChildren().addAll(fillerHbox1, firstPairVBox);
        }

        InputStream imagestream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        imagestream = this.getClass().getResourceAsStream("/misc/sfondo_parquet.jpg");

        BackgroundImage rootBackground = new BackgroundImage(new Image(imagestream), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(rootBackground));

        root.getChildren().addAll(sxRoot, dxRoot);

        return root;
    }
    private void getCommonGoalsImage(CommonGoals commonGoals) {
        ArrayList<String> targetsCG = new ArrayList<>();
        targetsCG.add("/common_goal_cards/1.jpg");
        targetsCG.add("/common_goal_cards/2.jpg");
        targetsCG.add("/common_goal_cards/3.jpg");
        targetsCG.add("/common_goal_cards/4.jpg");
        targetsCG.add("/common_goal_cards/5.jpg");
        targetsCG.add("/common_goal_cards/6.jpg");
        targetsCG.add("/common_goal_cards/7.jpg");
        targetsCG.add("/common_goal_cards/8.jpg");
        targetsCG.add("/common_goal_cards/9.jpg");
        targetsCG.add("/common_goal_cards/10.jpg");
        targetsCG.add("/common_goal_cards/11.jpg");
        targetsCG.add("/common_goal_cards/12.jpg");

        ArrayList<String> descriptions = new ArrayList<>();
        descriptions.add("Two groups each containing 4 tiles of \n the same type in a 2x2 square. The tiles \n of one square can be different from \nthose of the other square.");
        descriptions.add("Two columns each formed by 6 \ndifferent types of tiles." );
        descriptions.add("Four groups each containing at least \n4 tiles of the same type (not necessarily \nin the depicted shape).\nThe tiles of one group can be different \nfrom those of another group.");
        descriptions.add("Six groups each containing at least \n 2 tiles of the same type \n (not necessarily in the depicted shape).\n The tiles of one group can be different \n from those of another group.");
        descriptions.add("Three columns each formed by 6 tiles \nof maximum three different types. One \ncolumn can show the same or a different \ncombination of another column.");
        descriptions.add("Two lines each formed by 5 different \ntypes of tiles. One line can show the \nsame or a different combination of the other line.");
        descriptions.add("""
                Four lines each formed by 5 tiles of\s
                maximum three different types. One\s
                line can show the same or a different\s
                combination of another line.""");
        descriptions.add("Four tiles of the same type in the four\n corners of the bookshelf.");
        descriptions.add("Eight tiles of the same type. There’s no \n" +
                "restriction about the position of these \n" +
                "tiles.");
        descriptions.add("five tiles of the same type forming an X.");
        descriptions.add("Five tiles of the same type forming a diagonal.");
        descriptions.add("Five columns of increasing or decreasing \n" +
                "height. Starting from the first column on \n" +
                "the left or on the right, each next column \n" +
                "must be made of exactly one more tile. \n" +
                "Tiles can be of any type. ");


        System.out.println(commonGoals.getFirstGoal());
        System.out.println(commonGoals.getSecondGoal());
        int first = commonGoals.getFirstGoal().getIndex();
        int second = commonGoals.getSecondGoal().getIndex();
        System.out.println(first);
        System.out.println(second);

        Stage commonGoalsStage = new Stage();

        // Crea la griglia per le immagini
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Aggiungi le righe e colonne alla griglia
        for (int row = 0; row < 3; row++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / 3);
            gridPane.getRowConstraints().add(rowConstraints);
        }
        for (int col = 0; col < 4; col++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / 4);
            gridPane.getColumnConstraints().add(colConstraints);
        }

        // Aggiungi le immagini alla griglia
        int imageIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                InputStream imageStream = getClass().getResourceAsStream(targetsCG.get(imageIndex));
                ImageView imageView = new ImageView(new Image(imageStream));
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setOpacity(0.3);

                Tooltip descriptionTooltip = new Tooltip(descriptions.get(imageIndex));  // Replace "Description" with your desired description

                // Set the Tooltip to hide after a certain duration (optional)
                descriptionTooltip.setAutoHide(true);
                descriptionTooltip.setHideDelay(Duration.seconds(2));  // Adjust the duration as needed

                // Event handler for drag enter
                imageView.setOnMouseClicked(event -> {
                    descriptionTooltip.show(imageView, event.getScreenX(), event.getScreenY());
                });

                gridPane.add(imageView, col, row);

                if (imageIndex == (first - 1) || imageIndex == (second - 1))
                    imageView.setOpacity(1.0);

                imageIndex++;
                if (imageIndex >= 12) {
                    imageIndex = 1;
                    break;
                }
            }
        }

        // Crea la scena e mostra la finestra
        Scene scene = new Scene(gridPane);
        commonGoalsStage.setTitle("Common Goals");
        commonGoalsStage.setScene(scene);
        commonGoalsStage.show();
    }
    /**
     * Sets the currently playing player.
     * Updates the playing player property.
     * If the playing player is the current client user, displays an information alert indicating that it's their turn.
     *
     * @param playingPlayer The nickname of the playing player.
     */
    public void setPlayingPlayer(String playingPlayer) {
        Platform.runLater(() -> {
            this.playingPlayerProp.set(playingPlayer);
            if (playingPlayer.equals(ClientManager.userNickname)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
                alert.setTitle("Turn Change");
                alert.setHeaderText("It's your turn");
                alert.showAndWait();
            }
        });
    }

    /**
     * Sets the currently chaired player.
     * Updates the chaired player property.
     *
     * @param chairedPlayer The nickname of the chaired player.
     */
    public void setChairedPlayer(String chairedPlayer) {
        Platform.runLater(() -> {
            this.chairedPlayerProp.set(chairedPlayer);
        });
    }

    /**
     * Sets the current game state.
     * Updates the game state property.
     *
     * @param gameState The current game state.
     */
    public void setGameState(String gameState) {
        Platform.runLater(() -> {
            this.gameStateProp.set(gameState);
        });
    }
    private Callback<ListView<Pair<String,Pair<String,String>>>, ListCell<Pair<String,Pair<String,String>>>> createChatCellFactory() {
        return new Callback<>() {
            @Override
            public ListCell<Pair<String,Pair<String,String>>> call(ListView<Pair<String,Pair<String,String>>> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Pair<String,Pair<String,String>> item, boolean empty) {
                        super.updateItem(item, empty);
                        setStyle("-fx-background-color: rgb(229,163,91); -fx-font-size: 13px");
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(getChatString(item));
                        }
                    }
                };
            }
        };
    }
    public void updateChatMessages(ArrayList<Pair<String,Pair<String,String>>> chatMessages){
        Platform.runLater(()->{
            if(chatFirstInitialization){
                //Adds all messages
                this.chat.addAll(chatMessages);
                this.chatFirstInitialization = false;
            }else{
                //Adds only the last message
                if(chatMessages.size()>0) {
                    this.chat.add(chatMessages.get(chatMessages.size() - 1));
                }
            }
            this.chatView.setCellFactory(createChatCellFactory());
            /* Code maybe useful for setting scrollBar at bottom
            this.scrollBox.applyCss();
            this.scrollBox.layout();
            this.scrollBox.setVvalue(scrollBox.getVmax());
            */
        });
    }
    private String getChatString(Pair<String, Pair<String,String>> tmp) {
        String s = null;
        if(tmp.getFirst().equals("All")){
            s = "[Public]  " + tmp.getSecond().getFirst() + ":  " + tmp.getSecond().getSecond();
        } else if (tmp.getFirst().equals(ClientManager.userNickname)) {
            s = "[Private]  " + tmp.getSecond().getFirst() + ":  " + tmp.getSecond().getSecond();
        }
        return s;
    }
    private String getBoardCardImage(BoardCard tmp) {

        ArrayList<String> targetTiles = new ArrayList<>();

        targetTiles.add("/item_tiles/Cornici1.1.png");
        targetTiles.add("/item_tiles/Cornici1.2.png");
        targetTiles.add("/item_tiles/Cornici1.3.png");
        targetTiles.add("/item_tiles/Gatti1.1.png");
        targetTiles.add("/item_tiles/Gatti1.2.png");
        targetTiles.add("/item_tiles/Gatti1.3.png");
        targetTiles.add("/item_tiles/Giochi1.1.png");
        targetTiles.add("/item_tiles/Giochi1.2.png");
        targetTiles.add("/item_tiles/Giochi1.3.png");
        targetTiles.add("/item_tiles/Libri1.1.png");
        targetTiles.add("/item_tiles/Libri1.2.png");
        targetTiles.add("/item_tiles/Libri1.3.png");
        targetTiles.add("/item_tiles/Piante1.1.png");
        targetTiles.add("/item_tiles/Piante1.2.png");
        targetTiles.add("/item_tiles/Piante1.3.png");
        targetTiles.add("/item_tiles/Trofei1.1.png");
        targetTiles.add("/item_tiles/Trofei1.2.png");
        targetTiles.add("/item_tiles/Trofei1.3.png");
        targetTiles.add("");
        if (tmp.getColor().equals(colorType.BLUE)) {
            if (tmp.getOrnament().equals(ornamentType.A))
                return targetTiles.get(0);
            if (tmp.getOrnament().equals(ornamentType.B)) {
                return targetTiles.get(1);
            }
            if (tmp.getOrnament().equals(ornamentType.C)) {
                return targetTiles.get(2);
            }
        } else if (tmp.getColor().equals(colorType.GREEN)) {
            if (tmp.getOrnament().equals(ornamentType.A)) {
                return targetTiles.get(3);
            } else if (tmp.getOrnament().equals(ornamentType.B)) {
                return targetTiles.get(4);
            } else if (tmp.getOrnament().equals(ornamentType.C)) {
                return targetTiles.get(5);
            }
        } else if (tmp.getColor().equals(colorType.YELLOW)) {
            if (tmp.getOrnament().equals(ornamentType.A)) {
                return targetTiles.get(6);
            } else if (tmp.getOrnament().equals(ornamentType.B)) {
                return targetTiles.get(7);
            } else if (tmp.getOrnament().equals(ornamentType.C)) {
                return targetTiles.get(8);
            }
        } else if (tmp.getColor().equals(colorType.WHITE)) {
            if (tmp.getOrnament().equals(ornamentType.A)) {
                return targetTiles.get(9);
            } else if (tmp.getOrnament().equals(ornamentType.B)) {
                return targetTiles.get(10);
            } else if (tmp.getOrnament().equals(ornamentType.C)) {
                return targetTiles.get(11);
            }
        } else if (tmp.getColor().equals(colorType.PURPLE)) {
            if (tmp.getOrnament().equals(ornamentType.A)) {
                return targetTiles.get(12);
            } else if (tmp.getOrnament().equals(ornamentType.B)) {
                return targetTiles.get(13);
            } else if (tmp.getOrnament().equals(ornamentType.C)) {
                return targetTiles.get(14);
            }
        } else if (tmp.getColor().equals(colorType.LIGHT_BLUE)) {
            if (tmp.getOrnament().equals(ornamentType.A)) {
                return targetTiles.get(15);
            } else if (tmp.getOrnament().equals(ornamentType.B)) {
                return targetTiles.get(16);
            } else if (tmp.getOrnament().equals(ornamentType.C)) {
                return targetTiles.get(17);
            }
        }
        return "";
    }
    private void showPersonalGoalImage(PersonalGoal personalGoal) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Personal Goal");
        String stream = getPersonalGoalImage(personalGoal);
        InputStream imageStream = getClass().getResourceAsStream(stream);
        Image personalGoalImage = new Image(imageStream);
        ImageView imageView = new ImageView(personalGoalImage);

        // Imposta la larghezza desiderata dell'immagine
        imageView.setFitWidth(400); // Imposta la larghezza desiderata (puoi modificare questo valore come preferisci)
        imageView.setPreserveRatio(true); // Mantiene le proporzioni dell'immagine

        VBox popupLayout = new VBox();
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.getChildren().add(imageView);

        Scene popupScene = new Scene(popupLayout);
        popupStage.setScene(popupScene);

        popupStage.show();
    }
    //PersonalGoal to image
    private String getPersonalGoalImage(PersonalGoal pg) {
        String[] targetPersonalGoal = {
                "/personal_goal_cards/Personal_Goals.png",
                "/personal_goal_cards/Personal_Goals2.png",
                "/personal_goal_cards/Personal_Goals3.png",
                "/personal_goal_cards/Personal_Goals4.png",
                "/personal_goal_cards/Personal_Goals5.png",
                "/personal_goal_cards/Personal_Goals6.png",
                "/personal_goal_cards/Personal_Goals7.png",
                "/personal_goal_cards/Personal_Goals8.png",
                "/personal_goal_cards/Personal_Goals9.png",
                "/personal_goal_cards/Personal_Goals10.png",
                "/personal_goal_cards/Personal_Goals11.png",
                "/personal_goal_cards/Personal_Goals12.png"
        };

        int index = pg.getSelectedGoalIndex();
        return targetPersonalGoal[index];
    }
    private GridPane createShelf(BoardCard[][] shelf) {
        GridPane root = new GridPane();
        // Imposta l'allineamento e lo spaziamento delle celle
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setGridLinesVisible(true);
        root.setPadding(new Insets(10)); // Imposta il padding per la griglia

        // Imposta le dimensioni ridotte per le celle della griglia
        int cellWidth = 200 / 5; // Divide la larghezza corrente per 3
        int cellHeight = 200 / 5; // Divide l'altezza corrente per 3
        for (int i = 0; i < shelf.length; i++) {
            root.getRowConstraints().add(new RowConstraints(cellHeight));
        }
        for (int i = 0; i < shelf[0].length; i++) {
            root.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }

        // Itera attraverso la livingRoom e crea le celle
        for (int row = 0; row < shelf.length; row++) {
            for (int col = 0; col < shelf[0].length; col++) {
                BoardCard card = shelf[row][col];
                // Crea l'ImageView per la carta basata sullo stato del BoardCard
                String stream = getBoardCardImage(card);
                if(!stream.equals("")) {
                    InputStream imageStream = getClass().getResourceAsStream(stream);
                    Image cardImage = new Image(imageStream);
                    ImageView cardImageView = new ImageView(cardImage);
                    cardImageView.setFitWidth(cellWidth); // Imposta la larghezza dell'immagine
                    cardImageView.setFitHeight(cellHeight); // Imposta l'altezza dell'immagine

                    // Crea il Pane come contenitore per l'ImageView
                    Pane cellPane = new Pane();
                    cellPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;"); // Imposta il colore e lo spessore del bordo
                    cellPane.getChildren().add(cardImageView);

                    // Aggiungi il cellPane alla cella corrispondente nella GridPane
                    root.add(cellPane, col, row);
                }
            }
        }
        return root;
    }
    int round = 0;
    private Pair<GridPane, Button[][]> createLivingRoomGridCells(BoardCard[][] livingRoom) {
            this.livingRoom = livingRoom;
            ArrayList<Button> clickedButtons = new ArrayList<>();
            GridPane gridPane = new GridPane();
            ArrayList<Pair<Integer, Integer>> coord = new ArrayList<>();
            // Imposta l'allineamento e lo spaziamento delle celle
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setGridLinesVisible(true);
            gridPane.setPadding(new Insets(10)); // Imposta il padding per la griglia

            // pane -> imageview -> gridpane(set pro
            /*
            Pane tmp = new Pane();
            ImageView imageView = new ImageView();
            gridPane.getChildren().add(imageView.fitHeightProperty())
             */

            // Imposta le dimensioni ridotte per le celle della griglia
            int cellWidth = 200 / 5; // Divide la larghezza corrente per 3
            int cellHeight = 200 / 5; // Divide l'altezza corrente per 3

            Button[][] buttons = new Button[livingRoom.length][livingRoom[0].length];

            for (int i = 0; i < livingRoom.length; i++) {
                gridPane.getColumnConstraints().add(new ColumnConstraints(cellWidth));
                gridPane.getRowConstraints().add(new RowConstraints(cellHeight));
            }
            Button finishedButton = new Button("OK");
            finishedButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            finishedButton.setPrefSize(cellWidth * 2, cellHeight * 2);
            gridPane.add(finishedButton, 8, 8);
            // Itera attraverso la livingRoom e crea le celle
            for (int row = 0; row < livingRoom.length; row++) {
                for (int col = 0; col < livingRoom[row].length; col++) {
                    BoardCard card = livingRoom[row][col];
                    // Crea l'ImageView per la carta basata sullo stato del BoardCard
                    String stream = getBoardCardImage(card);
                    if (!stream.equals("")) {
                        InputStream imageStream = this.getClass().getResourceAsStream(stream);
                        Image cardImage = new Image(imageStream);
                        ImageView cardImageView = new ImageView(cardImage);
                        cardImageView.setFitWidth(cellWidth); // Imposta la larghezza dell'immagine
                        cardImageView.setFitHeight(cellHeight); // Imposta l'altezza dell'immagine

                        // Crea il bottone e imposta le dimensioni in base all'immagine
                        Button button = new Button();
                        button.setGraphic(cardImageView);
                        button.setMaxSize(cellWidth, cellHeight);
                        button.setMinSize(cellWidth, cellHeight);
                        int finalCol = col;
                        int finalRow = row;
                        int finalCol1 = col;
                        int finalRow1 = row;
                        button.setOnAction(event -> {
                            System.out.println(ClientManager.userNickname + " clicked on a living room card");
                            System.out.println(playingPlayerProp.get() + " is the current playing player");
                            if (!Objects.equals(ClientManager.userNickname, playingPlayerProp.get())) {
                                // Display an alert blocking the selection
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Invalid Action");
                                alert.setHeaderText("Cannot Perform Action");
                                alert.setContentText("You are not the current playing player. Selection blocked.");
                                alert.showAndWait();
                            } else {
                                finishedButton.setOnAction(event1 -> {
                                    // Mostra una finestra di dialogo o un messaggio di conferma
                                    Alert Confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                                    Confirmation.setTitle("Conferma selezione");
                                    Confirmation.setHeaderText("Sei sicuro di voler confermare la selezione?");
                                    Confirmation.setContentText("Le carte selezionate verranno confermate.");

                                    Optional<ButtonType> result = Confirmation.showAndWait();
                                    if (result.isPresent() && result.get() == ButtonType.OK && checkRule(coord) && checkRuleDiagonal(coord) && inAdiacent(coord)) {
                                        // L'utente ha confermato la selezione, puoi eseguire le azioni desiderate qui
                                        // Rimuovi i bottoni corrispondenti alle coordinate selezionate
                                        System.out.println("Selected cards: " + selectedCards);
                                        ClientManager.gameController.onSelectedCards(coord, ClientManager.userNickname);

                                    } else if (result.get() == ButtonType.CANCEL) {
                                        // L'utente ha annullato la selezione, puoi ripristinare lo stato precedente se necessario
                                        selectedCards.clear();
                                        coord.clear();
                                        // Re-enable all the clicked buttons
                                        for (Button clickedButton : clickedButtons) {
                                            clickedButton.setDisable(false);
                                        }
                                        clickedButtons.clear();
                                    } else if (!checkRule(coord) || !checkRuleDiagonal(coord) || !inAdiacent(coord)) {
                                        selectedCards.clear();
                                        coord.clear();
                                        clickedButtons.clear();
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setTitle("Invalid Action");
                                        alert.setHeaderText("Cannot Perform Action");
                                        alert.setContentText("Not selectable cards. Selection blocked.");
                                        alert.showAndWait();
                                    }
                                });
                                // Codice da eseguire quando il bottone viene premuto
                                if (selectedCards.size() < 3 && selectables[finalRow1][finalCol1]) {
                                    if (round == 1) {
                                        maxSpace = getMaxSpaceInGrid(playerGridMap.get(playingPlayerProp.get()));
                                    }
                                    // Add the clicked button to the list
                                    clickedButtons.add(button);
                                    // Disable the button
                                    button.setDisable(true);
                                    selectedCards.add(card); // Aggiunge la BoardCard alla lista "selected"
                                    coord.add(new Pair<>(finalRow, finalCol)); // Aggiunge le coordinate alla lista "coord"
                                }
                                // Logica per terminare la selezione dopo aver selezionato tre carte
                                if ((selectedCards.size() == 3 || selectedCards.size() == maxSpace) && checkRule(coord) && checkRuleDiagonal(coord) && inAdiacent(coord)) {
                                    // Mostra una finestra di dialogo o un messaggio di conferma
                                    Alert Confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                                    Confirmation.setTitle("Conferma selezione");
                                    Confirmation.setHeaderText("Sei sicuro di voler confermare la selezione?");
                                    Confirmation.setContentText("Le carte selezionate verranno confermate.");

                                    Optional<ButtonType> result = Confirmation.showAndWait();
                                    if (result.isPresent() && result.get() == ButtonType.OK) {
                                        // L'utente ha confermato la selezione, puoi eseguire le azioni desiderate qui
                                        // Rimuovi i bottoni corrispondenti alle coordinate selezionate
                                        System.out.println("Selected cards: " + selectedCards);
                                        round = 1;
                                        ClientManager.gameController.onSelectedCards(coord, ClientManager.userNickname);

                                    } else {
                                        // L'utente ha annullato la selezione, puoi ripristinare lo stato precedente se necessario
                                        selectedCards.clear();
                                        coord.clear();
                                        // Re-enable all the clicked buttons
                                        for (Button clickedButton : clickedButtons) {
                                            clickedButton.setDisable(false);
                                        }
                                        clickedButtons.clear();
                                    }
                                } else if (!checkRule(coord) || !checkRuleDiagonal(coord) || !inAdiacent(coord)) {
                                    // L'utente ha annullato la selezione, puoi ripristinare lo stato precedente se necessario
                                    selectedCards.clear();
                                    coord.clear();
                                    // Re-enable all the clicked buttons
                                    for (Button clickedButton : clickedButtons) {
                                        clickedButton.setDisable(false);
                                    }
                                    clickedButtons.clear();
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Invalid Action");
                                    alert.setHeaderText("Cannot Perform Action");
                                    alert.setContentText("Not selectable cards. Selection blocked.");
                                    alert.showAndWait();
                                }
                            }

                        });
                        gridPane.add(button, col, row);
                        buttons[row][col] = button;
                    }
                }
            }
            this.livingRoomButtons = buttons;
        return new Pair<>(gridPane, buttons);
    }
    private int getMaxSpaceInGrid(GridPane gridPane) {
        int maxSpace = 0;
        int tmpSpace = 0;

        for (int col = 0; col < 5; col++) {
            tmpSpace = isSpaceAvailableInColumn(col);
            if (tmpSpace > maxSpace) {
                maxSpace = tmpSpace;
            }
        }
        System.out.println("Max space: " + maxSpace);
        return maxSpace;
    }
    //Choose column
    /**
     * Handles the ordering of selected cards.
     * If there are multiple selected cards, displays an interface for ordering them.
     * If there is only one selected card, proceeds to setting up the column buttons.
     */
    public void orderSelected() {
        Platform.runLater(() -> {
            if (selectedCards.size() > 1) {
                Stage stage = new Stage();
                selectOrder(selectedCards);

                // Create the user interface
                VBox root = new VBox(10);
                root.setPadding(new Insets(10));
                root.setAlignment(Pos.CENTER);

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);

                // Add cards to the grid
                for (int i = 0; i < selectedCards.size(); i++) {
                    Button cardButton = new Button();
                    String stream = getBoardCardImage(selectedCards.get(i));
                    InputStream imageStream = this.getClass().getResourceAsStream(stream);
                    Image cardImage = new Image(imageStream);
                    ImageView imageView = new ImageView(cardImage);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);
                    cardButton.setGraphic(imageView);

                    Label numberLabel = new Label(Integer.toString(i + 1));
                    numberLabel.setStyle("-fx-font-weight: bold;");

                    VBox vbox = new VBox(5);
                    vbox.setAlignment(Pos.CENTER);
                    vbox.getChildren().addAll(numberLabel, cardButton);

                    int finalIndex = i;
                    cardButton.setOnAction(event -> {
                        Collections.swap(selectedCards, finalIndex, 0);
                        updateGridPane(gridPane);
                    });
                    gridPane.add(vbox, i, 0);
                }

                root.getChildren().add(gridPane);

                Button confirmButton = new Button("CONFIRM");
                confirmButton.setStyle("-fx-background-color: green;");
                confirmButton.setOnAction(event -> {
                    setColumn();
                    stage.close();
                });

                root.getChildren().add(confirmButton);

                Scene scene = new Scene(root, 400, 200);
                stage.setScene(scene);
                stage.setTitle("Order Selection");
                stage.initModality(Modality.APPLICATION_MODAL); // Set the window as modal
                stage.setOnCloseRequest(event -> event.consume()); // Prevent the window from closing when clicking the "X"
                stage.show();
            } else {
                setColumn();
            }
        });
    }

    private void updateGridPane(GridPane gridPane) {
        // Clear the grid pane
        gridPane.getChildren().clear();

        // Add the updated cards to the grid pane
        for (int i = 0; i < selectedCards.size(); i++) {
            Button cardButton = new Button();
            String stream = getBoardCardImage(selectedCards.get(i));
            InputStream imageStream = this.getClass().getResourceAsStream(stream);
            Image image = new Image(imageStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            cardButton.setGraphic(imageView);

            Label numberLabel = new Label(Integer.toString(i + 1));
            numberLabel.setStyle("-fx-font-weight: bold;");

            VBox vbox = new VBox(5);
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(numberLabel, cardButton);

            int finalIndex = i;
            cardButton.setOnAction(event -> {
                Collections.swap(selectedCards, finalIndex, 0);
                updateGridPane(gridPane);
            });
            gridPane.add(vbox, i, 0);
        }
    }
    private void selectOrder(ArrayList<BoardCard> selectedCards) {
        // Questo metodo viene chiamato per impostare le carte selezionate
        this.selectedCards = selectedCards;
    }
    /**
     * Sets up the column buttons for the player's grid.
     * Calculates the dimensions of each button based on the player's grid size.
     * Adds a button for each column in the grid.
     * Handles the button click event to perform actions based on the selected column.
     */
    public void setColumn() {
        GridPane shelfPlayer = playerGridMap.get(ClientManager.userNickname);
        double cellWidth = shelfPlayer.getWidth() / shelfPlayer.getColumnCount();
        double cellHeight = shelfPlayer.getHeight() / (2 * shelfPlayer.getRowCount());

        // Add a button for each column
        for (int col = 0; col < shelfPlayer.getColumnCount(); col++) {
            int column = col; // Save the column number
            Button button = new Button("" + (col + 1));
            button.setPrefWidth(cellWidth);
            button.setPrefHeight(cellHeight);
            button.setOnAction(event -> {
                selectedColumn = column; // Save the selected column number
                // Check if there is enough space in the selected column for selected cards
                int capacity = isSpaceAvailableInColumn(column);
                if (selectedCards.size() <= capacity) {
                    // Add your desired logic here
                    ArrayList<BoardCard> tmp = new ArrayList<>(selectedCards);
                    selectedCards.clear();
                    // Remove the button from the GridPane
                    shelfPlayer.getChildren().remove(button);
                    ClientManager.gameController.onSelectedColumn(tmp, selectedColumn, ClientManager.userNickname);
                } else {
                    String message = "Non c'è abbastanza spazio nella colonna selezionata. Seleziona un'altra colonna.";
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attenzione");
                    alert.setHeaderText(null);
                    alert.setContentText(message);
                    alert.showAndWait();
                }
            });
            shelfPlayer.add(button, col, 6);
        }
    }

    private int isSpaceAvailableInColumn(int column) {
        if (column < 0 || column > 5) {
            throw new IllegalArgumentException("Invalid column index.");
        }

        int spaceAvailable = 0;
        for (int i = 0; i < 6; i++) {
            if (colorType.EMPTY_SPOT == playerShelf[i][column].getColor()) {
                spaceAvailable++;
            }
        }

        return spaceAvailable;
    }
    /**
     * Sets the living room grid with the specified board cards.
     *
     * @param livingRoom The array of board cards representing the living room.
     */
    public void setLivingRoom(BoardCard[][] livingRoom) {
        Platform.runLater(() -> {
            livingRoomGrid.getChildren().clear();
            livingRoomGrid = createLivingRoomGridCells(livingRoom).getFirst();
            sxRoot.getChildren().set(2, livingRoomGrid);
        });
    }


    /**
     * Sets the shelf for a player and updates the corresponding UI elements.
     *
     * @param updatedPlayerShelf The pair containing the player name and the updated player shelf.
     */
    public void setShelf(Pair<String, BoardCard[][]> updatedPlayerShelf) {
        Platform.runLater(() -> {
            if (updatedPlayerShelf != null) {
                if (updatedPlayerShelf.getFirst().equals(ClientManager.userNickname)) {
                    this.playerShelf = updatedPlayerShelf.getSecond();
                }
                String player = updatedPlayerShelf.getFirst();
                GridPane shelfGrid = playerGridMap.get(player);
                HBox shelfBox = playerBoxMap.get(player);
                if (playerGridMap.containsKey(player)) {
                    // Clear the current contents of the shelf grid
                    shelfGrid.getChildren().clear();

                    // Create a new shelf grid based on the updated player shelf
                    shelfGrid = createShelf(updatedPlayerShelf.getSecond());

                    // Update the player grid map with the new shelf grid
                    playerGridMap.put(updatedPlayerShelf.getFirst(), shelfGrid);

                    // Update the first child of the shelf box with the new shelf grid
                    shelfBox.getChildren().set(0, shelfGrid);
                }
            }
        });
    }


    /**
     * Sets the selectables matrix that represents the selectable state of items.
     *
     * @param selectables The Boolean matrix indicating the selectable state of items.
     */
    public void setSelectables(Boolean[][] selectables) {
        Platform.runLater(() -> {
            this.selectables = selectables;
        });
    }

    /**
     * Updates the points for a specific player and refreshes the corresponding UI element.
     *
     * @param updatedPlayerPoints The pair containing the player name and updated points.
     */
    public void setUpdatedPlayerPoints(Pair<String, Integer> updatedPlayerPoints) {
        Platform.runLater(() -> {
            String player = updatedPlayerPoints.getFirst();
            HBox playerPointsBox = playerPointsBoxMap.get(player);

            // Create label for the points description
            Label playerPointsDescr = new Label("Points: ");
            playerPointsDescr.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");

            // Create label for the updated points
            Label playerPoints = new Label(updatedPlayerPoints.getSecond().toString());
            playerPoints.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: WHITE");

            // Update the player points box in the map
            playerPointsBoxMap.put(updatedPlayerPoints.getFirst(), playerPointsBox);

            // Clear the current content of the player points box and add the updated labels
            playerPointsBox.getChildren().clear();
            playerPointsBox.getChildren().addAll(playerPointsDescr, playerPoints);
        });
    }
    /**
     * Sets the final scoreboard and displays it in a separate stage.
     * The scoreboard is sorted in descending order based on the players' scores.
     * @param finalScoreBoard The list of pairs containing player names and scores.
     */
    public void setFinalScoreBoard(ArrayList<Pair<String, Integer>> finalScoreBoard) {
        Platform.runLater(() -> {

            // Create a new stage to display the scoreboard
            Stage stage = new Stage();

            // Create the root VBox for the stage
            VBox root = new VBox(10);
            root.setPadding(new Insets(10));
            root.setAlignment(Pos.CENTER);

            // Carica l'immagine di sfondo

            InputStream imageStream;
            imageStream = this.getClass().getResourceAsStream("/misc/base_pagina2.jpg");
            Image backgroundImage = new Image(imageStream);

            // Create a background image object
            BackgroundImage background = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );

            // Set the background of the root VBox
            root.setBackground(new Background(background));

            // Add the "Scoreboard" label to the center
            Label title = new Label("Scoreboard");
            title.setFont(Font.font("Curlz MT", FontWeight.BOLD, 40));
            title.setStyle("-fx-text-fill: #ffe800;");

            root.getChildren().add(title);

            // Sort the players based on the scores in descending order
            Collections.sort(finalScoreBoard, (a, b) -> b.getSecond().compareTo(a.getSecond()));

            // Iterate over the finalScoreBoard list and add player names and scores to the VBox
            for (Pair<String, Integer> entry : finalScoreBoard) {
                String playerName = entry.getFirst();
                int score = entry.getSecond();
                Label label = new Label(playerName + ": " + score);
                label.setFont(Font.font("Curlz MT", 25));
                label.setStyle("-fx-text-fill: #ffffff;");
                root.getChildren().add(label);
            }

            // Create a scene with the root VBox
            Scene scene = new Scene(root, 500, 400);

            // Set the title of the stage
            stage.setTitle("Scoreboard Test");

            // Set the scene of the stage
            stage.setScene(scene);

            // Show the stage
            stage.show();

            // Set the action to be performed when the stage is closed
            stage.setOnCloseRequest(event -> {
                // Close the stage
                stage.close();
                // Terminate the JavaFX application
                Platform.exit();
            });
        });
    }
    private boolean checkRuleDiagonal(ArrayList<Pair<Integer, Integer>> coordinates) {
        // Verifica che l'ArrayList contenga almeno 1 e al massimo 3 coordinate
        if (coordinates.size() < 1 || coordinates.size() > 3) {
            return false;
        }

        // Verifica che le coordinate siano valide
        int dim = 9; // Dimensione della matrice
        for (Pair<Integer, Integer> coord : coordinates) {
            int row = coord.getFirst();
            int col = coord.getSecond();
            if (row < 0 || row >= dim || col < 0 || col >= dim) {
                return false;
            }
        }

        // Se l'ArrayList contiene solo 1 carta, ritorna true (la regola è rispettata)
        if (coordinates.size() == 1) {
            return true;
        }

        // Se l'ArrayList contiene 2 o 3 carte, verifica se formano una retta orizzontale o verticale
        Pair<Integer, Integer> coord1 = coordinates.get(0);
        Pair<Integer, Integer> coord2 = coordinates.get(1);

        // Controlla se le carte formano una retta in orizzontale o verticale
        boolean isHorizontalLine = coord1.getFirst().equals(coord2.getFirst());
        boolean isVerticalLine = coord1.getSecond().equals(coord2.getSecond());

        // Controlla se le carte formano una linea retta
        if (!isHorizontalLine && !isVerticalLine) {
            return false;
        }

        // Controlla se le carte sono adiacenti e hanno almeno un lato libero
        boolean isAdjacentAndHasFreeSide = (
                Math.abs(coord1.getSecond() - coord2.getSecond()) <= 1 ||
                        Math.abs(coord1.getFirst() - coord2.getFirst()) <= 1
        );

        return isAdjacentAndHasFreeSide;
    }
    private boolean checkRule(ArrayList<Pair<Integer, Integer>> coordinates) {
        // Verifica che l'ArrayList contenga almeno 1 e al massimo 3 coordinate
        if (coordinates.size() < 1 || coordinates.size() > 3) {
            return false;
        }

        // Verifica che le coordinate siano valide
        int dim = 9; // Dimensione della matrice
        for (Pair<Integer, Integer> coord : coordinates) {
            int row = coord.getFirst();
            int col = coord.getSecond();
            if (row < 0 || row >= dim || col < 0 || col >= dim) {
                return false;
            }
        }

        // Se l'ArrayList contiene solo 1 carta, ritorna true (la regola è rispettata)
        if (coordinates.size() == 1) {
            return true;
        }

        // Se l'ArrayList contiene 2 o 3 carte, verifica se formano una retta
        Pair<Integer, Integer> coord1 = coordinates.get(0);
        Pair<Integer, Integer> coord2 = coordinates.get(1);

        // Controlla se le carte formano una retta in orizzontale o verticale
        boolean isHorizontalLine = true;
        boolean isVerticalLine = true;

        for (int i = 2; i < coordinates.size(); i++) {
            Pair<Integer, Integer> currentCoord = coordinates.get(i);

            if(!(selectables[currentCoord.getFirst()][currentCoord.getSecond()] && selectables[coord1.getFirst()][coord1.getSecond()] && selectables[coord2.getFirst()][coord2.getSecond()])){
                isVerticalLine = false;
                isVerticalLine = false;
            }
            if (!currentCoord.getFirst().equals(coord1.getFirst()) || !currentCoord.getFirst().equals(coord2.getFirst())) {
                isHorizontalLine = false;
            }
            if (!currentCoord.getSecond().equals(coord1.getSecond()) || !currentCoord.getSecond().equals(coord2.getSecond())) {
                isVerticalLine = false;
            }

        }
        return isHorizontalLine || isVerticalLine;
    }
    private boolean inRowTwo(Pair<Integer,Integer> coordA, Pair<Integer,Integer> coordB){
        int xA = coordA.getFirst();
        int yA = coordA.getSecond();
        int xB = coordB.getFirst();
        int yB = coordB.getSecond();

        if(xA == xB && (yA == yB+1 || yB == yA+1)){
            return true;
        }
        else return yA == yB && (xA == xB + 1 || xB == xA + 1);

    }
    private boolean inAdiacent(ArrayList<Pair<Integer, Integer>> coordinates){
        if (coordinates.size() == 1){
            return true;
        } else if (coordinates.size() == 2){
            return inRowTwo(coordinates.get(0),coordinates.get(1));
        } else if (coordinates.size() == 3){
            return inRowThree(coordinates.get(0),coordinates.get(1),coordinates.get(2));
        } else {
            return false;
        }
    }
    private boolean inRowThree(Pair<Integer,Integer> coordA, Pair<Integer,Integer> coordB, Pair<Integer,Integer> coordC){
        int xA = coordA.getFirst();
        int yA = coordA.getSecond();
        int xB = coordB.getFirst();
        int yB = coordB.getSecond();
        int xC = coordC.getFirst();
        int yC = coordC.getSecond();
        if(!((xA==xB && xB==xC) || (yA == yB && yB == yC)))
            return false;
        else {
            return inRowTwo(coordA,coordB) && inRowTwo(coordB,coordC);
        }
    }

    /**
     * Disables all buttons in the living room.
     * This method is executed on the JavaFX application thread using `Platform.runLater()`.
     * It iterates over each row of buttons in the living room and disables each button.
     */
    public void disableAllButtons() {
        Platform.runLater(() -> {
            for (Button[] row : livingRoomButtons) {
                for (Button button : row) {
                    button.setDisable(true);
                }
            }
        });
    }

}
