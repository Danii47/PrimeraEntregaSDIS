package sdis.broker.client.unit;

import sdis.broker.client.Cliente;
import sdis.broker.common.Primitiva;
import sdis.broker.common.MensajeProtocolo;

import java.io.IOException;

public class AuthDeleteQ extends Cliente {
    public static final Primitiva[] associatedPrimitives = {Primitiva.XAUTH, Primitiva.DELETEQ};

    public AuthDeleteQ() throws IOException {
        super();
    }

    // user :string, pass :string
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Use:\njava sdis.broker.client.unit.AuthDeleteQ usuario contraseña idCola");
            System.exit(-1);
        }

        MensajeProtocolo request, response;

        try {
            AuthDeleteQ client = new AuthDeleteQ();

            System.out.println("< " + client.waitWelcome().toString());

            String user = args[0];
            String password = args[1];
            String queueId = args[2];

            request = new MensajeProtocolo(associatedPrimitives[0], user, password);
            System.out.println("> " + request);
            response = client.testRequestResponse(request);
            System.out.println("< " + response.toString());

            request = new MensajeProtocolo(associatedPrimitives[1], queueId);
            System.out.println("> " + request);
            response = client.testRequestResponse(request);
            System.out.println("< " + response.toString());

        } catch (IOException e) {
            System.err.println("Error de E/S en los objetos.\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error al convertir el objeto recibido en MensajeProtocolo.\n" + e.getMessage());
        }
    }
}