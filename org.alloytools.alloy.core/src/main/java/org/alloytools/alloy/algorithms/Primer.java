package org.alloytools.alloy.algorithms;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.Pos;
import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.ExprBadJoin;
import edu.mit.csail.sdg.ast.ExprUnary;
import edu.mit.csail.sdg.ast.ExprVar;

public class Primer extends CopyVisitor {

    ExprBuilder<Expr> b;

    Primer(ExprBuilder<Expr> exprBuilder) {
        this.b = exprBuilder;
    }

    @Override
    public Expr visit(ExprVar x) throws Err {
        return ExprUnary.Op.PRIME.make(Pos.UNKNOWN, x);
    }

    @Override
    public Expr visit(ExprBadJoin x) throws Err {
        Expr right = x.right.accept(this);
        return ExprBadJoin.make(Pos.UNKNOWN, Pos.UNKNOWN, x.left, right);
    }

}
