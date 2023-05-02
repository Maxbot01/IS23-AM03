package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameManagerController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.controller.pubSub.PubSubService;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.messageModel.GameManagerMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.LobbyMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.MatchStateMessage;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.view.CLIgeneral;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ClientMain implements Runnable {
    private Socket socket;

    public ClientMain(Socket socket) {
        //the client starts, lets set the pub/sub environment.
        this.socket = socket;
    }
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            // Leggo e scrivo nella connessione finche' non ricevo "quit"
            while (true) {
                String receivedMessage = in.nextLine();
                if (receivedMessage.equals("quit")) {
                    break;
                } else {
                    out.println("Received: " + receivedMessage);
                    //receives a json encoded message, decoding is needed
                    /*the received json message can be of types:
                    - CONNECTION 'acks and ping pongs ecc..' -> NetworkMessage
                    - MESSAGE 'big messages defined in the model' -> other message types
                     */
                    //TODO: decode messahe here and give it to a variable called receivedMessageDecoded: Message

                    Message receivedMessageDecoded;

                    out.flush();
                }
            }
            // Chiudo gli stream e il socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

