package org.alloytools.alloy.runtime.provider;

import java.util.List;
import java.util.function.Supplier;

import org.alloytools.alloy.builder.api.ModuleBuilder.PredDecl;
import org.alloytools.alloy.model.api.Environment;
import org.alloytools.alloy.model.api.TDeclaration;
import org.alloytools.alloy.model.api.TFormula;
import org.alloytools.alloy.model.api.TModule;
import org.alloytools.alloy.model.api.TPred;

public class InternalPredicate implements TPred, Internal {

	final InternalModule module;
	final String name;
	final List<InternalDeclaration> formalArguments;
	final Supplier<TFormula> body;

	InternalPredicate(InternalModule module, PredDecl predDecl) {
		this.module = module;
		this.name = predDecl.pred().get();
		this.formalArguments = predDecl.formalArgs().stream().map( decl -> new InternalDeclaration(module,decl)).toList();
		this.body = new LazyResolve<>(env -> null);
	}

	void resolve(Environment env) {
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public TFormula body() {
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

}
