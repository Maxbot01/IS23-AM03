package it.polimi.ingsw.view.GUIView;

import it.polimi.ingsw.client.ClientManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LoginView {
    private ScreenSwitcher screenSwitcher;
    private GridPane gridPane;

    public LoginView(ScreenSwitcher screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
        createContent();
    }

    public GridPane createContent() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25));

        Label titleLabel = new Label("Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            // If login is successful, switch to the GameManagerView
            System.out.println("sel user:"+usernameField.getText()+" psw: "+ passwordField.getText());
            ClientManager.gameManagerController.onSetCredentials(usernameField.getText(), passwordField.getText());

            //screenSwitcher.showGameManagerView();
        });

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(titleLabel, usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        vbox.setAlignment(Pos.CENTER);
        gridPane.add(vbox, 0, 0);

        return gridPane;
    }
}
