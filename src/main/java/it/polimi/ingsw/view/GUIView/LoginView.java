package it.polimi.ingsw.view.GUIView;

import it.polimi.ingsw.controller.client.ClientManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.InputStream;

import static it.polimi.ingsw.controller.client.ClientMain.stub;

/**
 * The LoginView class represents the view for the login screen.
 */
public class LoginView {
    private ScreenSwitcher screenSwitcher;
    private StackPane rootPane;
    private ImageView goalImageView; // ImageView per l'immagine dei personal goals

    /**
     * Constructs a LoginView object.
     *
     * @param screenSwitcher the screen switcher object to switch between views
     */
    public LoginView(ScreenSwitcher screenSwitcher){
        this.screenSwitcher = screenSwitcher;
        createContent();
    }

    /**
     * Creates the content of the login view.
     *
     * @return the root pane containing the login view content
     */
    public StackPane createContent(){
        rootPane = new StackPane();
        rootPane.setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(25));

        Label titleLabel = new Label("My Shelfie");
        titleLabel.setStyle("-fx-font-size: 60px; -fx-font-family: 'Curlz MT'; -fx-text-fill: WHITE");

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-family: 'Curlz MT'; -fx-font-size: 30px");
        usernameLabel.setTextFill(Color.WHITE);
        TextField usernameField = new TextField();

        InputStream imageStream = getClass().getResourceAsStream("/Publisher_material/Display_5.jpg");
        Image backgroundImage = new Image(imageStream);
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(rootPane.heightProperty());
        rootPane.getChildren().add(backgroundImageView);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-family: 'Curlz MT'; -fx-font-size: 30px");
        passwordLabel.setTextFill(Color.WHITE);
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-style: italic");
        loginButton.setOnAction(e -> {
            // If login is successful, switch to the GameManagerView
            if (usernameField.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input: Please enter a username.", ButtonType.OK);
                alert.setHeaderText("Username Error");
                alert.showAndWait();
            } else if (passwordField.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input: Please enter a password.", ButtonType.OK);
                alert.setHeaderText("Password Error");
                alert.showAndWait();
            } else {
                ClientManager.gameManagerController.onSetCredentials(usernameField.getText(), passwordField.getText(), stub);
            }
            //screenSwitcher.showGameManagerView();
        });

        Region fillerVBox1 = new Region();
        VBox.setVgrow(fillerVBox1, Priority.ALWAYS);
        Region fillerVBox2 = new Region();
        VBox.setVgrow(fillerVBox2, Priority.ALWAYS);
        Region fillerVBox3 = new Region();
        VBox.setVgrow(fillerVBox3, Priority.ALWAYS);

        VBox vbox = new VBox(10);
        titleLabel.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().addAll(titleLabel, fillerVBox1, fillerVBox2, fillerVBox3, usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        vbox.setAlignment(Pos.CENTER);
        gridPane.getChildren().add(vbox);

        rootPane.getChildren().add(gridPane);

        return rootPane;
    }

    /**
     * Displays an error message.
     *
     * @param errorMessage the error message to be displayed
     */
    public void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
        alert.showAndWait();
    }
}
