package sdis.broker.client.unit;

import sdis.broker.client.Cliente;
import sdis.broker.common.Primitiva;
import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.customexceptions.WelcomeException;

import java.io.IOException;

public class ReadQ extends Cliente {
    public static final Primitiva associatedPrimitive = Primitiva.READQ;

    public ReadQ() throws IOException {
        super();
    }

    // key :string
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Use:\njava sdis.broker.client.unit.ReadQ clave");
            System.exit(-1);
        }

        try {
            ReadQ client = new ReadQ();

            System.out.println("< " + client.waitWelcome().toString());

            String key = args[0];

            MensajeProtocolo request = new MensajeProtocolo(associatedPrimitive, key);
            System.out.println("> " + request);
            MensajeProtocolo response = client.testRequestResponse(request);
            System.out.println("< " + response.toString());

        } catch (IOException e) {
            System.err.println("Error de E/S en los objetos.\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error al convertir el objeto recibido en MensajeProtocolo.\n" + e.getMessage());
        } catch (WelcomeException e) {
            System.err.println(e.getMessage());
        }
    }
}