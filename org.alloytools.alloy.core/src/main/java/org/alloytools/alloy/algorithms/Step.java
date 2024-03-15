package org.alloytools.alloy.algorithms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.alloytools.alloy.algorithms.Statement.Compound;

import edu.mit.csail.sdg.alloy4.Pos;
import edu.mit.csail.sdg.ast.Decl;
import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.Func;

class Step {

    final String   label;
    final Compound statements;

    Step(String label, Compound statements) {
        this.label = label;
        this.statements = statements;
    }



    @Override
    public String toString() {
        return "pred " + label + " : " + statements + "";
    }


    public Func pred(Algorithm algorithm, ExprBuilder<Expr> b) {
        statements.build(algorithm);
        Expr body = b.pop();
        System.out.println(body);
        return new Func(Pos.UNKNOWN, Pos.UNKNOWN, label, Collections.emptyList(), null, body);
    }



    public void doVariables(Algorithm alg) {
        Set<Ref> touched = statements.doVariables(alg);

        for (Relation r : alg.relations.values()) {
            if (r.usage != Relation.Usage.FIXED) {
                Set<Ref> filtered = touched.stream().filter(ref -> ref.relation == r).collect(Collectors.toSet());
                if (filtered.isEmpty()) {
                    // no references, make all equal
                    if (r.usage != Relation.Usage.P) {
                        alg.b.var(r.label);
                        statements.add(new Statement.Assignment(alg.b.pop(), null));
                    }
                } else {
                    // references, so subtract from domain
                    switch (r.usage) {
                        case MULTIPLE :
                            alg.b.var(alg.toSimple(r.head.label));
                            for (Ref ref : filtered) {
                                alg.b.push(ref.path).minus();
                            }
                            Decl tmp = new Decl(null, null, null, null, Arrays.asList(alg.b.var("tmp").pop()), alg.b.pop());
                            List<Decl> tmps = Arrays.asList(tmp);

                            alg.b.var("tmp").var(r.label).prime().join();
                            alg.b.var("tmp").var(r.label).join();
                            alg.b.equals();

                            alg.b.all(tmps);

                            statements.add(new Statement.Assignment(null, alg.b.pop()));
                            break;
                        default :
                        case FIXED :
                        case ONE :
                        case P :
                        case THIS :
                            // any reference means we're ok
                            break;
                    }
                }
            }
        }

    }


    public Expr build(Algorithm alg) {
        statements.build(alg);
        return alg.b.pop();
    }
}
