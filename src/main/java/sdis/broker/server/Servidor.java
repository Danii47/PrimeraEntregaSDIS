package sdis.broker.server;

import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.Primitiva;
import sdis.broker.common.Strings;
import sdis.utils.MultiMap;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    private static final int PORT = 12345;
    private static final int MAX_CLIENTS = 5;

    public static ConcurrentHashMap<String, User> initAuthStorage() {
        ConcurrentHashMap<String, User> authStorage = new ConcurrentHashMap<>();

        authStorage.put("cllamas", new User("cllamas", "qwerty", Status.USER));
        authStorage.put("hector", new User("hector", "lkjlkj", Status.USER));
        authStorage.put("sdis", new User("sdis", "987123", Status.USER));
        authStorage.put("admin", new User("admin", "$%&/()=", Status.ADMIN));

        return authStorage;
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);
        MultiMap<String, String> messageQueueMap = new MultiMap<>();
        BlacklistManager connectionManager = new BlacklistManager(3);
        BlacklistManager failsLoginManager = new BlacklistManager(2);
        ConcurrentHashMap<String, User> authStorage = initAuthStorage();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("----Server Started----");

            Thread mainServer = new Thread(() -> {
                String clientIP;
                Socket clientSocket;
                Sirviente sirviente;

                try {
                    while (true) {
                        System.out.println("----Server Waiting For Client----");

                        clientSocket = serverSocket.accept();
                        clientIP = clientSocket.getInetAddress().getHostAddress();

                        try {
                            int connectionCounter = connectionManager.registerAttempt(clientIP);
                            System.out.println("[BM] (connections) for IP /" + clientIP + " = " + connectionCounter);

                            sirviente = new Sirviente(clientSocket, authStorage, messageQueueMap, connectionManager, failsLoginManager);
                            executor.execute(sirviente);

                        } catch (IllegalStateException e) {
                            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                            MensajeProtocolo response = new MensajeProtocolo(Primitiva.ERROR, Strings.MAX_CONNECTIONS);
                            out.writeObject(response);

                            clientSocket.close();

                            System.out.println("[BM] (connections) for IP /" + clientIP + " exceeded maximum attempts. Error sent and connection closed.");
                        }

                    }
                } catch (IOException e) {
                    System.err.println("Server: Error accepting client connection " + e.getMessage());
                }
            }, "MainServerThread");

            mainServer.start();
            mainServer.join();
        } catch (IOException e) {
            System.err.println("Cerrando socket de cliente");
            e.printStackTrace(System.err);
        } catch (InterruptedException e) {
            System.err.println("Cerrando socket de cliente");
        }
    }
}
