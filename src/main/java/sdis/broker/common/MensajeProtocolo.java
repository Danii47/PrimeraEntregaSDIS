package sdis.broker.common;

import sdis.broker.common.customexceptions.MalMensajeProtocoloException;

import java.io.Serializable;

public class MensajeProtocolo implements Serializable {
    private final Primitiva primitiva;
    private final String mensaje;
    private final String idCola;

    // STATE, INFO, ADDED, DELETED y EMPTY
    public MensajeProtocolo(Primitiva primitiva) throws MalMensajeProtocoloException {

        if (primitiva == null)
            throw new MalMensajeProtocoloException("Primitiva no puede ser null");

        if (!ClientPrimitives.PRIMITIVES_0_ARGS.contains(primitiva) && !ServidorPrimitives.PRIMITIVES_0_ARGS.contains(primitiva))
            throw new MalMensajeProtocoloException("Primitiva " + primitiva + " no es válida");


        this.primitiva = primitiva;
        this.idCola = null;
        this.mensaje = null;
    }

    // READQ, DELETEQ, BADCODE, XAUTH, MSG, BADCODE, STATE y NOTAUTH, ERROR
    public MensajeProtocolo(Primitiva primitiva, String mensaje) throws MalMensajeProtocoloException {

        if (primitiva == null)
            throw new MalMensajeProtocoloException("Primitiva no puede ser null");

        if (mensaje == null)
            throw new MalMensajeProtocoloException("Mensaje no puede ser null");

        if (!ClientPrimitives.PRIMITIVES_1_ARGS.contains(primitiva) && !ServidorPrimitives.PRIMITIVES_1_ARGS.contains(primitiva))
            throw new MalMensajeProtocoloException("Primitiva " + primitiva + " no es válida");

        this.primitiva = primitiva;

        if (primitiva == Primitiva.XAUTH || primitiva == Primitiva.MSG || primitiva == Primitiva.STATE || primitiva == Primitiva.NOTAUTH || primitiva == Primitiva.ERROR || primitiva == Primitiva.BADCODE) {
            this.idCola = null;
            this.mensaje = mensaje;
        } else {
            this.idCola = mensaje;
            this.mensaje = null;
        }
    }

    // XAUTH y ADDMSG
    public MensajeProtocolo(Primitiva primitiva, String idCola, String mensaje) throws MalMensajeProtocoloException {

        if (primitiva == null)
            throw new MalMensajeProtocoloException("Primitiva no puede ser null");

        if (mensaje == null)
            throw new MalMensajeProtocoloException("Mensaje no puede ser null");

        if (idCola == null)
            throw new MalMensajeProtocoloException("idCola no puede ser null");

        if (!ClientPrimitives.PRIMITIVES_2_ARGS.contains(primitiva))
            throw new MalMensajeProtocoloException("Primitiva " + primitiva + " no es válida");


        this.primitiva = primitiva;
        this.mensaje = mensaje;
        this.idCola = idCola;
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

    @Override
    public String toString() {
        switch (primitiva) {
            case MSG:
            case STATE:
            case NOTAUTH:
            case ERROR:
            case BADCODE:
                return primitiva + ":" + mensaje;
            case READQ:
            case DELETEQ:
                return primitiva + ":" + idCola;
            case XAUTH:
            case ADDMSG:
                return primitiva + ":" + idCola + ":" + mensaje;
            default:
                return this.primitiva.toString();
        }
    }
}
