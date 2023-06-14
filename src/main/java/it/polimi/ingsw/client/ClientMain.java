package it.polimi.ingsw.client;

import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.server.MyRemoteInterface;
import it.polimi.ingsw.server.MyRemoteObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.UUID;

public class ClientMain implements Runnable{

    private Socket socket;
    private static ObjectOutputStream output;
    private ObjectInputStream input;
    private static boolean isRunning;
    static boolean isSocketClient;
    private MyRemoteInterface remoteObject;


    public ClientMain(Socket socket, boolean isCLi, boolean isSocketClient, MyRemoteInterface remoteObject) {
        if(isSocketClient) {
            this.socket = socket;

            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                isRunning = true;
            } catch (IOException e) {
                System.out.println("Error creating client I/O streams: " + e.getMessage());
                isRunning = false;
            }
        } else {
            this.socket = null;
            this.input = null;
            this.output = null;
            this.remoteObject = remoteObject;
            isRunning = true;
        }

        System.out.println(isSocketClient);
        ClientManager.initializeClientManagerSingleton(isCLi, isSocketClient, remoteObject);
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
                        try {
                            ClientManager.clientReceiveMessage(serializedMessage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
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
    public void runRMI() {
       try {
           System.out.println("Server pronto.");
           Message message = (Message) input.readObject();
           System.out.println("Received message from server: " + message);
        } catch (Exception e) {
            System.out.println("Error receiving message from server: " + e.getMessage());
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
        Scanner scanner = new Scanner(System.in);

        //System.out.print("Seleziona il tipo di connessione (socket/rmi): ");
        String connectionType;

        boolean isCLI = true;  // Imposta a true o false a seconda delle tue esigenze
        ClientMain client;
        do { //Added in case the client inserts a wrong string
            System.out.print("Seleziona il tipo di connessione (socket/rmi): ");
            connectionType = scanner.nextLine();
            if (connectionType.equalsIgnoreCase("socket")) {
                System.out.println("Socket mode selected.");
                Socket socket = new Socket("localhost", 1234);
                client = new ClientMain(socket, isCLI, true, null);
                client.run();
                client.stop();
                break;
            } else if (connectionType.equalsIgnoreCase("rmi")) {
                System.out.println("RMI mode selected.");
                MyRemoteInterface remoteObj = new MyRemoteObject();
                Registry registry = LocateRegistry.createRegistry(1098);
                registry.rebind("MyRemoteObject", remoteObj);
                client = new ClientMain(null, isCLI, false, remoteObj);
                client.runRMI();
                client.stop();
                break;
            } else {
                System.out.println("Tipo di connessione non valido.");
            }
        } while(true);

    }
}




