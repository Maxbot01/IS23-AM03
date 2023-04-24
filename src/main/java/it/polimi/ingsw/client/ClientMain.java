package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.pubSub.PubSubService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ClientMain implements Runnable {
    private Socket socket;
    private static PubSubService pubsub;

    public ClientMain(Socket socket) {
        //the client starts, lets set the pub/sub environment.
        this.socket = socket;
        pubsub = new PubSubService();
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

