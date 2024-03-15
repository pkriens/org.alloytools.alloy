package org.alloytools.alloy.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.ast.Assert;
import edu.mit.csail.sdg.ast.Decl;
import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.ExprBad;
import edu.mit.csail.sdg.ast.ExprBadCall;
import edu.mit.csail.sdg.ast.ExprBadJoin;
import edu.mit.csail.sdg.ast.ExprBinary;
import edu.mit.csail.sdg.ast.ExprCall;
import edu.mit.csail.sdg.ast.ExprConstant;
import edu.mit.csail.sdg.ast.ExprITE;
import edu.mit.csail.sdg.ast.ExprLet;
import edu.mit.csail.sdg.ast.ExprList;
import edu.mit.csail.sdg.ast.ExprQt;
import edu.mit.csail.sdg.ast.ExprUnary;
import edu.mit.csail.sdg.ast.ExprVar;
import edu.mit.csail.sdg.ast.Func;
import edu.mit.csail.sdg.ast.Sig;
import edu.mit.csail.sdg.ast.Sig.Field;
import edu.mit.csail.sdg.ast.VisitReturn;
import edu.mit.csail.sdg.parser.Macro;

public class CopyVisitor extends VisitReturn<Expr> {

    @Override
    public Expr visit(ExprBinary x) throws Err {
        return new ExprBinary(x, copy(x.left), copy(x.right));
    }

    @Override
    public Expr visit(ExprList x) throws Err {
        return new ExprList(x, copy(x.args));
    }

    @Override
    public Expr visit(ExprCall x) throws Err {
        return new ExprCall(x, copy(x.args));
    }

    @Override
    public Expr visit(ExprConstant x) throws Err {
        return x;
    }

    @Override
    public Expr visit(ExprITE x) throws Err {
        return new ExprITE(x, copy(x.cond), copy(x.left), copy(x.right));
    }

    @Override
    public Expr visit(ExprLet x) throws Err {
        return new ExprLet(x, copy(x.var), copy(x.expr), copy(x.sub));
    }

    @Override
    public Expr visit(ExprQt x) throws Err {
        return new ExprQt(x, x.op, copy(x.decls), copy(x.sub));
    }

    @Override
    public Expr visit(ExprUnary x) throws Err {
        return new ExprUnary(x, x.op, copy(x.sub));
    }

    @Override
    public Expr visit(ExprVar x) throws Err {
        return new ExprVar(x);
    }

    @Override
    public Expr visit(Sig x) throws Err {
        return x;
    }

    @Override
    public Expr visit(Field x) throws Err {
        return x;
    }

    /** Visits a ExprBad node */
    @Override
    public Expr visit(ExprBad x) throws Err {
        return x;
    }

    /** Visits a ExprBadCall node */
    @Override
    public Expr visit(ExprBadCall x) throws Err {
        return x;
    }

    /** Visits a ExprBadJoin node */
    @Override
    public Expr visit(ExprBadJoin x) throws Err {
        return new ExprBadJoin(x, copy(x.left), copy(x.right), x.errors);
    }

    private List<Expr> copy(Iterable<Expr> source) {
        List<Expr> copy = new ArrayList<>();
        for (Expr expr : source) {
            copy.add(copy(expr));
        }
        return copy;
    }

    @SuppressWarnings("unchecked" )
    private <T extends Expr> T copy(T source) {
        if (source == null)
            return null;

        return (T) source.accept(this);
    }

    private List<Decl> copy(Collection<Decl> decls) {
        List<Decl> output = new ArrayList<>();
        decls.forEach(e -> output.add(copy(e)));
        return output;
    }

    private Decl copy(Decl source) {
        return source;
    }

    @Override
    public Expr visit(Func x) throws Err {
        return copy(x);
    }

    @Override
    public Expr visit(Assert x) throws Err {
        return new Assert(x, x.label, copy(x.expr));
    }

    @Override
    public Expr visit(Macro macro) throws Err {
        return macro;
    }

}
