package sdis.broker.common;

public class UnexpectedResponseException extends RuntimeException {
    public UnexpectedResponseException() {
        super("Respuesta inesperada. BADCODE enviado.");
    }
}
