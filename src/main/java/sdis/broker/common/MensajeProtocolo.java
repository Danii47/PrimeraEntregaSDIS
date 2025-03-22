package sdis.broker.common;

import java.io.Serializable;

public class MensajeProtocolo implements Serializable {
    private final Primitiva primitiva;
    private String idCola;
    private final String mensaje;

    public MensajeProtocolo(Primitiva p, String idCola, String mensaje) throws MalMensajeProtocoloException {
        if (p == Primitiva.PUSH) {
            this.primitiva = p;
            this.mensaje = mensaje;
            this.idCola = idCola;
        } else {
            throw new MalMensajeProtocoloException("error");
        }
    }


    public MensajeProtocolo(Primitiva p, String mensaje) throws MalMensajeProtocoloException {
        if (p == Primitiva.HELLO || p == Primitiva.PULL_OK) {
            this.idCola = null;
            this.mensaje = mensaje;

        } else if (p == Primitiva.PULL_WAIT || p == Primitiva.PULL_NOWAIT) {
            this.idCola = mensaje;
            this.mensaje = null;
        } else {
            throw new MalMensajeProtocoloException("error");
        }
        this.primitiva = p;
    }


    public MensajeProtocolo(Primitiva p) throws MalMensajeProtocoloException {
        if (p == Primitiva.PUSH_OK || p == Primitiva.NOTUNDERSTAND || p == Primitiva.NOTHING) {
            this.primitiva = p;
            this.mensaje = null;
            this.idCola = null;

        } else {
            throw new MalMensajeProtocoloException("error");
        }
    }


    public Primitiva getPrimitiva() {
        return primitiva;
    }

    public String getIdCola() {
        return idCola;
    }

    public String getMensaje() {
        return mensaje;
    }
}
