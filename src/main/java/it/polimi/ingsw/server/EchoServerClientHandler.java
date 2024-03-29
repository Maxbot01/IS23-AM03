package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoServerClientHandler implements Runnable{
        private Socket socket;
    /**
     * Constructor of the socket
     * @param socket
     */
        public EchoServerClientHandler(Socket socket) {
            this.socket = socket;
        }
    /**
     * Opens the connection for in/out messages.
     * Closes it with the message "quit"
     */
        public void run() {
            try {
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                // Can read and write until I receive "quit"
                while (true) {
                    String line = in.nextLine();
                    if (line.equals("quit")) {
                        break;
                    } else {
                        out.println("Received: " + line);
                        out.flush();
                    }
                }
                // stream and socket are closed
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } }

}
