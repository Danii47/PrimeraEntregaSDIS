package sdis.broker.server;

import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.Primitiva;
import sdis.broker.common.Strings;
import sdis.utils.MultiMap;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class Sirviente implements Runnable {
    private final ThreadPoolExecutor executor;
    private final Socket clientSocket;
    private final ConcurrentHashMap<String, User> authStorage;
    private final MultiMap<String, String> messageQueueMap;
    private final BlacklistManager connectionsManager;
    private final BlacklistManager failsLoginManager;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final AtomicInteger uploadMessages;
    private final AtomicInteger downloadMessages;
    private User loggedUser;

    public Sirviente(ThreadPoolExecutor executor, Socket socket, ConcurrentHashMap<String, User> authStorage, MultiMap<String, String> messageQueueMap, BlacklistManager connectionsManager, BlacklistManager failsLoginManager, AtomicInteger uploadMessages, AtomicInteger downloadMessages) throws IOException {
        this.executor = executor;
        this.clientSocket = socket;
        this.authStorage = authStorage;
        this.messageQueueMap = messageQueueMap;
        this.connectionsManager = connectionsManager;
        this.failsLoginManager = failsLoginManager;
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.uploadMessages = uploadMessages;
        this.downloadMessages = downloadMessages;
        this.loggedUser = null;
    }

    public String getClientIP() {
        return clientSocket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {
        int connectionCounter;
        MensajeProtocolo request, response;

        try {
            response = new MensajeProtocolo(Primitiva.INFO);
            out.writeObject(response);

            while (true) {
                request = (MensajeProtocolo) in.readObject();
                response = getResponse(request);

                out.writeObject(response);
            }

        } catch (IOException e) {
            System.err.println(Strings.CLOSED_CONNECTION);
        } catch (ClassNotFoundException e) {
            System.err.println(Strings.CLASS_NOT_FOUND_MESSAGE);
        } finally {
            connectionCounter = connectionsManager.unregisterAttempt(getClientIP());
            System.out.println(Strings.NEW_CONNECTION(getClientIP(), connectionCounter));
        }
    }

    public MensajeProtocolo getResponse(MensajeProtocolo request) {
        return switch (request.getPrimitiva()) {
            case XAUTH -> replyXAuth(request);
            case ADDMSG -> replyAddMsg(request);
            case READQ -> replyReadQ(request);
            case STATE -> replyState();
            case DELETEQ -> replyDeleteQ(request);
            default -> replyBadCode(request);
        };
    }

    public MensajeProtocolo replyXAuth(MensajeProtocolo request) {
        if (failsLoginManager.isBanned(getClientIP()))
            return new MensajeProtocolo(Primitiva.ERROR, Strings.MAX_LOGIN_ATTEMPTS);

        if (!authStorage.containsKey(request.getIdCola()) || !authStorage.get(request.getIdCola()).getPassword().equals(request.getMensaje())) {
            int actualAttempts = failsLoginManager.registerAttempt(getClientIP());
            System.out.println(Strings.LOGIN_FAILS(getClientIP(), actualAttempts));

            return new MensajeProtocolo(Primitiva.NOTAUTH, Strings.INVALID_CREDENTIALS);
        }

        loggedUser = authStorage.get(request.getIdCola());
        return new MensajeProtocolo(Primitiva.XAUTH, Strings.LOGGED);
    }

    public MensajeProtocolo replyAddMsg(MensajeProtocolo request) {
        if (loggedUser == null)
            return new MensajeProtocolo(Primitiva.NOTAUTH, Strings.NOT_LOGGED);

        if (request.getIdCola() == null || request.getMensaje() == null)
            return new MensajeProtocolo(Primitiva.NOTAUTH, Strings.NOT_LOGGED);

        if (!messageQueueMap.containsKey(request.getIdCola()) && loggedUser.getStatus() != Status.ADMIN)
            return new MensajeProtocolo(Primitiva.NOTAUTH, Strings.NOT_ADMIN);

        uploadMessages.incrementAndGet();
        messageQueueMap.push(request.getIdCola(), request.getMensaje());
        return new MensajeProtocolo(Primitiva.ADDED);
    }

    public MensajeProtocolo replyReadQ(MensajeProtocolo request) {
        if (loggedUser == null)
            return new MensajeProtocolo(Primitiva.NOTAUTH, Strings.NOT_LOGGED);

        if (!messageQueueMap.containsKey(request.getIdCola()) || messageQueueMap.isEmpty(request.getIdCola()))
            return new MensajeProtocolo(Primitiva.EMPTY);

        downloadMessages.incrementAndGet();
        return new MensajeProtocolo(Primitiva.MSG, messageQueueMap.pop(request.getIdCola()));
    }

    public MensajeProtocolo replyState() {
        if (loggedUser == null)
            return new MensajeProtocolo(Primitiva.NOTAUTH, Strings.NOT_LOGGED);

        if (loggedUser.getStatus() != Status.ADMIN)
            return new MensajeProtocolo(Primitiva.NOTAUTH, Strings.NOT_ADMIN);

        String information = executor.getMaximumPoolSize() + " : " + executor.getActiveCount() + " : " + uploadMessages.get() + " : " + downloadMessages.get();
        return new MensajeProtocolo(Primitiva.STATE, information);
    }

    public MensajeProtocolo replyDeleteQ(MensajeProtocolo request) {
        if (loggedUser == null)
            return new MensajeProtocolo(Primitiva.NOTAUTH, Strings.NOT_LOGGED);

        if (loggedUser.getStatus() != Status.ADMIN)
            return new MensajeProtocolo(Primitiva.NOTAUTH, Strings.NOT_ADMIN);

        if (!messageQueueMap.containsKey(request.getIdCola()))
            return new MensajeProtocolo(Primitiva.EMPTY);

        messageQueueMap.remove(request.getIdCola());
        return new MensajeProtocolo(Primitiva.DELETED);
    }

    public MensajeProtocolo replyBadCode(MensajeProtocolo request) {
        return new MensajeProtocolo(Primitiva.BADCODE, Strings.UNEXPECTED_MESSAGE(request.getPrimitiva()));
    }
}
