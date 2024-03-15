package org.alloytools.alloy.algorithms;

import java.util.Map;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.Pos;
import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.ExprBadJoin;
import edu.mit.csail.sdg.ast.ExprVar;

public class Resolver extends CopyVisitor {

    final Map<String,Expr> context;
    final ExprBuilder< ? > b;

    Resolver(ExprBuilder< ? > exprBuilder, Map<String,Expr> context) {
        this.b = exprBuilder;
        this.context = context;
    }

    @Override
    public Expr visit(ExprVar x) throws Err {
        String name = x.label;
        if (name.startsWith("@"))
            return b.var(name.substring(1)).pop();

        if (context.containsKey(name)) {
            b.push(context.get(name));
            b.var(name);
            return b.join().pop();
        }

        return new ExprVar(x);
    }

    @Override
    public Expr visit(ExprBadJoin x) throws Err {
        if (x.left instanceof ExprVar && ((ExprVar) x.left).label.equals("this"))
            return x;

        Expr left = x.left.accept(this);
        Expr right = x.right.accept(this);
        return ExprBadJoin.make(Pos.UNKNOWN, Pos.UNKNOWN, left, right);
    }

}
