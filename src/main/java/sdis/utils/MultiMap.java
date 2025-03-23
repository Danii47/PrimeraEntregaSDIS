package sdis.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiMap<K, T> {

    private final ConcurrentMap<K, ConcurrentLinkedQueue<T>> map = new ConcurrentHashMap<K, ConcurrentLinkedQueue<T>>();

    public void push(K clave, T valor) {
        java.util.Queue<T> cola = map.get(clave);
        if (cola == null) {
            ConcurrentLinkedQueue<T> nueva = new ConcurrentLinkedQueue<T>();
            ConcurrentLinkedQueue<T> previa = map.putIfAbsent(clave, nueva);
            cola = (previa == null) ? nueva : previa;
        }
        cola.add(valor);
    }

    public T pop(K clave) {
        ConcurrentLinkedQueue<T> cola = map.get(clave);

        if (cola == null)
            throw new IllegalArgumentException("Error: la clave '" + clave + "' no existe.");

        T valor = cola.poll();
        if (valor == null)
            throw new IllegalStateException("Error: la cola asociada a la clave '" + clave + "' está vacía.");

        return valor;
    }

    public void remove(K clave) {
        map.remove(clave);
    }

    public boolean containsKey(K clave) {
        return map.containsKey(clave);
    }

    public boolean isEmpty(K clave) {
        ConcurrentLinkedQueue<T> cola = map.get(clave);

        if (cola == null)
            throw new IllegalArgumentException("Error: la clave '" + clave + "' no existe.");

        return cola.isEmpty();
    }
}
