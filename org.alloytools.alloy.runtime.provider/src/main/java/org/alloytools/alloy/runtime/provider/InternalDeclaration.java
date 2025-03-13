package org.alloytools.alloy.runtime.provider;

import java.util.List;

import org.alloytools.alloy.builder.api.ModuleBuilder.Decl;
import org.alloytools.alloy.builder.api.Pos;
import org.alloytools.alloy.model.api.Environment;
import org.alloytools.alloy.model.api.Multiplicity;
import org.alloytools.alloy.model.api.TDeclaration;
import org.alloytools.alloy.model.api.TType;
import org.alloytools.alloy.model.api.TValue;

public class InternalDeclaration implements TDeclaration {

	InternalDeclaration(InternalModule module, Decl declaration) {
		
	}
	public InternalDeclaration(InternalModule module, InternalSig internalSig, Pos<Decl> decl) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean var() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean disjNames() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> names() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean disjValues() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Multiplicity multiplicity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TValue domain() {
		// TODO Auto-generated method stub
		return null;
	}
	public void resolve(Environment env) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Pos<TType> where() {
		// TODO Auto-generated method stub
		return null;
	}

}
