package it.polimi.ingsw.client;

import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ClientMain implements Runnable {
    private Socket socket;

    public ClientMain(Socket socket) {
        //the client starts, lets set the pub/sub environment.
        this.socket = socket;
    }

    //private playerNickname;

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
                    //TODO: decode message here and give it to a variable called receivedMessageDecoded: Message
                    //TODO: BE SURE TO RECEIVE MESSAGES FOR THIS CLIENT:
                    //check if right user only if it's not NetworkMessage
                    Message receivedMessageDecoded = new MessageSerializer().deserialize(receivedMessage);
                    if(receivedMessageDecoded.getClass() != NetworkMessage.class){

                    }
                    ClientManager.clientReceiveMessage(receivedMessageDecoded);
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

