package org.alloytools.alloy.classic.provider;

import java.util.List;
import java.util.Set;

import org.alloytools.alloy.builder.api.FormulaBuilder;
import org.alloytools.alloy.builder.api.ModuleBuilder;
import org.alloytools.alloy.builder.api.Pos;
import org.alloytools.alloy.builder.api.ValueBuilder;
import org.alloytools.alloy.model.api.Multiplicity;
import org.alloytools.alloy.model.api.Qualifier;
import org.alloytools.alloy.model.api.TModule;
import org.alloytools.alloy.model.api.UserMessage;


public class PardinusModuleBuilder implements ModuleBuilder {


    @Override
    public ModuleBuilder module(Pos<String> qualifiedName, List<Pos<String>> formalArguments) {
        return null;
    }

    @Override
    public ModuleBuilder open(Pos<String> qualifiedName, List<Pos<String>> typeReferences, Pos<String> as) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModuleBuilder enum_(Set<Pos<Qualifier>> qualifiers, Pos<String> name, List<Pos<String>> members) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModuleBuilder sig(List<Pos<Qualifier>> qualifiers, Pos<String> name, Pos<String> extend, List<Pos<String>> ins, List<Pos<Decl>> fields, FormulaBuilder block) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModuleBuilder fact(Pos<String> fact, FormulaBuilder facts) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModuleBuilder assert_(Pos<String> name, FormulaBuilder facts) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModuleBuilder pred(Pos<String> pred, List<Decl> formalArgs, FormulaBuilder body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModuleBuilder fun(Pos<String> name, List<Decl> formalArgs, Pos<Multiplicity> multiplicity, ValueBuilder domain, ValueBuilder body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModuleBuilder run(Pos<String> id, Pos<String> predRef, FormulaBuilder block, Pos<Integer> expects, TypeScope... scopes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModuleBuilder check(Pos<String> id, Pos<String> predRef, FormulaBuilder block, TypeScope... scope) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModuleBuilder macro(Pos<String> id, List<Pos<String>> formalArguments, ValueBuilder formula) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ModuleBuilder add(UserMessage message) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueBuilder valueBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TModule build() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueBuilder number(int value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueBuilder qualifiedName(String qname) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueBuilder this_() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueBuilder iden() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueBuilder univ() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueBuilder none() {
        // TODO Auto-generated method stub
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
    public ValueBuilder arrow(ValueBuilder left, Pos<Multiplicity> rangeMultiplicity, Pos<Multiplicity> domainMultiplicity, ValueBuilder right) {
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
    public ValueBuilder boxJoin(ValueBuilder value, List<ValueBuilder> left) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueBuilder dot(ValueBuilder left, ValueBuilder right) {
        // TODO Auto-generated method stub
        return null;
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

}
