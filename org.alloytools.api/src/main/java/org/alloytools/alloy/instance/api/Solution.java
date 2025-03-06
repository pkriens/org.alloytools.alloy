package org.alloytools.alloy.instance.api;

import java.util.Map;

import org.alloytools.alloy.core.api.ToolChain;
import org.alloytools.alloy.model.api.TCommand;
import org.alloytools.alloy.model.api.TModule;

/**
 * A solution is produced by a {@link ToolChain}. It can either be satisfiable or
 * not. Only a satisfiable solution contains instances.
 */
public interface Solution extends Iterable<Instance> {

    /**
     * The solver that produced this solution
     *
     * @return the solver that produced this solution
     */
    ToolChain getSolver();

    /**
     * The options used when solving for this solution
     *
     * @return the options used when solving for this solution
     */
    Map<String,String> getOptions();

    /**
     * The module this solution was derived from.
     *
     * @return the module this solution was derived from.
     */
    TModule getModule();

    /**
     * The command this is a solution to
     *
     * @return the command this is a solution to
     */
    TCommand getCommand();

    /**
     * Whether the specification was satisfiable or not.
     */
    boolean isSatisfied();

    /**
     * Returns an empty relation.
     *
     * @return an empty relation.
     */
    IRelation none();

}
