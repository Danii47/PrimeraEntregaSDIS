package sdis.broker.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    private static final int PORT = 47014;
    private static final int MAX_CLIENTS = 5;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            BlacklistManager connectionManager = new BlacklistManager(3);
            BlacklistManager failsLoginManager = new BlacklistManager(2);
            System.out.println("----Server Waiting For Client----");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                if (connectionManager.isBanned(clientSocket.getInetAddress().getHostAddress())) {
                    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                        out.println("Err Max Number of login attempts reached.");
                    } catch (IOException e) {
                        System.err.println("Error while sending message to banned client: " + e.getMessage());
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            System.err.println("Error while closing socket: " + e.getMessage());
                        }
                    }
                    continue;
                }

//                Thread t = new Thread(new Sirviente(clientSocket, connectionManager, failsLoginManager), "ClientHandler");
//                t.start();
                executor.execute(new Sirviente(clientSocket, connectionManager, failsLoginManager));
            }
        } catch (IOException e) {
            System.err.println("Cerrando socket de cliente");
            e.printStackTrace(System.err);
        }
    }
}
