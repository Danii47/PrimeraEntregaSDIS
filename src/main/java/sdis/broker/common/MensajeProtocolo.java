package sdis.broker.common;

import java.io.Serializable;

public class MensajeProtocolo implements Serializable {
    private final Primitiva primitiva;
    private String idCola;
    private final String mensaje;

    public MensajeProtocolo(Primitiva primitiva, String idCola, String mensaje) {
        this.primitiva = primitiva;
        this.idCola = idCola;
        this.mensaje = mensaje;
    }

    public MensajeProtocolo(Primitiva primitiva, String mensaje) {
        this.primitiva = primitiva;
        this.mensaje = mensaje;
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
