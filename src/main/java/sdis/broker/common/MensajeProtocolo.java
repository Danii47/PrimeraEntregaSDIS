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
        this.
        validarNumeroParametros();
    }

    /**
     * Constructor que inicializa un mensaje con una primitiva y dos parámetros.
     *
     * @param primitiva la primitiva del protocolo
     * @param parametro1 el primer parámetro del mensaje
     * @param parametro2 el segundo parámetro del mensaje
     */
    public MensajeProtocolo(Primitiva primitiva, String mensaje, String idCola) {
        this.primitiva = primitiva;
        this.parametros = new String[]{parametro1, parametro2};
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
