package org.alloytools.alloy.runtime.provider;

import java.util.List;

import org.alloytools.alloy.builder.api.ModuleBuilder.FunDecl;
import org.alloytools.alloy.model.api.Multiplicity;
import org.alloytools.alloy.model.api.TDeclaration;
import org.alloytools.alloy.model.api.TFun;
import org.alloytools.alloy.model.api.TModule;
import org.alloytools.alloy.model.api.TType;
import org.alloytools.alloy.model.api.TValue;

class InternalFunction implements TFun, Internal {

	final InternalModule module;
	final String name;
	final List<InternalDeclaration> formalArguments;
	final Multiplicity multiplicity;
	final LazyResolve<TValue> domain;
	final LazyResolve<TValue> body;

	InternalFunction(InternalModule module, FunDecl funDecl) {
		this.module = module;
		this.multiplicity = funDecl.multiplicity().get();
		this.name = funDecl.name().get();
		this.formalArguments = funDecl.formalArgs().stream().map(decl -> new InternalDeclaration(module, decl))
				.toList();
		this.domain = new LazyResolve<>(env -> env.resolve(funDecl.domain()));
		this.body = new LazyResolve<>(env -> env.resolve(funDecl.body()));
	}


	@Override
	public String name() {
		return name;
	}

	@Override
	public TValue body() {
		return body.get();
	}

	@Override
	public TModule module() {
		return module;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<TDeclaration> arguments() {
		return (List) formalArguments;
	}

	@Override
	public Multiplicity multiplicity() {
		return multiplicity;
	}

	@Override
	public TValue domain() {
		return domain.get();
	}

	@Override
	public TType type() {
		return domain.get().type();
	}

}
