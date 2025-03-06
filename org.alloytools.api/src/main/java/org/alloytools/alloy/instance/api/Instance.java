package org.alloytools.alloy.instance.api;

/**
 * Represents a value assignment that satisfies an Alloy specification. An
 * instance belongs to a {@link Solution}. A solution can have multiple
 * instances, all satisfying the same Alloy specification.
 */
public interface Instance extends Iterable<Trace>, Trace {


}
