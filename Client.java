package CHATWORLD;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Connected to chat server");

            // Start a new thread to listen for messages from the server
            new Thread(new ReceiveMessages()).start();

            // Read messages from the console and send to server
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                out.println(message);
            }
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    private class ReceiveMessages implements Runnable {
        @Override
        public void run() {
            String receivedMessage;
            try {
                while ((receivedMessage = in.readLine()) != null) {
                    System.out.println("Server: " + receivedMessage);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server");
            }
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
