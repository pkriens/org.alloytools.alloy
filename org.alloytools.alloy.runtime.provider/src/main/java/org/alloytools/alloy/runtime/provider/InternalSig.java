package org.alloytools.alloy.runtime.provider;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.alloytools.alloy.builder.api.ModuleBuilder.SigDecl;
import org.alloytools.alloy.builder.api.Pos;
import org.alloytools.alloy.model.api.Environment;
import org.alloytools.alloy.model.api.TDeclaration;
import org.alloytools.alloy.model.api.TFormula;
import org.alloytools.alloy.model.api.TSig;
import org.alloytools.alloy.model.api.TType;

class InternalSig implements TSig, Internal {
	final InternalModule module;
	final String name;
	final List<InternalDeclaration> declarations;
	final LazyResolve<List<InternalSig>> base;
	final boolean isExtend;
	
	InternalSig(InternalModule module, SigDecl sig) {
		this.module = module;
		this.name = sig.name().get();
		this.declarations = sig.fields().stream().map(decl -> new InternalDeclaration(module, this, decl)).toList();
		this.isExtend = sig.isExtend();
		this.base = new LazyResolve<>( env -> resolve(env,sig.inherit()));
	}

	static List<InternalSig> resolve(Environment env, List<Pos<String>> inherit) {
		return null;
	}

	@Override
	public String name() {
		return name;
	}


	@Override
	public Optional<TFormula> block() {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public TSig common(TSig... with) {
		// TODO Auto-generated method stub
		return null;
	}

	public void resolve(Environment env) {

	}

	@Override
	public TType type() {
		return null;
	}

	@Override
	public Pos<TType> where() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, TDeclaration> fields() {
		// TODO Auto-generated method stub
		return null;
	}

}
