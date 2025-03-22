package sdis.broker.common;

public enum Primitiva {
    HELLO,          // mensaje de saludo
    PUSH,           // envío de mensaje a una cola
    PUSH_OK,        // respuesta afirmativa de PUSH
    PULL_WAIT,      // solicitud de obtener mensaje, esperando
    PULL_NOWAIT,    // solicitud de obtener mensaje, sin esperar
    PULL_OK,        // respuesta afirmativa de PULL
    NOTHING,        // cola vacía en respuesta a PULL_NOWAIT
    NOTUNDERSTAND;  // error de protocolo
}

