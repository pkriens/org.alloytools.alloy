package org.alloytools.alloy.model.api;

import java.util.Optional;

import org.alloytools.alloy.model.api.TType.TTupleType;

/**
 * <a href="https://groups.csail.mit.edu/sdg/pubs/2004/typeSystem.pdf>A Type
 * System for Object Models</a>
 *
 */

public interface TType extends Iterable<TTupleType> {

    interface TTupleType extends Iterable<TSig> {

        TSig[] get();

        TSig get(int i);

        int arity();

    }

    int arity();

    TType join(TType other);

    TType product(TType other);

    boolean isSubSetOf(TType other);

    Optional<String> getError();

    default boolean isNotAType() {
        return getError().isPresent();
    }

    boolean isBoolean();

}

