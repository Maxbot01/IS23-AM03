module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires com.google.gson;
    requires java.management.rmi;
    requires commons.cli;


    opens it.polimi.ingsw.fx to javafx.fxml, javafx.controls, javafx.graphics;
    exports it.polimi.ingsw.fx;

}