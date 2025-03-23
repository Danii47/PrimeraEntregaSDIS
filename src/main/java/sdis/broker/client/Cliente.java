package sdis.broker.client;

import sdis.broker.common.*;
import sdis.broker.common.customexceptions.MalMensajeProtocoloException;
import sdis.broker.common.customexceptions.UnexpectedResponseException;
import sdis.broker.common.customexceptions.WelcomeException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 47014;

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public Cliente() throws IOException {
        socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public MensajeProtocolo waitWelcome() throws IOException, ClassNotFoundException {
        MensajeProtocolo response = (MensajeProtocolo) in.readObject();

        if (response == null) {
            sendBadCode(null);
            throw new WelcomeException("Mensaje de bienvenida NO recibido.");
        }

        if (response.getPrimitiva() == Primitiva.ERROR)
            throw new WelcomeException(response.getMensaje());

        if (response.getPrimitiva() != Primitiva.INFO) {
            sendBadCode(response.getPrimitiva());
            throw new UnexpectedResponseException();
        }

        return response;
    }

    public MensajeProtocolo testRequestResponse(MensajeProtocolo request) throws IOException, MalMensajeProtocoloException, ClassNotFoundException {
        switch (request.getPrimitiva()) {
            case XAUTH:
                return sendXAuth(request);
            case ADDMSG:
                return sendAddMsg(request);
            case READQ:
                return sendReadQ(request);
            case DELETEQ:
                return sendDeleteQ(request);
            case STATE:
                return sendState(request);
            default:
                throw new IllegalArgumentException("Primitiva no válida: " + request.getPrimitiva());
        }
    }

    public MensajeProtocolo sendXAuth(MensajeProtocolo request) throws IOException, ClassNotFoundException, UnexpectedResponseException {
        out.writeObject(request);

        MensajeProtocolo response = (MensajeProtocolo) in.readObject();

        if (response.getPrimitiva() != Primitiva.XAUTH && response.getPrimitiva() != Primitiva.NOTAUTH && response.getPrimitiva() != Primitiva.ERROR) {
            sendBadCode(response.getPrimitiva());
            throw new UnexpectedResponseException();
        }

        return response;
    }

    public MensajeProtocolo sendAddMsg(MensajeProtocolo request) throws IOException, ClassNotFoundException, UnexpectedResponseException {
        out.writeObject(request);

        MensajeProtocolo response = (MensajeProtocolo) in.readObject();

        if (response.getPrimitiva() != Primitiva.ADDED && response.getPrimitiva() != Primitiva.NOTAUTH && response.getPrimitiva() != Primitiva.ERROR) {
            sendBadCode(response.getPrimitiva());
            throw new UnexpectedResponseException();
        }

        return response;
    }

    public MensajeProtocolo sendReadQ(MensajeProtocolo request) throws IOException, ClassNotFoundException, UnexpectedResponseException {
        out.writeObject(request);

        MensajeProtocolo response = (MensajeProtocolo) in.readObject();

        if (response.getPrimitiva() != Primitiva.MSG && response.getPrimitiva() != Primitiva.EMPTY && response.getPrimitiva() != Primitiva.NOTAUTH && response.getPrimitiva() != Primitiva.ERROR) {
            sendBadCode(response.getPrimitiva());
            throw new UnexpectedResponseException();
        }

        return response;
    }

    public MensajeProtocolo sendDeleteQ(MensajeProtocolo request) throws IOException, ClassNotFoundException {
        out.writeObject(request);

        MensajeProtocolo response = (MensajeProtocolo) in.readObject();

        if (response.getPrimitiva() != Primitiva.DELETED && response.getPrimitiva() != Primitiva.EMPTY && response.getPrimitiva() != Primitiva.NOTAUTH && response.getPrimitiva() != Primitiva.ERROR) {
            sendBadCode(response.getPrimitiva());
            throw new UnexpectedResponseException();
        }

        return response;
    }

    public MensajeProtocolo sendState(MensajeProtocolo request) throws IOException, ClassNotFoundException {
        out.writeObject(request);

        MensajeProtocolo response = (MensajeProtocolo) in.readObject();

        if (response.getPrimitiva() != Primitiva.STATE && response.getPrimitiva() != Primitiva.NOTAUTH && response.getPrimitiva() != Primitiva.ERROR) {
            sendBadCode(response.getPrimitiva());
            throw new UnexpectedResponseException();
        }

        return response;
    }

    public void sendBadCode(Primitiva receivePrimitive) {
        MensajeProtocolo badCodeResponse = new MensajeProtocolo(Primitiva.BADCODE, "Mensaje no esperado, recibido: " + ((receivePrimitive == null) ? "null" : receivePrimitive.toString()));

        try {
            out.writeObject(badCodeResponse);
        } catch (IOException e) {
            System.out.println("Ocurrió un error al enviar el BadCode al servidor");
        }
    }
}
