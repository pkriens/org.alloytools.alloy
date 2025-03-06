
package org.alloytools.alloy.model.api;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.alloytools.alloy.instance.api.Solution;

public interface TCommand {

    enum Expects {
                  UNKNOWN,
                  SATISFIABLE,
                  UNSATISFIABLE
    }

    /**
     * The name of the command
     */
    String name();

    /**
     * Return the scopes defined by this command
     *
     * @return scopes defined by this command
     */
    Set<TScope> scopes();

    /**
     * A hint whether the command is expected to be satisfiable or not. This is
     * mainly used for testing and documentation purposes and has no effect on
     * solving.
     *
     * @return expects
     */
    Optional<Expects> expects();

    /**
     * Get the associated module.
     *
     * @return the module
     */
    TModule module();

    TFormula block();

    Optional<TCommand> implies();

    Solution solve(Map<String,Object> options);
}
