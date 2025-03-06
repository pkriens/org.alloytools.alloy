package org.alloytools.alloy.classic.solver;

import java.util.Map;

import org.allotools.conversion.util.DTOs;
import org.alloytools.alloy.core.api.Alloy;
import org.alloytools.alloy.core.api.ToolChain;
import org.alloytools.alloy.core.api.ToolChain;
import org.alloytools.alloy.model.api.TCommand;
import org.alloytools.alloy.model.api.TModule;

public abstract class AbstractSolver implements ToolChain {

    private final Alloy core;

    public AbstractSolver(Alloy core) {
        this.core = core;
    }

    public Alloy getAlloy() {
        return core;
    }

    public String toString() {
        return getName();
    }

    protected SolverOptions processOptions(TModule module, TCommand command, SolverOptions optionsOrNull) {

        SolverOptions options = optionsOrNull == null ? newOptions() : optionsOrNull;

        assert newOptions().getClass().isAssignableFrom(options.getClass()) : options.getClass() + " is invalid option class for " + this;

        Map<String,String> sourceOptions = module.getSourceOptions(command);
        DTOs.set(options, sourceOptions);

        return options;
    }

}
