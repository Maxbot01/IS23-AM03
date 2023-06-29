package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.App;
import it.polimi.ingsw.model.MyRemoteInterface;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.view.GUIView.ScreenSwitcher;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.UUID;

/**
 * The main class for the client-side of the game.
 */
public class ClientMain implements Runnable {

    private Socket socket;
    private static ObjectOutputStream output;
    private ObjectInputStream input;
    private static boolean isRunning;
    public static boolean isSocketClient;
    public static MyRemoteInterface stub;

    public static ClienRMIObjectInterface clienRMIInstance;

    /**
     * Constructor for the ClientMain class.
     *
     * @param socket          The socket for socket-based communication.
     * @param isCLi           Flag indicating whether the client is a CLI client or not.
     * @param isSocketClient  Flag indicating whether the client is using socket-based communication or RMI.
     * @param remoteObject    The remote object for RMI-based communication.
     * @param ipAddr          The IP address of the server.
     */
    public ClientMain(Socket socket, boolean isCLi, boolean isSocketClient, MyRemoteInterface remoteObject, String ipAddr) {
        if (isSocketClient) {
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
            this.stub = remoteObject;
            isRunning = true;
        }

        System.out.println(isSocketClient);
        ClientManager.initializeClientManagerSingleton(isCLi, isSocketClient, remoteObject, ipAddr);
    }

    /**
     * Sends a message to the server.
     *
     * @param message The message to send.
     */
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
            while (isRunning) {
                String message = (String) input.readObject();
                //System.out.println("Received message from server: " + message);
                MessageSerializer messageSerializer = new MessageSerializer();
                Message serializedMessage = messageSerializer.deserialize(message);
                if (serializedMessage != null) {
                    Thread newThread = new Thread(() -> {
                        //System.out.println("New thread created: " + Thread.currentThread().getName());
                        try {
                            ClientManager.clientReceiveMessage(serializedMessage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    newThread.start();
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

    /**
     * Stops the client.
     */
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

    /**
     * Sets the remote object stub.
     *
     * @param stub The remote object stub.
     */
    public static void setStub(MyRemoteInterface stub) {
        ClientMain.stub = stub;
    }

    /**
     * Runs the RMI-based communication with the server.
     *
     * @param ipAddress The IP address of the server.
     */
    public static void runRMI(String ipAddress) {
        try {
            while (isRunning) {
                Thread newThread = new Thread(() -> {
                    Message messageRMI = null;
                    try {
                        messageRMI = stub.ReceiveMessageRMI(ipAddress);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        if (stub.getFlag()) {
                            try {
                                ClientManager.clientReceiveMessage(messageRMI);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
                newThread.start();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Error receiving message from server: ");
        isRunning = false;
    }

    /**
     * The main method of the client.
     *
     * @param args The command line arguments.
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        int port = 8089;
        int portRMI = 1919;

        String connectionType;

        boolean isCLI = true;
        ClientMain client;

        String interfaceType;

        do {
            System.out.print("Select your interface (CLI/GUI): ");
            Scanner scan1 = new Scanner(System.in);
            interfaceType = scan1.next();
            if (interfaceType.equals("GUI")) {
                App.main(null);
            } else if (interfaceType.equals("CLI")) {
                do {
                    System.out.print("Select the type of connection (socket/rmi): ");
                    Scanner scan2 = new Scanner(System.in);
                    connectionType = scan2.nextLine();
                    if (connectionType.equalsIgnoreCase("socket")) {
                        isSocketClient = true;
                        System.out.println("Socket mode selected.");
                        System.out.print("Enter the server IP address: ");
                        String serverIP = scan2.nextLine();
                        Socket socket = new Socket(serverIP, port);
                        client = new ClientMain(socket, isCLI, true, null, null);
                        client.run();
                        client.stop();
                        break;
                    } else if (connectionType.equalsIgnoreCase("rmi")) {
                        System.out.println("RMI mode selected.");
                        isSocketClient = false;

                        System.out.print("Enter the server IP address: ");
                        String serverIP = scan2.nextLine();

                        clienRMIInstance = new ClienRMIObject();

                        Registry registry = LocateRegistry.getRegistry(serverIP, portRMI);
                        MyRemoteInterface stub = null;

                        try {
                            stub = (MyRemoteInterface) registry.lookup("MyRemoteInterface");
                        } catch (NotBoundException e) {
                            throw new RuntimeException(e);
                        }

                        setStub(stub);

                        String ipAddress = UUID.randomUUID().toString();
                        stub.registerClient(ipAddress);
                        new ClientMain(null, isCLI, false, stub, ipAddress);
                        return;
                    } else {
                        System.out.println("Invalid connection type.");
                    }
                } while (true);
            }

        } while (true);
    }

}


