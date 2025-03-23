package sdis.broker.common;


public enum Primitiva {
    INFO,       // S → C: El servidor da la bienvenida al usuario antes de iniciar interacción
    XAUTH,      // C → S: El cliente envía nombre de usuario y password para identificarse
                // S → C: Servidor confirma autenticación exitosa con mensaje de confirmación
    ADDMSG,     // C → S: Solicita añadir un mensaje a una cola, requiere autenticación USER/ADMIN
    ADDED,      // S → C: Confirma que se ha añadido el mensaje a la cola con éxito
    READQ,      // C → S: Cliente solicita consumir el siguiente elemento de una cola
    MSG,        // S → C: Servidor devuelve el elemento recuperado de la cola
    EMPTY,      // S → C: Indica que no es posible entregar un valor (cola vacía o inexistente)
    STATE,      // C → S: Cliente ADMIN solicita información administrativa del servidor
                // S → C: Servidor devuelve información administrativa solicitada
    DELETEQ,    // C → S: Cliente solicita borrar una cola, requiere permisos ADMIN
    DELETED,    // S → C: Confirma que se ha eliminado una cola con éxito
    NOTAUTH,    // S → C: Error de autenticación o permisos insuficientes
    ERROR,      // S → C: Error genérico del sistema (conexiones máximas, blacklist)
    BADCODE     // C → S: Cliente envía código incorrecto en el protocolo
                // S → C: Servidor indica que se recibió un intercambio ilegal de primitivas
}

