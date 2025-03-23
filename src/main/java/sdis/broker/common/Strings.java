package sdis.broker.common;

public class Strings {
    public static final String NOT_LOGGED = "Not logged in";
    public static final String NOT_ADMIN = "Permission denied, not admin";
    public static final String INVALID_CREDENTIALS = "Err 401 ~ Credentials DO NOT MATCH. Try again";
    public static final String MAX_CONNECTIONS = "Err Max Number of connections reached.";
    public static final String MAX_LOGIN_ATTEMPTS = "Err Max Number of login attempts reached.";
    public static final String LOGGED = "User successfully logged";

    public static final String CLOSED_CONNECTION = "Closed client connection";
    public static final String CLASS_NOT_FOUND_MESSAGE = "No fue posible transformar el objeto en la clase MensajeProtocolo";

    public static String NEW_CONNECTION(String clientIP, int connectionCounter) {
        return String.format("[BM] (connections) for IP /%s = %d", clientIP, connectionCounter);
    }

    public static String LOGIN_FAILS(String clientIP, int actualAttempts) {
        return String.format("[BM] (login fails) for IP /%s = %d", clientIP, actualAttempts);
    }

    public static String UNEXPECTED_MESSAGE(Primitiva primitive) {
        return String.format("Unexpected message: %s", ((primitive == null) ? "null" : primitive.toString()));
    }
}
