package it.polimi.ingsw.server;

import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.MyRemoteInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This class manages the server socket, is multi threaded to handle multiple clients and messages
 */
public class ServerMain {

    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    public MyRemoteInterface stubProva;

    public static HashMap<String, RemoteUserInfo> userIdentificationInServer = new HashMap<>();

    private boolean isRunning;

    /**
     * ServerMain Constructor. Creates the serverPort at the indicated port, and the ArrayList of clients
     * @param port
     */
    public ServerMain(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
            isRunning = true;
        } catch (IOException e) {
            System.out.println("Error creating server socket: " + e.getMessage());
            isRunning = false;
        }
    }

    /**
     * Adds a new player to the RemoteUser HashMap
     * @param username
     * @param remoteUserInfo
     */
    public static void addUserToHashMap(String username, RemoteUserInfo remoteUserInfo){
        userIdentificationInServer.put(username, remoteUserInfo);
    }

    /**
     * Getter for the RemoteUser HashMap
     * @return userIdentificationInServer, HashMap with name, userinfo
     */
    public static HashMap<String, RemoteUserInfo> getUserIdentification(){
        return userIdentificationInServer;
    }

    /**
     * Sends a message to the client correspondent socket
     * @param message
     * @param socket
     */
    public void sendMessageToSocket(String message, Socket socket){
        for (ClientHandler client : clients) {
            if(client.socket.equals(socket)){
                //right socket to send message to
                client.sendMessage(message);
            }
        }
    }
    /**
     * Sends a message in BroadCast
     * @param message
     */
    public void broadcastMessageSocket(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    /**
     * Turns on the Server, waiting for connections
     */
    public void start() {
        while (isRunning) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler client = new ClientHandler(clientSocket);
                clients.add(client);
                client.start();
            } catch (IOException e) {
                System.out.println("Error accepting client connection: " + e.getMessage());
            }
        }
    }

    /**
     * Switch off the Server
     */
    public void stop() {
        isRunning = false;
        try {
            serverSocket.close();
            for (ClientHandler client : clients) {
                client.interrupt();
            }
        } catch (IOException e) {
            System.out.println("Error stopping server: " + e.getMessage());
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private ObjectOutputStream output;
        private ObjectInputStream input;
        private boolean isRunning;

        /**
         * Constructor of the ClientHandler
         * @param socket
         */
        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                isRunning = true;
            } catch (IOException e) {
                System.out.println("Error creating client I/O streams: " + e.getMessage());
                isRunning = false;
            }
        }

        /**
         * Sends the String message to the Client
         * @param message
         */
        public void sendMessage(String message) {
            try {
                output.writeObject(message);
                output.flush();
            } catch (IOException e) {
                System.out.println("Error sending message to client: " + e.getMessage());
                isRunning = false;
            }
        }

        /**
         * Accepts messages from the Client
         */
        public void receiveMessages() {
            try {
                while (isRunning) {
                    String message = (String) input.readObject();
                    System.out.println("Received message from client " + socket.getInetAddress().getHostAddress() + ": " + message);
                    VirtualGameManagerSerializer.deserializeMethod(message, socket, null);
                    //broadcastMessage("Client " + socket.getInetAddress().getHostAddress() + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("Error receiving message from client " + socket.getInetAddress().getHostAddress() + ": " + e.getMessage());
                isRunning = false;
            } catch (ClassNotFoundException e) {
                System.out.println("Error parsing message from client " + socket.getInetAddress().getHostAddress() + ": " + e.getMessage());
                isRunning = false;
            }
        }

        /**
         * Shuts down the input/output traffic
         */
        public void stopServer() {
            isRunning = false;
            try {
                output.close();
                input.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Error stopping client handler for client " + socket.getInetAddress().getHostAddress() + ": " + e.getMessage());
            }
        }

        private void broadcastMessage(String message) {
            for (ClientHandler client : clients) {
                if (client != this) {
                    client.sendMessage(message);
                }
            }
        }

        @Override
        public void run() {
            receiveMessages();
        }
    }

    public static ServerMain server;

    public static void main(String[] args) {

        //per socket:
        int port = 8089;
        int portRMI = 1919;
        server = new ServerMain(port);
        System.out.println("Starting server socket on port " + port);
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            String serverIP = localAddress.getHostAddress();
            System.out.println("Server IP: " + serverIP);
        } catch (UnknownHostException e) {
            System.out.println("Error retrieving server IP: " + e.getMessage());
        }
        //per rmi:
        //System.setProperty("java.rmi.server.hostname", args[0]);
        //System.out.println("ip: " +  args[0]);
        GameManager obj = GameManager.getInstance();
        try {
            MyRemoteInterface stub = (MyRemoteInterface) UnicastRemoteObject.exportObject(obj, portRMI);
            Registry registry = null;
            registry = LocateRegistry.createRegistry(portRMI);
            registry.bind("MyRemoteInterface", stub);
            System.out.println("Server RMI ready");

        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        server.start();
    }

}

    /*
    private int port;
    public ServerMain(int port) {
        this.port = port;
    }
    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            return; }
        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new EchoServerClientHandler(socket));
            } catch(IOException e) {
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }


    private void decodeAndCall(){
        //GameManager.getInstance().setCredentials();
    }


    public void sendMessage(Message message, String toPlayer, String ID) throws IOException {
        String json = new MessageSerializer().serialize(message, toPlayer, ID);
        oos.writeObject(json);
        //o.close();
    }

    private static ObjectOutputStream oos;


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //create the socket server object
        //static ServerSocket variable

        //server classes init

        ServerSocket server;
        //socket server port on which it will listen
        int port = 1234;
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        while(true){
            System.out.println("Waiting for the client request");
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
            String message = (String) ois.readObject();
            System.out.println("Message Received: " + message);
            //create ObjectOutputStream object
            oos = new ObjectOutputStream(socket.getOutputStream());
            //write object to Socket
            //oos.writeObject("client sent "+ message);
            VirtualGameManagerSerializer.deserializeMethod(message);
            //close resources
            ois.close();
            oos.close();
            socket.close();
            //terminate the server if client sends exit request
            if(message.equalsIgnoreCase("exit")) break;
        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        server.close();
    } }


    */
