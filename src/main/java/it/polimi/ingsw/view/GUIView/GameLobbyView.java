package it.polimi.ingsw.view.GUIView;

import it.polimi.ingsw.controller.client.ClientManager;
import it.polimi.ingsw.model.helpers.Pair;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * The GameLobbyView class represents the view of the game lobby.
 * It displays the list of players, chat messages, and provides controls for interacting with the lobby.
 */
public class GameLobbyView {
    private ScreenSwitcher screenSwitcher;
    private VBox lobbyView;

    private ArrayList<String> users;
    private ObservableList<Pair<String,Pair<String,String>>> chat;
    private ListView<Pair<String,Pair<String,String>>> chatView;
    private VBox playersContainer;
    private ComboBox<String> receiverBox;
    private boolean chatFirstInitialization;
    private String host;
    private String chatDestination;

    /**
     * Constructs a GameLobbyView object with the specified ScreenSwitcher.
     *
     * @param screenSwitcher the ScreenSwitcher object used for switching between screens
     */
    public GameLobbyView(ScreenSwitcher screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
        chat = FXCollections.observableArrayList();
    }

    /**
     * Returns the host of the game.
     *
     * @return the host of the game
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Adds a new player to the lobby.
     *
     * @param newPlayer the name of the new player
     */
    public void addNewPlayerToLobby(String newPlayer) {
        Platform.runLater(() -> {
            Label newPlayerLabel = new Label(newPlayer);
            newPlayerLabel.setStyle("-fx-font-size: 13px;");
            newPlayerLabel.setTextFill(Color.WHITE);
            this.playersContainer.getChildren().add(newPlayerLabel);
            this.receiverBox.getItems().add(newPlayer);
        });
    }

    /**
     * Creates the content of the game lobby view.
     *
     * @param users  the list of players in the lobby
     * @param host   the host of the game
     * @param isHost true if the local player is the host, false otherwise
     * @return the root VBox of the game lobby view
     */
    public VBox createContent(ArrayList<String> users, String host, boolean isHost) {
        this.users = users;
        this.host = host;
        this.chatFirstInitialization = true;
        this.chatDestination = null;

        //Start of my lobby idea section

        //Root
        VBox rootMY = new VBox();
        //rootMY.setPadding(new Insets(30));
        rootMY.setSpacing(20);
        //rootMY.setStyle("-fx-background-image:url(misc/sfondo_parquet.jpg)");

        InputStream imageStream;
        imageStream = this.getClass().getResourceAsStream("/misc/sfondo_parquet.jpg");
        BackgroundImage rootBackground = new BackgroundImage(new Image(imageStream),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
        rootMY.setBackground(new Background(rootBackground));

        Region fillerVBox1 = new Region();
        VBox.setVgrow(fillerVBox1, Priority.ALWAYS);
        Region fillerVBox2 = new Region();
        VBox.setVgrow(fillerVBox2, Priority.ALWAYS);
        Region fillerVBox3 = new Region();
        VBox.setVgrow(fillerVBox3, Priority.ALWAYS);
        Region fillerVBox4 = new Region();
        VBox.setVgrow(fillerVBox4, Priority.ALWAYS);
        Region fillerHBox1 = new Region();
        HBox.setHgrow(fillerHBox1, Priority.ALWAYS);
        Region fillerHBox2 = new Region();
        HBox.setHgrow(fillerHBox2, Priority.ALWAYS);
        Region fillerHBox3 = new Region();
        HBox.setHgrow(fillerHBox3, Priority.ALWAYS);
        Region fillerHBox4 = new Region();
        HBox.setHgrow(fillerHBox4, Priority.ALWAYS);
        Region fillerHBox5 = new Region();
        HBox.setHgrow(fillerHBox5, Priority.ALWAYS);
        Region fillerHBox6 = new Region();
        HBox.setHgrow(fillerHBox6, Priority.ALWAYS);


        //Title
        HBox titleBox = new HBox();
        Label titleLabel = new Label("LOBBY");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-font-style: italic; -fx-font-family: 'Curlz MT'");
        titleBox.getChildren().addAll(fillerHBox1,titleLabel,fillerHBox2);

        //GameId and Host section
        VBox firstBox = new VBox();
        HBox gameIdBox = new HBox();
        HBox hostBox = new HBox();
        Label gameIdTitleLabel = new Label("Game ID: ");
        Label gameIdLabel = new Label(ClientManager.lobbyController.getID());
        gameIdTitleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        gameIdLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        gameIdLabel.setTextFill(Color.LIGHTBLUE);
        //gameIdLabel.setAlignment(Pos.TOP_LEFT);
        Label hostTitleLabel = new Label("Host: ");
        Label hostLabel = new Label(host);
        hostTitleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        hostLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        hostLabel.setTextFill(Color.ORANGE);
        //hostLabel.setAlignment(Pos.TOP_RIGHT);
        //firstLine.setAlignment(Pos.TOP_RIGHT);TODO: FUNZIONAVA
        gameIdBox.getChildren().addAll(gameIdTitleLabel,gameIdLabel);
        hostBox.getChildren().addAll(hostTitleLabel,hostLabel);
        firstBox.getChildren().addAll(gameIdBox,hostBox);

    //List and Start Match Section
        HBox middleContainer = new HBox();


        //List section
        this.playersContainer = new VBox();
        Label listLabel = new Label("Players");
        listLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        playersContainer.getChildren().add(listLabel);
        for (String user : users) {
            Label userLabel = new Label(user);
            userLabel.setStyle("-fx-font-size: 13px");
            userLabel.setTextFill(Color.WHITE);
            playersContainer.getChildren().add(userLabel);
        }

        //Buttons section
        VBox buttonsContainer = new VBox();
        Button startMatchButton = new Button("Start Match");
        Button readyButton = new Button("Ready");
        startMatchButton.setOnAction(event -> {
            ClientManager.lobbyController.onStartMatch(ClientManager.lobbyController.getID(),host);
        });
        readyButton.setOnAction(e -> {
            ClientManager.gameController.setReady(ClientManager.lobbyController.getID(),ClientManager.userNickname);
        });
        if(!isHost){
            startMatchButton.setDisable(true);
        }else{
            readyButton.setDisable(true);
        }
        buttonsContainer.getChildren().addAll(fillerVBox1,startMatchButton,fillerVBox2,readyButton,fillerVBox4);
        //VBox for both firstline and players
        VBox sxMidContainer = new VBox();
        sxMidContainer.getChildren().addAll(firstBox,playersContainer);

        middleContainer.getChildren().addAll(fillerHBox3,sxMidContainer,fillerHBox4,buttonsContainer,fillerHBox5);

        //Chat/lastLine section
        VBox chatSection = new VBox();
        Label chatTitle = new Label(" Chat ");
        chatTitle.setStyle("-fx-background-color: rgb(255,224,199);-fx-font-size: 14px;");
        chatTitle.setPadding(new Insets(3));
        //Input and Receiver Section
        HBox inputChatSection = new HBox();
        //ComboBox
        this.receiverBox = new ComboBox<>();
        receiverBox.getItems().add("All");
        for(String s: users) {
            if(!s.equals(ClientManager.userNickname)){
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
        HBox.setHgrow(chatMessage,Priority.ALWAYS);
        ScrollPane scrollBox = new ScrollPane();
        chatView = new ListView<>(chat);
        chatView.setCellFactory(createChatCellFactory());
        scrollBox.setContent(chatView); // apply "this." in case of scrollbar-at-bottom code
        scrollBox.setFitToHeight(true); // apply "this." in case of scrollbar-at-bottom code
        scrollBox.setFitToWidth(true); // apply "this." in case of scrollbar-at-bottom code
        //This background set refers only to the listview, we also need a style set for the single cell
        chatView.setStyle("-fx-background-color: rgb(229,163,91)"); //LOOK TO THE LEFT, see that square, you can adjust the color from there

        /* Code useful for VBox/Label implementation of chat
        chatMessage.setStyle("-fx-background-color: rgb(233,233,235);\" +\n" +"\"-fx-background-radius: 20px");
        */
        inputChatSection.getChildren().addAll(chatMessage,receiverBox,chatButton);
        chatSection.getChildren().addAll(chatTitle,scrollBox,inputChatSection);

        //Chat first initialization, so that it already appears full of the messages sent before
        ClientManager.lobbyController.onGetChat(true);
        /* Code maybe useful for setting scrollBar at bottom
        scrollBox.setVvalue(1D);
        chatView.heightProperty().addListener(observable -> scrollBox.setVvalue(1.0));
        */


        chatButton.setOnAction(event -> {
            if(!chatMessage.getText().isEmpty()) {
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
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING,"Before clicking on the 'Send' button, please insert a message");
                alert.setTitle("Invalid Action");
                alert.setHeaderText("No chat input");
                alert.showAndWait();
            }
        });
        //Root
        rootMY.getChildren().addAll(titleBox,middleContainer,fillerVBox3,chatSection);
        return rootMY;
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
    /**
     * Updates the chat messages in the lobby view.
     *
     * @param chatMessages the list of chat messages to update
     */
    public void updateChatMessages(ArrayList<Pair<String,Pair<String,String>>> chatMessages){
        Platform.runLater(()->{
            if(chatFirstInitialization){
                // Adds all messages
                this.chat.addAll(chatMessages);
                this.chatFirstInitialization = false;
            }else{
                // Adds only the last message
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

    private String getChatString(Pair<String,Pair<String,String>> tmp){
        String s = null;
        if(tmp.getFirst().equals("All")){
            s = "[Public]  " + tmp.getSecond().getFirst() + ":  " + tmp.getSecond().getSecond();
        } else if (tmp.getFirst().equals(ClientManager.userNickname)) {
            s = "[Private]  " + tmp.getSecond().getFirst() + ":  " + tmp.getSecond().getSecond();
        }
        return s;
    }
    /**
     * Displays an error message in the lobby view.
     *
     * @param errorMessage the error message to display
     */
    public void showError(String errorMessage){
        Platform.runLater(() -> {
            Alert alert;
            if(errorMessage.equals("The game has started\nEnter the game with the command \"ready\"")){
                alert = new Alert(Alert.AlertType.INFORMATION, errorMessage, ButtonType.OK);
            }else {
                alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
            }
            alert.showAndWait();
        });
    }

}
