package sdis.broker.common;

import java.util.Set;

public interface ServidorPrimitives {
    Set<Primitiva> PRIMITIVES_0_ARGS = Set.of(Primitiva.INFO, Primitiva.ADDED, Primitiva.DELETED, Primitiva.EMPTY);

    Set<Primitiva> PRIMITIVES_1_ARGS = Set.of(Primitiva.XAUTH, Primitiva.MSG, Primitiva.BADCODE, Primitiva.STATE, Primitiva.NOTAUTH, Primitiva.ERROR);
}
