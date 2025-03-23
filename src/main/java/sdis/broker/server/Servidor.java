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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class Servidor {
    private static final int DEFAULT_PORT = 47014;
    private static final int MAX_CLIENTS = 5;
    private static final int MAX_CONNECTIONS = 3;
    private static final int MAX_LOGIN_ATTEMPTS = 2;

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
        BlacklistManager connectionManager = new BlacklistManager(MAX_CONNECTIONS);
        BlacklistManager failsLoginManager = new BlacklistManager(MAX_LOGIN_ATTEMPTS);
        ConcurrentHashMap<String, User> authStorage = initAuthStorage();
        AtomicInteger uploadMessages = new AtomicInteger();
        AtomicInteger downloadMessages = new AtomicInteger();

        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            System.out.println(Strings.SERVER_STARTED);

            Thread mainServer = new Thread(() -> {
                String clientIP;
                Socket clientSocket;
                Sirviente sirviente;

                try {
                    while (true) {
                        System.out.println(Strings.SERVER_WAITING_CLIENT);

                        clientSocket = serverSocket.accept();
                        clientIP = clientSocket.getInetAddress().getHostAddress();

                        try {
                            int connectionCounter = connectionManager.registerAttempt(clientIP);
                            System.out.println(Strings.NEW_CONNECTION(clientIP, connectionCounter));

                            sirviente = new Sirviente((ThreadPoolExecutor) executor, clientSocket, authStorage, messageQueueMap, connectionManager, failsLoginManager, uploadMessages, downloadMessages);
                            executor.execute(sirviente);

                        } catch (IllegalStateException e) {
                            try {
                                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                                MensajeProtocolo response = new MensajeProtocolo(Primitiva.ERROR, Strings.MAX_CONNECTIONS);
                                out.writeObject(response);

                            } catch (IOException error) {
                                System.out.println("Error E/S al enviar error.");
                            } finally {
                                clientSocket.close();
                                System.out.println(Strings.MAX_CONNECTIONS_REACHED(clientIP));
                            }
                        }

                    }
                } catch (IOException e) {
                    System.err.println("Server: Error accepting client connection " + e.getMessage());
                }
            }, "MainServerThread");

            mainServer.start();
            mainServer.join();
        } catch (IOException | InterruptedException e) {
            System.err.println(Strings.CLOSING_SOCKET);
        }
    }
}
