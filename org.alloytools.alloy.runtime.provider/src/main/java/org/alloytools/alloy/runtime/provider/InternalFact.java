package org.alloytools.alloy.runtime.provider;

import org.alloytools.alloy.builder.api.ModuleBuilder.FactDecl;
import org.alloytools.alloy.model.api.TFact;
import org.alloytools.alloy.model.api.TFormula;

public class InternalFact implements TFact, Internal {

	final InternalModule module;
	final String name;
	final LazyResolve<TFormula> body;

	InternalFact(InternalModule module, FactDecl factDecl) {
		this.name = factDecl.fact().get();
		this.module = module;
		this.body = new LazyResolve<>(env -> factDecl.facts().resolve(env));
	}


	@Override
	public String name() {
		return name;
	}

	@Override
	public TFormula body() {
		return body.get();
	}

}
