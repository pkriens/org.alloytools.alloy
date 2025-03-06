package org.alloytools.alloy.runtime.provider;

import java.util.List;

import org.alloytools.alloy.builder.api.ModuleBuilder.ImportDecl;
import org.alloytools.alloy.builder.api.Pos;

public class InternalOpen implements Internal {

	final InternalModule module;
	final InternalModule child;
	final List<String> typeReferences;
	final String name;


	InternalOpen(InternalModule module, InternalModule child, ImportDecl open) {
		this.module = module;
		this.child = child;
		this.typeReferences =open.typeReferences().stream().map( Pos::get).toList();
		this.name = open.alias().isPresent() ? open.alias().get() : child.simpleName();
	}


}
