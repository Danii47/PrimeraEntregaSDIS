package sdis.broker.server;

import java.util.concurrent.ConcurrentHashMap;

public class BlacklistManager {
    private final ConcurrentHashMap<String, Integer> attemptsMap = new ConcurrentHashMap<>();
    private final int maxAttempts;

    public BlacklistManager(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    // Registrar un intento fallido o conexión
    public int registerAttempt(String ip) throws IllegalStateException {
        if (attemptsMap.getOrDefault(ip, 0) >= maxAttempts)
            throw new IllegalStateException("Intentos máximos de /" + ip + " superados: " + attemptsMap);

        attemptsMap.merge(ip, 1, Integer::sum);
        return attemptsMap.getOrDefault(ip, 0);
    }

    public int unregisterAttempt(String ip) {
        attemptsMap.computeIfPresent(ip, (f, value) -> value > 0 ? value - 1 : 0);
        return attemptsMap.getOrDefault(ip, 0);
    }

    // Verificar si la IP ha sido bloqueada
    public boolean isBanned(String ip) {
        return attemptsMap.getOrDefault(ip, 0) >= maxAttempts;
    }

    // Obtener el estado actual de la IP
    public int getAttempts(String ip) {
        return attemptsMap.getOrDefault(ip, 0);
    }
}
