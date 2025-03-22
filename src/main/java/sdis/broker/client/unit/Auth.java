package sdis.broker.client.unit;

import sdis.broker.common.Primitiva;
import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.MalMensajeProtocoloException;

import java.net.Socket;

public class Auth {
    final static private int PORT = 47014;
    static java.io.ObjectInputStream ois = null;
    static java.io.ObjectOutputStream oos = null;

    public static void main(String[] args) throws java.io.IOException {
        if (args.length != 2) {
            System.out.println("Use:\njava sdis.broker.client.unit.Auth host mensaje");
            System.exit(-1);
        }


        String host = args[0];
        String mensaje = args[1];

        try (Socket sock = new Socket(host, PORT)) {
            oos = new java.io.ObjectOutputStream(sock.getOutputStream());
            ois = new java.io.ObjectInputStream(sock.getInputStream());
            pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.XAUTH, mensaje));
        } catch (java.io.EOFException e) {
            System.err.println("Cliente: Fin de conexión.");
        } catch (java.io.IOException e) {
            System.err.println("Cliente: Error de apertura o E/S sobre objetos: " + e);
        } catch (MalMensajeProtocoloException e) {
            System.err.println("Cliente: Error mensaje Protocolo: " + e);
        } catch (Exception e) {
            System.err.println("Cliente: Excepción. Cerrando Sockets: " + e);
        } finally {
            ois.close();
            oos.close();
        }
    }

    static void pruebaPeticionRespuesta(MensajeProtocolo mp) throws java.io.IOException, MalMensajeProtocoloException, ClassNotFoundException {
        System.out.println("> " + mp);
        oos.writeObject(mp);
        System.out.println("< " + (MensajeProtocolo) ois.readObject());
    }
}