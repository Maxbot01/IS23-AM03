package it.polimi.ingsw.view.GUIView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyView {
    private ScreenSwitcher screenSwitcher;
    private VBox lobbyView;

    private ArrayList<String> users;
    private String host;

    public GameLobbyView(ScreenSwitcher screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
    }


    public Scene updateUsers(String newU){
        users.add(newU);
        return createContent(this.users, host, false);
    }
    public Scene createContent(ArrayList<String> users, String host, boolean isHost) {
        this.users = users;
        this.host = host;
        lobbyView = new VBox();
        lobbyView.setSpacing(10);
        lobbyView.setPadding(new Insets(20));
        lobbyView.setAlignment(Pos.CENTER);

        // Create and add the user labels to the lobby view
        for (String user : users) {
            Label userLabel = new Label(user);
            if (user.equals(host)) {
                userLabel.setTextFill(Color.color(0.4, 0.4, 0.8));
            }
            lobbyView.getChildren().add(userLabel);
        }

        // Create the start match button
        Button startMatchButton = new Button("Start Match");
        startMatchButton.setOnAction(event -> {
            // Call a method in ScreenSwitcher to start the match
            // screenSwitcher.startMatch();
        });

        // Disable the start match button initially
        startMatchButton.setDisable(true);

        // Add the start match button to the lobby view
        lobbyView.getChildren().add(startMatchButton);

        // Create the scene
        Scene scene = new Scene(lobbyView, 700, 600);

        if (isHost) {
            enableStartMatchButton();
        }

        return scene;
    }

    public void enableStartMatchButton() {
        // Enable the start match button
        Button startMatchButton = (Button) lobbyView.getChildren().get(lobbyView.getChildren().size() - 1);
        startMatchButton.setDisable(false);
    }
}
