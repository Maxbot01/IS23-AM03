package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.model.GameManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
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
                executor.submit(new ClientMain(socket));
            } catch(IOException e) {
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }


    private void decodeAndCall(){
        //GameManager.getInstance().setCredentials();
    }

    public static void main(String[] args) {
        ServerMain echoServer = new ServerMain(1234);
        echoServer.startServer();
    } }