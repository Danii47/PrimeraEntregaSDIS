package sdis.broker.common;

public enum Primitiva {
    INFO,       // Mensaje del servidor que espera el cliente para empezar la interacción
    XAUTH,      // Mensaje de identificación del cliente y respuesta positiva / negativa del servidor
    ADDMSG,     // Solicitud de añadir un mensaje a una cola una vez loggeado con éxito
    ADDED,      // El servidor confirma la acción
    READQ,      // El cliente solicita el siguiente elemento de la cola;
                // el servidor responde con MSG si hay datos o EMPTY si la cola no existe o está vacía
    MSG,        // El servidor envía al cliente un String recuperado de la cola
    EMPTY,      // El servidor indica que no hay un valor disponible,
                // ya sea porque la lista está vacía o no existe
    STATE,      // Un usuario ADMIN solicita información del servidor;
                // el servidor responde con STATE y los datos si tiene permisos, o con NOTAUTH si no
    DELETEQ,    // El cliente solicita eliminar la cola key; el servidor responde con DELETED si se borra,
                // EMPTY si ya estaba vacía o no existía, y NOTAUTH si no tiene permisos
    DELETED,    // Se ha eliminado una cola con éxito
    NOAUTH,     // El servidor indica que la operación no es permitida por falta de autenticación o
                // permisos, devolviendo un mensaje de error según el caso.
    ERROR,      // El servidor devuelve esta primitiva en caso de error, como alcanzar el número
                // máximo de conexiones o intentos fallidos de login, con el mensaje correspondiente
    BARCODE;    // El servidor o cliente devuelven esta primitiva en caso de un intercambio
                // ilegal de primitivas, similar a la primitiva NOTUNDERSTAND
}

