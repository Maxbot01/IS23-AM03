package it.polimi.ingsw.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setFullScreen(true);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("myshelfie/main_view.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
            stage.setFullScreen(false);
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        launch();
    }
}