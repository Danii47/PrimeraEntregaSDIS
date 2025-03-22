package sdis.broker.server;

import java.util.concurrent.ConcurrentHashMap;

public class AuthStorage {
    private static final ConcurrentHashMap<String, String> credentials = new ConcurrentHashMap<>();

    static {
        // Almacenar credenciales iniciales
        credentials.put("cllamas", "qwerty");
        credentials.put("hector", "lkjlkj");
        credentials.put("sdis", "987123");
        credentials.put("admin", "$%&/()=");
    }

    public static boolean isValidUser(String username, String password) {
        return password.equals(credentials.get(username));
    }
}