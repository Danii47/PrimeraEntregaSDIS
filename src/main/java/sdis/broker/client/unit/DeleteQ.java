package sdis.broker.client.unit;

import sdis.broker.client.Cliente;
import sdis.broker.common.Primitiva;
import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.MalMensajeProtocoloException;

import java.io.IOException;
import java.net.Socket;

public class DeleteQ extends Cliente {
    public static final Primitiva associatedPrimitive = Primitiva.DELETEQ;

    public DeleteQ() throws IOException {
        super();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Use:\njava sdis.broker.client.unit.DeleteQ nombre");
            System.exit(-1);
        }

        try {
            DeleteQ client = new DeleteQ();

            System.out.println("< " + client.waitWelcome().getMensaje());

            String name = args[0];

            MensajeProtocolo request = new MensajeProtocolo(associatedPrimitive, name);
            System.out.println("> " + request);
            MensajeProtocolo response = client.testRequestResponse(request);
            System.out.println("< " + response.toString());

        } catch (IOException e) {
            System.err.println("Error de E/S en los objetos.\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error al convertir el objeto recibido en MensajeProtocolo.\n" + e.getMessage());
        }
    }
}