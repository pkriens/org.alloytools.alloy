package org.alloytools.alloy.runtime.provider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.alloytools.alloy.builder.api.ModuleBuilder.ModuleDecl;
import org.alloytools.alloy.model.api.Environment;
import org.alloytools.alloy.model.api.TCheck;
import org.alloytools.alloy.model.api.TCommand;
import org.alloytools.alloy.model.api.TFact;
import org.alloytools.alloy.model.api.TFun;
import org.alloytools.alloy.model.api.TMacro;
import org.alloytools.alloy.model.api.TModule;
import org.alloytools.alloy.model.api.TPred;
import org.alloytools.alloy.model.api.TRun;
import org.alloytools.alloy.model.api.TSig;
import org.alloytools.alloy.model.api.TType;
import org.alloytools.alloy.model.api.Typed;
import org.alloytools.alloy.model.api.UserMessage;

public class InternalModule implements TModule, Environment {
	final ModuleDecl moduleDecl;
	final List<Internal> internals = new ArrayList<>();
	
	InternalModule(ModuleDecl moduleDecl) {
		this.moduleDecl = moduleDecl;
	}


	Environment getEnv() {
		// TODO Auto-generated method stub
		return null;
	}

	void add( Internal internal) {
		internals.add(internal);
	}


	@Override
	public List<Typed> resolve(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public URI uri() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TSig> sigs() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TRun> runs() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TCheck> checks() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<UserMessage> messages() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Map<String, Object> options(TCommand command) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TFun> functions() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TPred> preds() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TFact> facts() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TMacro> macros() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<String> formalArguments() {
		// TODO Auto-generated method stub
		return null;
	}


	public void seal() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String simpleName() {
		// TODO Auto-generated method stub
		return null;
	}


	public void add(InternalCommand command) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public TType INT() {
		// TODO Auto-generated method stub
		return null;
	}
}
