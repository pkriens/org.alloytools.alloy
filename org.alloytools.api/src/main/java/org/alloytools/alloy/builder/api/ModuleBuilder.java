package org.alloytools.alloy.builder.api;

import java.util.List;

import org.alloytools.alloy.model.api.Multiplicity;
import org.alloytools.alloy.model.api.Qualifier;
import org.alloytools.alloy.model.api.TModule;
import org.alloytools.alloy.model.api.UserMessage;

public interface ModuleBuilder {

    record FormalArgument(boolean exactly, Pos<String> name) {
    }

    record Decl(Pos<String> var, Pos<String> disjNames, List<Pos<String>> names, Pos<String> disjValues, Pos<Multiplicity> multiplicity, ValueBuilder domain) {

        public Decl var(Pos<String> var) {
            return new Decl(var, disjNames, names, disjValues, multiplicity, domain);
        }

    }

    record KeyValue(Pos<String> name, ValueBuilder value) {
    }

    enum CommandType {
                      CHECK,
                      RUN
    };

    record ImportDecl(Pos<String> qname, List<Pos<String>> typeReferences, Pos<String> alias) {
    }

    record ModuleDecl(Pos<String> qname, List<FormalArgument> formalArguments) {
    }

    record SigDecl(List<Pos<Qualifier>> qualifiers, Pos<String> name, boolean isExtend, List<Pos<String>> inherit, List<Pos<Decl>> fields, FormulaBuilder block) {
    }

    record FactDecl(Pos<String> fact, FormulaBuilder facts) {
    }

    public record AssertDecl(Pos<String> name, FormulaBuilder facts) {
    }

    record PredDecl(Pos<String> pred, List<Decl> formalArgs, FormulaBuilder body) {
    }

    record FunDecl(Pos<String> name, List<Decl> formalArgs, Pos<Multiplicity> multiplicity, ValueBuilder domain, ValueBuilder body) {
    }

    record MacroDecl(Pos<String> id, List<Pos<String>> formalArguments, ValueBuilder formula) {
    }

    record CommandDecl(Pos<String> label, Pos<CommandType> type, Pos<String> predicate, FormulaBuilder body, List<Pos<TypeScope>> scopes, Pos<Integer> expect, Pos<CommandDecl> implies) {

        public CommandDecl label(Pos<String> label) {
            return new CommandDecl(label, type, predicate, body, scopes, expect, implies);
        }
    }

    record TypeScope(boolean exactly, Pos<Integer> start, Pos<Integer> end, Pos<Integer> step, Pos<String> typeReference) {
    }


    ModuleBuilder module(ModuleDecl moduleDecl);

    ModuleBuilder open(ImportDecl importDecl);

    ModuleBuilder sig(SigDecl sigDecl);

    ModuleBuilder fact(FactDecl factDecl);

    ModuleBuilder assert_(AssertDecl assertDecl);

    ModuleBuilder pred(PredDecl predDecl);

    ModuleBuilder fun(FunDecl fundDecl);

    ModuleBuilder command(CommandDecl runDecl);

    ModuleBuilder macro(MacroDecl macroDecl);


    ModuleBuilder add(UserMessage message);


    TModule build();

    ValueBuilder number(Pos<Integer> value);

    ValueBuilder reference(Pos<String> qname);

    ValueBuilder metaReference(Pos<String> qname);

    ValueBuilder comprehension(List<Decl> decl, FormulaBuilder block);


    ValueBuilder sum(List<Decl> decl, ValueBuilder value);

    ValueBuilder primitive(String qname, ValueBuilder operation, ValueBuilder right);

    // this could be either a join or function call.
    ValueBuilder call(String qname, List<ValueBuilder> args);


    ValueBuilder union(ValueBuilder left, ValueBuilder right);

    ValueBuilder difference(ValueBuilder left, ValueBuilder right);

    ValueBuilder cardinality(ValueBuilder value);

    ValueBuilder override(ValueBuilder left, ValueBuilder right);

    ValueBuilder intersection(ValueBuilder left, ValueBuilder right);

    ValueBuilder arrow(ValueBuilder left, Pos<Multiplicity> rangeMultiplicity, Pos<Multiplicity> domainMultiplicity, ValueBuilder right);

    ValueBuilder range(ValueBuilder left, ValueBuilder right);

    ValueBuilder domain(ValueBuilder left, ValueBuilder right);


    ValueBuilder dot(ValueBuilder left, ValueBuilder right);

    ValueBuilder box(ValueBuilder value, List<ValueBuilder> left);

    ValueBuilder prime(ValueBuilder value);

    ValueBuilder transpose(ValueBuilder value);

    ValueBuilder transitiveClosure(ValueBuilder value);

    ValueBuilder reflexiveTransitiveClosure(ValueBuilder value);

    ValueBuilder atname(Pos<String> name);

    ValueBuilder qname(Pos<String> qname);


    FormulaBuilder no(ValueBuilder value);

    FormulaBuilder lone(ValueBuilder value);

    FormulaBuilder one(ValueBuilder value);

    FormulaBuilder some(ValueBuilder value);

    FormulaBuilder set(ValueBuilder value);

    FormulaBuilder in(ValueBuilder left, ValueBuilder right, boolean not);

    FormulaBuilder equal(ValueBuilder left, ValueBuilder right, boolean not);

    FormulaBuilder lessOrEqual(ValueBuilder left, ValueBuilder right, boolean not);

    FormulaBuilder greaterOrEqual(ValueBuilder left, ValueBuilder right, boolean not);

    FormulaBuilder greater(ValueBuilder left, ValueBuilder right, boolean not);

    FormulaBuilder less(ValueBuilder left, ValueBuilder right, boolean not);

    FormulaBuilder not(FormulaBuilder formula);

    FormulaBuilder always(FormulaBuilder formula);

    FormulaBuilder eventually(FormulaBuilder formula);

    FormulaBuilder after(FormulaBuilder formula);

    FormulaBuilder before(FormulaBuilder formula);

    FormulaBuilder historically(FormulaBuilder formula);

    FormulaBuilder once(FormulaBuilder formula);

    FormulaBuilder releases(FormulaBuilder left, FormulaBuilder right);

    FormulaBuilder since(FormulaBuilder left, FormulaBuilder right);

    FormulaBuilder triggered(FormulaBuilder left, FormulaBuilder right);

    FormulaBuilder and(FormulaBuilder left, FormulaBuilder right);

    FormulaBuilder implies(FormulaBuilder condition, FormulaBuilder consequent, FormulaBuilder else_);

    FormulaBuilder iff(FormulaBuilder left, FormulaBuilder right);

    FormulaBuilder or(FormulaBuilder left, FormulaBuilder right);

    FormulaBuilder let(List<KeyValue> context, FormulaBuilder formula);

    FormulaBuilder all(List<Decl> declarations, FormulaBuilder formula);

    FormulaBuilder no(List<Decl> declarations, FormulaBuilder formula);

    FormulaBuilder lone(List<Decl> declarations, FormulaBuilder formula);

    FormulaBuilder one(List<Decl> declarations, FormulaBuilder formula);

    FormulaBuilder some(List<Decl> declarations, FormulaBuilder formula);

    FormulaBuilder pred(Pos<String> qname, List<Pos<String>> arguments);



}
