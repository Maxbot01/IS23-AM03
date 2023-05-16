package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.GameManagerController;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorType;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.server.ServerMain;
import it.polimi.ingsw.view.CLIgeneral;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import static it.polimi.ingsw.client.VirtualGameManagerSerializer.serializeMethod;
import static java.lang.System.out;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientMain implements Runnable{

    private Socket socket;
    private static ObjectOutputStream output;
    private ObjectInputStream input;
    private static boolean isRunning;

    public ClientMain(Socket socket, boolean isCLi){
        this.socket = socket;

        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            isRunning = true;
        } catch (IOException e) {
            System.out.println("Error creating client I/O streams: " + e.getMessage());
            isRunning = false;
        }
        ClientManager.userUID = UUID.randomUUID().toString();
        ClientManager cl = new ClientManager(isCLi);
        ClientManager.isSocket = true;
    }

    public static void sendMessage(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.out.println("Error sending message to server: " + e.getMessage());
            isRunning = false;
        }
    }

    public void run() {
        try {
            Thread previousThread = null;

            while (isRunning) {
                String message = (String) input.readObject();
                System.out.println("Received message from server: " + message);
                MessageSerializer messageSerializer = new MessageSerializer();
                Message serializedMessage = messageSerializer.deserialize(message);
                if(serializedMessage != null){
                    //if it's meant for us
                    //TODO: add exception to handle wrongly received message to react accordingly
                    //TODO: put this into thread to stop cli from blocking this loop
                    if(previousThread != null){
                        previousThread.interrupt();
                    }
                    Thread newThread = new Thread(() -> {
                        ClientManager.clientReceiveMessage(serializedMessage);
                    });
                    newThread.start();
                    previousThread = newThread;
                }
            }
        } catch (IOException e) {
            System.out.println("Error receiving message from server: " + e.getMessage());
            isRunning = false;
        } catch (ClassNotFoundException e) {
            System.out.println("Error parsing message from server: " + e.getMessage());
            isRunning = false;
        }
    }

    public void stop() {
        isRunning = false;
        try {
            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error stopping client: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        //CLIgeneral cli = new CLIgeneral();
        Socket socket = new Socket("localhost", 1234);
        ClientMain client = new ClientMain(socket, true);

// Send a message to the server
        //client.sendMessage("Hello, server!");

// Receive messages from the server until the client is stopped
        client.run();

// Stop the client
        client.stop();
    }
}




