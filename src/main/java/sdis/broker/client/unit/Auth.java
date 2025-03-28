package sdis.broker.client.unit;

import sdis.broker.client.Cliente;
import sdis.broker.common.Primitiva;
import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.customexceptions.WelcomeException;

import java.io.IOException;

public class Auth extends Cliente {
    public static final Primitiva associatedPrimitive = Primitiva.XAUTH;

    public Auth() throws IOException {
        super();
    }

    // user :string, pass :string
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Use:\njava sdis.broker.client.unit.Auth usuario contraseña");
            System.exit(-1);
        }

        try {
            Auth client = new Auth();

            System.out.println("< " + client.waitWelcome().toString());

            String user = args[0];
            String password = args[1];

            MensajeProtocolo request = new MensajeProtocolo(associatedPrimitive, user, password);
            System.out.println("> " + request);
            MensajeProtocolo response = client.testRequestResponse(request);
            System.out.println("< " + response.toString());

        } catch (WelcomeException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de E/S en los objetos.\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error al convertir el objeto recibido en MensajeProtocolo.\n" + e.getMessage());
        }
    }
}