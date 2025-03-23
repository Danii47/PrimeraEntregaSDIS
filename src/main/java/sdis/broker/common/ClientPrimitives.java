package sdis.broker.common;

import java.util.Set;

public interface ClientPrimitives {
    public static final Set<Primitiva> PRIMITIVES_0_ARGS = Set.of(Primitiva.STATE);

    public static final Set<Primitiva> PRIMITIVES_1_ARGS = Set.of(Primitiva.READQ, Primitiva.DELETEQ, Primitiva.BADCODE);

    public static final Set<Primitiva> PRIMITIVES_2_ARGS = Set.of(Primitiva.XAUTH, Primitiva.ADDMSG);
}
