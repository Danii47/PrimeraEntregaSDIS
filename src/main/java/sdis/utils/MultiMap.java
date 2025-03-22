package sdis.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiMap<K, T> {

    private final ConcurrentMap<K, ConcurrentLinkedQueue<T>>
            map = new ConcurrentHashMap<K, ConcurrentLinkedQueue<T>>();


    /**
     * Agrega un valor a la cola asociada a la clave dada.
     * Si la clave no existe, se crea una nueva cola.
     */
//    public void push(K clave, T valor) {
//        map.computeIfAbsent(clave, k -> new ConcurrentLinkedQueue<>()).add(valor);
//    }
//    ESTO ES LO MISMO que lo de abajo

    public void push(K clave, T valor) {
        java.util.Queue<T> cola = map.get(clave);
        if (cola == null) {
            //putIfAbsent es atómica pero requiere "nueva", y es costoso
            ConcurrentLinkedQueue<T> nueva = new ConcurrentLinkedQueue<T>();
            ConcurrentLinkedQueue<T> previa = map.putIfAbsent(clave, nueva);
            cola = (previa == null) ? nueva : previa;
        }
        cola.add(valor);
    }

    /**
     * Extrae y devuelve el primer valor de la cola asociada a la clave.
     * @throws IllegalArgumentException si la clave no existe en el mapa.
     * @throws IllegalStateException si la cola asociada a la clave está vacía.
     */
    public T pop(K clave) {
        ConcurrentLinkedQueue<T> cola = map.get(clave);

        if (cola == null) {
            throw new IllegalArgumentException("Error: la clave '" + clave + "' no existe.");
        }

        T valor = cola.poll();
        if (valor == null) {
            throw new IllegalStateException("Error: la cola asociada a la clave '" + clave + "' está vacía.");
        }

        return valor;
    }

    /**
     * Verifica si una clave existe en el mapa.
     */
    public boolean containsKey(K clave) {
        return map.containsKey(clave);
    }

    /**
     * Verifica si la cola asociada a una clave está vacía.
     * @throws IllegalArgumentException si la clave no existe.
     */
    public boolean isEmpty(K clave) {
        ConcurrentLinkedQueue<T> cola = map.get(clave);

        if (cola == null) {
            throw new IllegalArgumentException("Error: la clave '" + clave + "' no existe.");
        }

        return cola.isEmpty();
    }
}
