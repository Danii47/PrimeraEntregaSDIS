package sdis.broker.common;

import java.util.Set;

public interface ServidorPrimitives {
    public static final Set<Primitiva> PRIMITIVES_0_ARGS =
            Set.of(
                    Primitiva.INFO,
                    Primitiva.ADDED,
                    Primitiva.DELETED,
                    Primitiva.EMPTY
            );

    public static final Set<Primitiva> PRIMITIVES_1_ARGS =
            Set.of(
                    Primitiva.XAUTH,
                    Primitiva.MSG,
                    Primitiva.BADCODE,
                    Primitiva.STATE,
                    Primitiva.NOAUTH,
                    Primitiva.ERROR
            );
}
