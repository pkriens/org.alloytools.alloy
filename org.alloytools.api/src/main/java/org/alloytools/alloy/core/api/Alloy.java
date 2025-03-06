package org.alloytools.alloy.core.api;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Primary interface into Alloy. An instance can be found through the Java
 * {@link java.util.ServiceLoader}
 * <p>
 * This class is the root into Alloy. It provides access to the solvers, the
 * compiler, and the visualizers.
 */
public interface Alloy {

    /**
     * Get a list of available solvers
     *
     * @return a list of available solvers
     */
    List<ToolChain> toolchains();

    /**
     * Get a solver with a specific name
     *
     * @param id the name of the solver
     * @return an optional Alloy solver
     */
    default Optional<ToolChain> toolchains(String id) {
        return toolchains().stream().filter(chain -> chain.id().equals(id)).findAny();
    }

    /**
     * Get a file in the Alloy private directory. The intention for this path is to
     * be used by solvers or visualizers for caches and preferences.
     *
     * @param pathWithSlashes a path separated with slashes also on windows
     * @return a Path to a file on the file system using slashes to separate
     *         directories.
     */
    File getPreferencesDir(String id);

}
