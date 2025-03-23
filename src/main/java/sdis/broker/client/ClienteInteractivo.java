package sdis.broker.client;

import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.Primitiva;

import java.io.IOException;

public class ClienteInteractivo extends Cliente {
    public ClienteInteractivo() throws IOException {
        super();
    }

    public static void main(String[] args) {
        ClienteInteractivo client;

        try {
            client = new ClienteInteractivo();

            System.out.println("< " + client.waitWelcome().getMensaje());

            MensajeProtocolo request = new MensajeProtocolo(Primitiva.XAUTH, "admin", "$%&/()=");
            System.out.println("> " + request);
            MensajeProtocolo response = client.testRequestResponse(request);
            System.out.println("< " + response.toString());

        } catch (IOException e) {
            System.err.println("Error de E/S en los objetos.\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error al convertir el objeto recibido en MensajeProtocolo.\n" + e.getMessage());
        }
    }
}
