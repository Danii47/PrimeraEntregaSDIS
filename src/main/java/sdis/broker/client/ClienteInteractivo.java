package sdis.broker.client;

import sdis.broker.common.customexceptions.MalMensajeProtocoloException;
import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.Primitiva;
import sdis.broker.common.customexceptions.WelcomeException;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClienteInteractivo extends Cliente {
    private static final String INPUT_SYMBOL = "< ";
    private static final String OUTPUT_SYMBOL = "> ";
    private static final String SPLIT_PATTERN = " ";

    public ClienteInteractivo() throws IOException {
        super();
    }

    public static void main(String[] args) {
        ClienteInteractivo client;
        MensajeProtocolo request, response;
        Scanner scanner = new Scanner(System.in);
        String[] requestStrings;

        try {
            client = new ClienteInteractivo();

            System.out.println(INPUT_SYMBOL + client.waitWelcome().toString());

//            ---- HARDCODE ---- HARDCODE ---- HARDCODE ---- HARDCODE ---- HARDCODE ----
//            request = new MensajeProtocolo(Primitiva.XAUTH, "admin", "$%&/()=");
//            System.out.println(OUTPUT_SYMBOL + request);
//            response = client.testRequestResponse(request);
//            System.out.println(INPUT_SYMBOL + response.toString());

            while (!(requestStrings = scanner.nextLine().split(SPLIT_PATTERN))[0].equals("exit")) {


                if (requestStrings.length > 3) {
                    System.err.println("Número inválido de argumentos");
                    continue;
                }

                try {
                    if (requestStrings.length == 1)
                        request = new MensajeProtocolo(Primitiva.valueOf(requestStrings[0].toUpperCase()));
                    else if (requestStrings.length == 2)
                        request = new MensajeProtocolo(Primitiva.valueOf(requestStrings[0].toUpperCase()), requestStrings[1]);
                    else
                        request = new MensajeProtocolo(Primitiva.valueOf(requestStrings[0].toUpperCase()), requestStrings[1], requestStrings[2]);

                    System.out.println(OUTPUT_SYMBOL + request);
                    response = client.testRequestResponse(request);
                    System.out.println(INPUT_SYMBOL + response.toString());
                } catch (IllegalArgumentException e) {
                    System.err.println("Primitiva no válida");
                } catch (MalMensajeProtocoloException e) {
                    System.err.println("Mensaje protocolo mal formado");
                }
            }

        } catch (IOException e) {
            System.err.println("Conexión con el servidor perdida.\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error al convertir el objeto recibido en MensajeProtocolo.\n" + e.getMessage());
        } catch (WelcomeException e) {
            System.err.println(e.getMessage());
        } catch (NoSuchElementException e) {
            System.err.println("Saliendo del cliente CTRL-C...");
        }
    }
}
