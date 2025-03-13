package org.alloytools.alloy.runtime.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.alloytools.alloy.ast.api.AST.Join;
import org.alloytools.alloy.builder.api.FormulaBuilder;
import org.alloytools.alloy.builder.api.ModuleBuilder;
import org.alloytools.alloy.builder.api.Pos;
import org.alloytools.alloy.builder.api.ValueBuilder;
import org.alloytools.alloy.core.api.AlloyCompiler;
import org.alloytools.alloy.model.api.Multiplicity;
import org.alloytools.alloy.model.api.TModule;
import org.alloytools.alloy.model.api.TType;
import org.alloytools.alloy.model.api.TValue;
import org.alloytools.alloy.model.api.UserMessage;

import aQute.lib.collections.MultiMap;

public class ModuleBuilderProvider implements ModuleBuilder {

	final MultiMap<String, Internal> references = new MultiMap<>();
	final AlloyCompiler compiler;

	ModuleDecl moduleDecl = new ModuleDecl(Pos.empty(), Collections.emptyList());

	final List<ImportDecl> imports = new ArrayList<>();
	final List<Consumer<InternalModule>> typed = new ArrayList<>();
	final List<Consumer<InternalModule>> untyped = new ArrayList<>();
	final List<UserMessage> messages = new ArrayList<>();

	public ModuleBuilderProvider(AlloyCompiler compiler) {
		this.compiler = compiler;
	}

	@Override
	public TModule build() {
		InternalModule module = new InternalModule(moduleDecl);

		for (ImportDecl importDecl : imports) {
			InternalModule child = (InternalModule) compiler.compile(module, importDecl.qname().get());
			InternalOpen open = new InternalOpen(module, child, importDecl);
			module.add(open);
		}
		for (Consumer<InternalModule> internal : typed) {
			internal.accept(module);
		}
		for (Consumer<InternalModule> internal : untyped) {
			internal.accept(module);
		}
		module.seal();
		return module;
	}

	@Override
	public ModuleBuilder module(ModuleDecl moduleDecl) {
		this.moduleDecl = moduleDecl;
		return this;
	}

	@Override
	public ModuleBuilder open(ImportDecl importDecl) {
		imports.add(importDecl);
		return this;
	}

	@Override
	public ModuleBuilder sig(SigDecl sigDecl) {
		typed.add(module -> {
			InternalSig sig = new InternalSig(module, sigDecl);
			module.add(sig);
		});
		return this;
	}

	@Override
	public ModuleBuilder fact(FactDecl factDecl) {
		untyped.add(module -> {
			InternalFact sig = new InternalFact(module, factDecl);
			module.add(sig);
		});
		return this;
	}

	@Override
	public ModuleBuilder assert_(AssertDecl assertDecl) {
		untyped.add(module -> {
			InternalAssert assrt = new InternalAssert(module, assertDecl);
			module.add(assrt);
		});
		return this;
	}

	@Override
	public ModuleBuilder pred(PredDecl predDecl) {
		typed.add(module -> {
			InternalPredicate pred = new InternalPredicate(module, predDecl);
			module.add(pred);
		});
		return this;
	}

	@Override
	public ModuleBuilder fun(FunDecl fundDecl) {
		typed.add(module -> {
			InternalFunction pred = new InternalFunction(module, fundDecl);
			module.add(pred);
		});
		return this;
	}

	@Override
	public ModuleBuilder command(CommandDecl commandDecl) {
		untyped.add(module -> {
			InternalCommand command = new InternalCommand(module, commandDecl);
			module.add(command);
		});
		return null;
	}

	@Override
	public ModuleBuilder macro(MacroDecl macroDecl) {
		untyped.add(module -> {
			InternalMacro command = new InternalMacro(module, macroDecl);
			module.add(command);
		});
		return null;
	}

	@Override
	public ModuleBuilder add(UserMessage message) {
		messages.add(message);
		return this;
	}

	@Override
	public ValueBuilder number(Pos<Integer> value) {
		return null;
	}

	@Override
	public ValueBuilder metaReference(Pos<String> qname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder comprehension(List<Decl> decl, FormulaBuilder block) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder sum(List<Decl> decl, ValueBuilder value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder primitive(String qname, ValueBuilder operation, ValueBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder call(String qname, List<ValueBuilder> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder union(ValueBuilder left, ValueBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder difference(ValueBuilder left, ValueBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder cardinality(ValueBuilder value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder override(ValueBuilder left, ValueBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder intersection(ValueBuilder left, ValueBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder arrow(ValueBuilder left, Pos<Multiplicity> rangeMultiplicity,
			Pos<Multiplicity> domainMultiplicity, ValueBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder range(ValueBuilder left, ValueBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder domain(ValueBuilder left, ValueBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder box(ValueBuilder left, List<ValueBuilder> args) {
		return null;
	}

	@Override
	public ValueBuilder dot(ValueBuilder left, ValueBuilder right) {
		return right;
	}

	@Override
	public ValueBuilder prime(ValueBuilder value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder transpose(ValueBuilder value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder transitiveClosure(ValueBuilder value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder reflexiveTransitiveClosure(ValueBuilder value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder atname(Pos<String> name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder qname(Pos<String> qname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder no(ValueBuilder value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder lone(ValueBuilder value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder one(ValueBuilder value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder some(ValueBuilder value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder set(ValueBuilder value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder in(ValueBuilder left, ValueBuilder right, boolean not) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder equal(ValueBuilder left, ValueBuilder right, boolean not) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder lessOrEqual(ValueBuilder left, ValueBuilder right, boolean not) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder greaterOrEqual(ValueBuilder left, ValueBuilder right, boolean not) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder greater(ValueBuilder left, ValueBuilder right, boolean not) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder less(ValueBuilder left, ValueBuilder right, boolean not) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder not(FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder always(FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder eventually(FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder after(FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder before(FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder historically(FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder once(FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder releases(FormulaBuilder left, FormulaBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder since(FormulaBuilder left, FormulaBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder triggered(FormulaBuilder left, FormulaBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder and(FormulaBuilder left, FormulaBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder implies(FormulaBuilder condition, FormulaBuilder consequent, FormulaBuilder else_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder iff(FormulaBuilder left, FormulaBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder or(FormulaBuilder left, FormulaBuilder right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder let(List<KeyValue> context, FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder all(List<Decl> declarations, FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder no(List<Decl> declarations, FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder lone(List<Decl> declarations, FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder one(List<Decl> declarations, FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder some(List<Decl> declarations, FormulaBuilder formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FormulaBuilder pred(Pos<String> qname, List<Pos<String>> arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueBuilder reference(Pos<String> qname) {
		// TODO Auto-generated method stub
		return null;
	}
}
