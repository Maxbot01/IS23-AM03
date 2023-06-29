package it.polimi.ingsw.view.GUIView;

import it.polimi.ingsw.controller.client.ClienRMIObject;
import it.polimi.ingsw.controller.client.ClienRMIObjectInterface;
import it.polimi.ingsw.controller.client.ClientMain;
import it.polimi.ingsw.model.MyRemoteInterface;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

import static it.polimi.ingsw.controller.client.ClientMain.setStub;

/**
 * The ConnectionView class represents the view for the connection settings.
 */
public class ConnectionView {
    private ScreenSwitcher screenSwitcher;
    private GridPane gridPane;
    private String ipAddress;

    /**
     * Constructs a ConnectionView object.
     *
     * @param screenSwitcher the ScreenSwitcher object used for switching between screens
     */
    public ConnectionView(ScreenSwitcher screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
        this.ipAddress = "";
        createContent();
    }

    /**
     * Creates the content for the connection view.
     *
     * @return the GridPane representing the connection view content
     */
    public GridPane createContent() {

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(25));
        int port = 8089;
        int portRMI = 1919;
        Label titleLabel = new Label("Connection Settings");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-family: 'Arial'; -fx-text-fill: BLACK");

        Label ipAddressLabel = new Label("IP Address:");
        ipAddressLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial'");
        TextField ipAddressField = new TextField();
        ipAddressField.setPrefWidth(200);

        //RMI MODE
        Button rmiButton = new Button("RMI");
        rmiButton.setStyle("-fx-font-size: 16px; -fx-font-family: 'Arial'");
        rmiButton.setOnAction(e -> {
            ipAddress = ipAddressField.getText(); // Updating ipAddress variable

            try {
                ClientMain.clienRMIInstance = new ClienRMIObject();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }


            Registry registry = null;
            try {
                registry = LocateRegistry.getRegistry(ipAddress, portRMI);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            MyRemoteInterface stub = null;

            try {
                stub = (MyRemoteInterface) registry.lookup("MyRemoteInterface");
            } catch (NotBoundException | RemoteException ex) {
                throw new RuntimeException(ex);
            }

            setStub(stub);
            String ipAddress = UUID.randomUUID().toString();


            try {
                stub.registerClient(ipAddress);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            new ClientMain(null, false, false, stub, ipAddress);
        });

        //SOCKET MODE
        Button socketButton = new Button("Socket");
        socketButton.setStyle("-fx-font-size: 16px; -fx-font-family: 'Arial'");
        socketButton.setOnAction(e -> {
            ipAddress = ipAddressField.getText(); // Aggiornamento della variabile ipAddress
            new Thread(() -> {
                try {
                    Socket socket = new Socket(ipAddress, port);
                    ClientMain client = new ClientMain(socket, false, true, null, null);
                    client.run();
                    client.stop();
                } catch (IOException ex) {
                    System.out.println("Error connecting to server: " + ex.getMessage());
                }
            }).start();
        });

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(titleLabel, ipAddressLabel, ipAddressField, rmiButton, socketButton);
        vbox.setAlignment(Pos.CENTER);
        gridPane.add(vbox, 0, 0);

        return gridPane;
    }

}
