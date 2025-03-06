package org.alloytools.alloy.runtime.provider;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.alloytools.alloy.builder.api.ModuleBuilder.CommandDecl;
import org.alloytools.alloy.instance.api.Solution;
import org.alloytools.alloy.model.api.TCommand;
import org.alloytools.alloy.model.api.TFormula;
import org.alloytools.alloy.model.api.TModule;
import org.alloytools.alloy.model.api.TScope;

class InternalCommand implements TCommand {

	InternalCommand(InternalModule module, CommandDecl commandDecl) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<TScope> scopes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Expects> expects() {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public TModule module() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFormula block() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<TCommand> implies() {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Solution solve(Map<String, Object> options) {
		// TODO Auto-generated method stub
		return null;
	}

}
