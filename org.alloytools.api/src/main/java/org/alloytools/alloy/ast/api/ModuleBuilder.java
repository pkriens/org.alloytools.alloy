package org.alloytools.alloy.ast.api;

import java.util.Set;

import org.alloytools.alloy.ast.api.AlloyUnit.Pos;

public interface ModuleBuilder {

    ModuleBuilder name(Pos pos, String qualifiedName, Set<String> formalArguments);

    ModuleBuilder open(Pos pos, String name, Set<String> formalArguments);

    SigBuilder sig(Pos pos, String name);

    interface SigBuilder {

        SigBuilder qualifiers(Qualifier... qualifiers);

        SigBuilder extend(Pos pos, String name);

        SigBuilder in(Pos pos, String... name);

        FieldBuilder field(Pos pos, String name);

        SigBuilder block(Pos pos, ExprBuilder block);

    }

    interface FieldBuilder {

        FieldBuilder var(Pos pos, boolean isVar);

        FieldBuilder decl(Pos pos, DeclBuilder decl);

    }

    // DeclBuilder declBuilder();

    // ExprBuilder declBuilder();

    interface DeclBuilder {

        DeclBuilder disj(Pos pos, boolean isDisj);

        DeclBuilder name(Pos pos, String name);

        DeclBuilder expr(Pos pos, boolean disj, ExprBuilder expr);
    }

    interface ExprBuilder {

        ExprBuilder number(Pos pos, int value);

        ExprBuilder qualifiedName(Pos pos, String qualifiedName);

        ExprBuilder noDereference(Pos pos, String name);

        ExprBuilder this_(Pos pos);

        ExprBuilder indexing(Pos pos, ExprBuilder... indexes);

        ExprBuilder else_(Pos pos, ExprBuilder elseExpr);

        ExprBuilder none(Pos pos);

        ExprBuilder prime(Pos pos);

        ExprBuilder block(Pos pos, ExprBuilder... exprs);

        ExprBuilder univ(Pos pos);

        ExprBuilder iden(Pos pos);

        ExprBuilder not(Pos pos, ExprBuilder expr);

        ExprBuilder no(Pos pos, ExprBuilder expr);

        ExprBuilder lone(Pos pos, ExprBuilder expr);

        ExprBuilder one(Pos pos, ExprBuilder expr);

        ExprBuilder some(Pos pos, ExprBuilder expr);

        ExprBuilder set(Pos pos, ExprBuilder expr);

        ExprBuilder cardinality(Pos pos, ExprBuilder expr);

        ExprBuilder transpose(Pos pos, ExprBuilder expr);

        ExprBuilder reflexiveTransitiveClosure(Pos pos, ExprBuilder expr);

        ExprBuilder transitiveClosure(Pos pos, ExprBuilder expr);

        ExprBuilder always(Pos pos, ExprBuilder expr);

        ExprBuilder eventually(Pos pos, ExprBuilder expr);

        ExprBuilder after(Pos pos, ExprBuilder expr);

        ExprBuilder before(Pos pos, ExprBuilder expr);

        ExprBuilder historically(Pos pos, ExprBuilder expr);

        ExprBuilder once(Pos pos, ExprBuilder expr);

        ExprBuilder or(Pos pos, ExprBuilder... right);

        ExprBuilder and(Pos pos, ExprBuilder... right);

        ExprBuilder iff(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder implies(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder intersection(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder union(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder difference(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder relationalOverride(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder domain(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder range(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder join(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder until(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder releases(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder since(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder triggered(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder after(Pos pos, ExprBuilder left, ExprBuilder right);

        enum ArrowMult {
                        LONE,
                        ONE,
                        SOME,
                        SET
        }

        ExprBuilder arrow(Pos pos, ExprBuilder left, ArrowMult leftMult, ArrowMult rightMul, ExprBuilder right);

        ExprBuilder in(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder eq(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder lt(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder le(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder gt(Pos pos, ExprBuilder left, ExprBuilder right);

        ExprBuilder ge(Pos pos, ExprBuilder left, ExprBuilder right);


    }

}
