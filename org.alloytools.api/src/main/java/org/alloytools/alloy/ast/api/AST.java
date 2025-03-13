package org.alloytools.alloy.ast.api;

import java.util.Map;

import org.alloytools.alloy.builder.api.Pos;
import org.alloytools.alloy.model.api.Multiplicity;
import org.alloytools.alloy.model.api.TDeclaration;
import org.alloytools.alloy.model.api.TFormula;
import org.alloytools.alloy.model.api.TFun;
import org.alloytools.alloy.model.api.TPred;
import org.alloytools.alloy.model.api.TSig;
import org.alloytools.alloy.model.api.TType;
import org.alloytools.alloy.model.api.TValue;

public interface AST {


    /**
     * [0-9]+ | '0x' [0-9A-Fa-f]+ | '0b' [10]+
     */

    record Number(Pos<TType> where, Pos<Integer> number) implements TValue {
    }

    /**
     * '"' ( ~["\\] | '\\' . )* '"'
     */
    record String_(Pos<TType> where, Pos<String> string) implements TValue {
    }

    /**
     * ...
     */
    record Sig(Pos<TType> where, TSig sig) implements TValue {

    }

    /**
     * ~ value
     */
    record Transpose(Pos<TType> where, TValue value) implements TValue {
    }

    /**
     * ^ value
     */
    record TransitiveClosure(Pos<TType> where, TValue value) implements TValue {
    }

    /**
     * * value
     */
    record ReflexiveTransitiveClosure(Pos<TType> where, TValue value) implements TValue {
    }

    /**
     * r'
     */
    record Prime(Pos<TType> where, Pos<TValue> value) implements TValue {
    }

    /**
     * value '.' value | value '[' value (',' value)* ']'
     */
    record Join(Pos<TType> where, TType type, TValue left, TValue right) implements TValue {
    }


    /**
     * value ':>' value
     */

    record RangeRestriction(Pos<TType> where, Pos<TValue> left, Pos<TValue> right) implements TValue {
    }

    /**
     * value '<:' value
     *
     */
    record DomainRestriction(Pos<TType> where, Pos<TValue> left, Pos<TValue> right) implements TValue {
    }

    /**
     * value multiplicity? '->' multiplicity? value
     */

    record Arrow(Pos<TType> where, TValue left, Multiplicity leftMulticiplicty, Multiplicity rightMulticiplicty, TValue right) implements TValue {
    }


    /**
     * value & value
     */
    record Intersection(Pos<TType> where, TValue left, TValue right) implements TValue {
    }

    /**
     * value ++ value
     */
    record Override_(Pos<TType> where, TValue left, TValue right) implements TValue {
    }

    /**
     * # r
     */
    record Cardinality(Pos<TType> where, TValue value) implements TValue {
    }

    /**
     * value + value
     */
    record Union(Pos<TType> where, TValue left, TValue right) implements TValue {
    }

    /**
     * value - value
     */
    record Difference(Pos<TType> where, TValue left, TValue right) implements TValue {
    }

    /**
     * '{' decl ( ',' decl )* ( block | bar ) '}'
     */
    record Comprehension(Pos<TType> where, TDeclaration[] declaration, TValue range) implements TValue {
    }

    /**
     * 'sum' decl ( ',' decl )+ '|' value
     */
    record Sum(Pos<TType> where, TDeclaration[] declaration, TValue range) implements TValue {
    }

    /**
     * formula ('=>'|'implies') value ('else' value)? ;
     */

    record ImpliesValue(Pos<TType> where, TFormula condition, TValue then, TValue else_) implements TValue {
    }

    record Fun(Pos<TType> where, TFun fun, TValue[] arguments, Multiplicity multiplicity, TValue body) implements TValue {
    }

    /**
     * Formulas
     */

    record Pred(Pos<TType> where, TPred pred, TValue[] arguments) implements TFormula {
    }

    record No(Pos<TType> where, TValue value) implements TFormula {
    }

    record Lone(Pos<TType> where, TValue value) implements TFormula {
    }

    record One(Pos<TType> where, TValue value) implements TFormula {
    }

    record Some(Pos<TType> where, TValue value) implements TFormula {
    }

    record Set(Pos<TType> where, TValue value) implements TFormula {
    }

    record Not(Pos<TType> where, TFormula value) implements TFormula {
    }

    record Always(Pos<TType> where, TFormula value) implements TFormula {
    }

    record Eventually(Pos<TType> where, TFormula value) implements TFormula {
    }

    record After(Pos<TType> where, TFormula value) implements TFormula {
    }

    record Before(Pos<TType> where, TFormula value) implements TFormula {
    }

    record Historically(Pos<TType> where, TFormula value) implements TFormula {
    }

    record Once(Pos<TType> where, TFormula value) implements TFormula {
    }

    record Releases(Pos<TType> where, TFormula left, TFormula right) implements TFormula {
    }

    record Since(Pos<TType> where, TFormula left, TFormula right) implements TFormula {
    }

    record Triggered(Pos<TType> where, TFormula left, TFormula right) implements TFormula {
    }

    record And(Pos<TType> where, TFormula left, TFormula right) implements TFormula {
    }

    record Or(Pos<TType> where, TFormula left, TFormula right) implements TFormula {
    }

    record Let(Pos<TType> where, Map<String,TValue> context, TFormula body) implements TFormula {
    }

    record Quantification(Pos<TType> where, Multiplicity multiplicity, TDeclaration[] declarations, TFormula body) implements TFormula {
    }

    record ImpliesFormula(Pos<TType> where, TFormula condition, TFormula then, TFormula else_) implements TFormula {
    }

}
