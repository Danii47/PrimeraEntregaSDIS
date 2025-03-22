package sdis.broker.client;

import sdis.broker.common.MalMensajeProtocoloException;
import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.Primitiva;

import java.io.*;
import java.net.Socket;

public class Cliente {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 47014;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            String username, password;
            MensajeProtocolo connectionMessage;

            // Mostrar el primer mensaje del servidor
            connectionMessage = (MensajeProtocolo) in.readObject();
            if (connectionMessage.getPrimitiva() != Primitiva.INFO || connectionMessage.getMensaje() == null)
                throw new MalMensajeProtocoloException("Error, primitiva INFO mal formada");

            System.out.println(connectionMessage.getMensaje());

            do {
                username = userInput.readLine();
                password = userInput.readLine();

                connectionMessage = new MensajeProtocolo(Primitiva.XAUTH, username, password);
                out.writeObject(connectionMessage);

                // Resolución del servidor
                connectionMessage = (MensajeProtocolo) in.readObject();
                System.out.println(connectionMessage.getMensaje());
            } while (connectionMessage.getPrimitiva() == Primitiva.NOAUTH);



        } catch (IOException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error en la clase: " + e.getMessage());
        }
    }
}
