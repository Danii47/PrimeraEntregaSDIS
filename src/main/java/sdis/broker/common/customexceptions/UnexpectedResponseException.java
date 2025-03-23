package sdis.broker.common.customexceptions;

public class UnexpectedResponseException extends RuntimeException {
    public UnexpectedResponseException() {
        super("Respuesta inesperada. BADCODE enviado.");
    }
}
