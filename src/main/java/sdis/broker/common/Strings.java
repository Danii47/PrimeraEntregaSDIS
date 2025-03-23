package sdis.broker.common;

public class Strings {
    public static final String SERVER_STARTED = "----Server Started----";
    public static final String SERVER_WAITING_CLIENT = "----Server Waiting For Client----";
    public static final String CLOSING_SOCKET = "Server: Closing socket";
    public static final String NOT_LOGGED = "User login is required";
    public static final String NOT_ADMIN = "NO ADMIN";
    public static final String INVALID_CREDENTIALS = "Err 401 ~ Credentials DO NOT MATCH. Try again";
    public static final String MAX_CONNECTIONS = "Err Max Number of connections reached.";
    public static final String MAX_LOGIN_ATTEMPTS = "Err Max Number of login attempts reached.";
    public static final String LOGGED = "User successfully logged";

    public static final String CLASS_NOT_FOUND_MESSAGE = "No fue posible transformar el objeto en la clase MensajeProtocolo";

    public static final String CLIENT_XAUTH_PARAMETERS = "XAUTH must have 2 parameters, name and password";
    public static final String CLIENT_STATE_PARAMETERS = "STATE mustn't have parameters";
    public static final String WELCOME_NOT_RECEIVED = "Welcome message hadn't been received.";

    public static final String PROTOCOL_QUEUE_ID_NOT_NULL = "Protocol queue id mustn't be null";
    public static final String PROTOCOL_MESSAGE_NOT_NULL = "Message mustn't be null";
    public static final String PROTOCOL_PRIMITIVE_NOT_NULL = "Primitive mustn't be null";

    public static final String INVALID_ARGUMENTS_LENGTH = "Invalid arguments length";

    public static String INVALID_PRIMITIVE(Primitiva primitive) {
        return String.format("Primitiva %s no es válida", primitive);
    }

    public static String CLOSED_CONNECTION(String lastUsername) {
        return String.format("Closed client connection, last user authenticated: %s", lastUsername);
    }

    public static String NEW_CONNECTION(String clientIP, int connectionCounter) {
        return String.format("[BM] (connections) for IP /%s = %d", clientIP, connectionCounter);
    }

    public static String MAX_CONNECTIONS_REACHED(String clientIP) {
        return String.format("[BM] (connections) for IP /%s exceeded maximum connections. Error sent and connection closed.", clientIP);
    }

    public static String LOGIN_FAILS(String clientIP, int actualAttempts) {
        return String.format("[BM] (login fails) for IP /%s = %d", clientIP, actualAttempts);
    }

    public static String UNEXPECTED_MESSAGE(Primitiva primitive) {
        return String.format("Unexpected message: %s", ((primitive == null) ? "null" : primitive.toString()));
    }

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 47014;
}
