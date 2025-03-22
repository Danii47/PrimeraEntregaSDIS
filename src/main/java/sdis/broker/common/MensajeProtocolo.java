package sdis.broker.common;

import java.io.Serializable;

public class MensajeProtocolo implements Serializable {
    private final Primitiva primitiva;
    private final String idCola;
    private final String mensaje;

    /**
     * Constructor que inicializa un mensaje con una primitiva y sin parámetros.
     *
     * @param primitiva la primitiva del protocolo
     */
    public MensajeProtocolo(Primitiva primitiva) {
        this.primitiva = primitiva;
        this.idCola = getIdCola();
        this.mensaje = getMensaje();
        validarNumeroParametros();
    }

    /**
     * Constructor que inicializa un mensaje con una primitiva y un parámetro.
     *
     * @param primitiva la primitiva del protocolo
     * @param idCola el parámetro del mensaje
     */
    public MensajeProtocolo(Primitiva primitiva, String idCola) {
        this.primitiva = primitiva;
        this.idCola = idCola;
        validarNumeroParametros();
    }

    /**
     * Constructor que inicializa un mensaje con una primitiva y dos parámetros.
     *
     * @param primitiva la primitiva del protocolo
     * @param mensaje el primer parámetro del mensaje
     * @param idCola el segundo parámetro del mensaje
     */
    public MensajeProtocolo(Primitiva primitiva, String mensaje, String idCola) {
        this.primitiva = primitiva;
        this.mensaje = mensaje;
        this.idCola = idCola;
        validarNumeroParametros();
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
