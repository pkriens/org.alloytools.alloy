package org.alloytools.alloy.core.api;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Represents a solver.
 */
public interface ToolChain {

    /**
     * The identity of the solver. This identity must be unique world-wide so a FQN
     * is recommended. For example, the primary class name.
     *
     * @return the identity
     */
    String id();

    /**
     * Get a human readable name.
     *
     * @return a name
     */
    String name();

    /**
     * Returns the tags of this solver.
     *
     * @return the tags
     */
    Set<String> tags();

    /**
     * Get a short English description of this solver.
     *
     * @return a description
     */
    String description();



    record OptionDescription<T>(String id, String name, String description, Class<T> dataType, Object defaultValue, Function<T,String> isValid) {
    }

    /**
     * Return a description of the options for this toolchain
     *
     * @return Option descriptions
     */
    List<OptionDescription< ? >> optionsDescription();

    AlloyCompiler compiler(SourceResolver resolver);
}
