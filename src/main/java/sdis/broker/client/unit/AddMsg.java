package sdis.broker.client.unit;

import sdis.broker.client.Cliente;
import sdis.broker.common.Primitiva;
import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.MalMensajeProtocoloException;

import java.io.IOException;
import java.net.Socket;

public class AddMsg extends Cliente {
    public static final Primitiva associatedPrimitive = Primitiva.ADDMSG;

    public AddMsg() throws IOException {
        super();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Use:\njava sdis.broker.client.unit.AddMsg nombre mensaje");
            System.exit(-1);
        }

        try {
            AddMsg client = new AddMsg();

            System.out.println("< " + client.waitWelcome().toString());

            String name = args[0];
            String message = args[1];

            MensajeProtocolo request = new MensajeProtocolo(associatedPrimitive, name, message);
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