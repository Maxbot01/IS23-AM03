package it.polimi.ingsw.client;

import it.polimi.ingsw.model.messageModel.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

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

        ClientManager.initializeClientManagerSingleton(isCLi);
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
            //Thread previousThread = null;

            /*Thread commandThread = new Thread(()->{//Added input thread for cli
               ClientManager.startReceivingCommands();
            });
            commandThread.start();*/
            while (isRunning) {
                String message = (String) input.readObject();
                System.out.println("Received message from server: " + message);
                MessageSerializer messageSerializer = new MessageSerializer();
                Message serializedMessage = messageSerializer.deserialize(message);
                if(serializedMessage != null){
                    //if it's meant for us
                    //TODO: add exception to handle wrongly received message to react accordingly
                    //TODO: put this into thread to stop cli from blocking this loop
                    /*if(previousThread != null){
                        previousThread.interrupt();
                    }*/

                    Thread newThread = new Thread(() -> { //This is the execution thread
                        System.out.println("New thread created: "+Thread.currentThread().getName());//DEBUG
                        ClientManager.clientReceiveMessage(serializedMessage);
                    });
                    newThread.start();
                    //previousThread = newThread;
                }
            }
            //commandThread.interrupt(); relative to the cliInput thread
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
        client.run();

        client.stop();
    }
}




