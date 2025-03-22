package sdis.broker.server;

import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.Primitiva;

import java.io.*;
import java.net.Socket;

public class Sirviente implements Runnable {
    private final Socket clientSocket;
    private final BlacklistManager connectionsManager;
    private final BlacklistManager failsLoginManager;

    public Sirviente(Socket socket, BlacklistManager connectionsManager, BlacklistManager failsLoginManager) {
        this.clientSocket = socket;
        this.connectionsManager = connectionsManager;
        this.failsLoginManager = failsLoginManager;
    }

    public String getClientIP() {
        return clientSocket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {
        int failsCounter;
        int connectionCounter = connectionsManager.registerAttempt(getClientIP());
        System.out.println("[BM] (connections) for IP /" + getClientIP() + " = " + connectionCounter);

        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            MensajeProtocolo connectionMessage;

            connectionMessage = new MensajeProtocolo(Primitiva.INFO, "Welcome, please type your credentials to █LOG in");
            out.writeObject(connectionMessage);

            do {
                connectionMessage = (MensajeProtocolo) in.readObject();

                if (!AuthStorage.isValidUser(connectionMessage.getIdCola(), connectionMessage.getMensaje())) {
                    if (failsLoginManager.isBanned(getClientIP())) {
                        out.writeObject("Err Max Number of login attempts reached.");
                        clientSocket.close();
                    } else {
                        connectionMessage = new MensajeProtocolo(Primitiva.NOAUTH, "Err 401 ~ Credentials DO NOT MATCH. Try again");
                        out.writeObject(connectionMessage);
                        failsCounter = failsLoginManager.registerAttempt(getClientIP());
                        System.out.println("[BM] (login fails) for IP /" + getClientIP() + " = " + failsCounter);
                    }
                }
            } while (!AuthStorage.isValidUser(connectionMessage.getIdCola(), connectionMessage.getMensaje()));

            connectionMessage = new MensajeProtocolo(Primitiva.XAUTH, "User successfully logged");
            out.writeObject(connectionMessage);



        } catch (IOException e) {
            System.err.println("Error con el cliente: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error en la clase: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar socket: " + e.getMessage());
            } finally {
                connectionCounter = connectionsManager.unregisterAttempt(getClientIP());
                System.out.println("[BM] (connections) for IP /" + getClientIP() + " = " + connectionCounter);
            }
        }
    }
}
