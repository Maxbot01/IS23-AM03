module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires commons.cli;
    requires java.desktop;
    requires com.google.gson;


    opens it.polimi.ingsw.fx to javafx.fxml, javafx.controls, javafx.graphics;
    exports it.polimi.ingsw.fx;

}