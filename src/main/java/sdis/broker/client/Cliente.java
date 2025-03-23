package sdis.broker.client;

import sdis.broker.common.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 12345;

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
            throw new WelcomeException("Mensaje de error del servidor: " + response.getMensaje());

        if (response.getPrimitiva() != Primitiva.INFO) {
            sendBadCode(response.getPrimitiva());
            throw new UnexpectedResponseException();
        }

        return response;
    }

    public MensajeProtocolo testRequestResponse(MensajeProtocolo request) throws IOException, MalMensajeProtocoloException, ClassNotFoundException {
        return switch (request.getPrimitiva()) {
            case XAUTH -> sendXAuth(request);
            case ADDMSG -> sendAddMsg(request);
            case READQ -> sendReadQ(request);
            case DELETEQ -> sendDeleteQ(request);
            case STATE -> sendState(request);
            default -> throw new IllegalArgumentException("Primitiva no válida: " + request.getPrimitiva());
        };
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

        if (response.getPrimitiva() != Primitiva.ADDMSG && response.getPrimitiva() != Primitiva.NOTAUTH) {
            sendBadCode(response.getPrimitiva());
            throw new UnexpectedResponseException();
        }

        return response;
    }

    public MensajeProtocolo sendReadQ(MensajeProtocolo request) throws IOException, ClassNotFoundException, UnexpectedResponseException {
        out.writeObject(request);

        MensajeProtocolo response = (MensajeProtocolo) in.readObject();

        if (response.getPrimitiva() != Primitiva.MSG && response.getPrimitiva() != Primitiva.EMPTY) {
            sendBadCode(response.getPrimitiva());
            throw new UnexpectedResponseException();
        }

        return response;
    }

    public MensajeProtocolo sendDeleteQ(MensajeProtocolo request) throws IOException, ClassNotFoundException {
        out.writeObject(request);

        MensajeProtocolo response = (MensajeProtocolo) in.readObject();

        if (response.getPrimitiva() != Primitiva.DELETED && response.getPrimitiva() != Primitiva.EMPTY && response.getPrimitiva() != Primitiva.NOTAUTH) {
            sendBadCode(response.getPrimitiva());
            throw new UnexpectedResponseException();
        }

        return response;
    }

    public MensajeProtocolo sendState(MensajeProtocolo request) throws IOException, ClassNotFoundException {
        out.writeObject(request);

        MensajeProtocolo response = (MensajeProtocolo) in.readObject();

        if (response.getPrimitiva() != Primitiva.STATE || response.getPrimitiva() != Primitiva.NOTAUTH) {
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
