package org.alloytools.alloy.core.spi;

import java.util.Set;

import org.alloytools.alloy.core.api.Alloy;
import org.alloytools.alloy.core.api.ToolChain;

/**
 * Provides access to the solvers embedded in the local application. To add a
 * solver, add a file in
 * {@code META-INF/services/org.alloytools.alloy.solver.api.AlloySolverFactory}
 * with as content the FQN of the implementation class.
 */
public interface AlloyToolChainFactory {

    /**
     * Answer a set of solvers for the given Alloy. The Alloy is passed to provide
     * access to a private data area for the given solver.
     *
     * @param alloy the home.
     * @return a set of Alloy Solver
     */
    Set<ToolChain> getAvailableToolChains(Alloy alloy);
}
