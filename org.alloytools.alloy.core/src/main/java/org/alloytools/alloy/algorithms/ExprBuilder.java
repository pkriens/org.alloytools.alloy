package org.alloytools.alloy.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.Pos;
import edu.mit.csail.sdg.ast.Attr;
import edu.mit.csail.sdg.ast.Decl;
import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.ExprBadJoin;
import edu.mit.csail.sdg.ast.ExprBinary;
import edu.mit.csail.sdg.ast.ExprConstant;
import edu.mit.csail.sdg.ast.ExprITE;
import edu.mit.csail.sdg.ast.ExprList;
import edu.mit.csail.sdg.ast.ExprQt;
import edu.mit.csail.sdg.ast.ExprUnary;
import edu.mit.csail.sdg.ast.ExprUnary.Op;
import edu.mit.csail.sdg.ast.ExprVar;
import edu.mit.csail.sdg.ast.Sig;
import edu.mit.csail.sdg.ast.Type;
import edu.mit.csail.sdg.parser.CompModule;

@SuppressWarnings("unchecked" )
public class ExprBuilder<T extends Expr> {

    final Stack<Expr>         stack     = new Stack<>();
    final CompModule          module;
    final Map<String,Expr>    context   = new HashMap<String,Expr>();
    final Map<String,Expr>    extra     = new HashMap<String,Expr>();
    final Map<String,ExprVar> variables = new HashMap<String,ExprVar>();
    final Resolver            resolver  = new Resolver(this, context);
    private boolean           init      = false;

    ExprBuilder(CompModule module) {
        this.module = module;
    }

    ExprBuilder<Expr> resolve() {
        Expr pop = pop();
        Expr resolved = pop.accept(resolver);
        push(resolved);
        return (ExprBuilder<Expr>) this;
    }

    ExprBuilder<ExprBadJoin> join() {
        Expr right = stack.pop();
        Expr left = stack.pop();
        stack.push(ExprBadJoin.make(Pos.UNKNOWN, Pos.UNKNOWN, left, right));
        return (ExprBuilder<ExprBadJoin>) this;
    }

    ExprBuilder<ExprVar> var(String label, Type type) {
        if (type == null)
            type = Type.EMPTY;

        stack.push(makeVar(label, type));
        return (ExprBuilder<ExprVar>) this;
    }

    public ExprVar makeVar(String label, Type type) {
        return variables.computeIfAbsent(label, name -> ExprVar.make(Pos.UNKNOWN, name, type));
    }

    public ExprVar makeVar(String label) {
        return makeVar(label, Type.EMPTY);
    }



    ExprBuilder<ExprVar> var(String label) {
        stack.push(makeVar(label, Type.EMPTY));
        return (ExprBuilder<ExprVar>) this;
    }


    public ExprBuilder<ExprBinary> equals() {
        return binary(ExprBinary.Op.EQUALS);
    }


    public ExprBuilder<ExprBinary> binary(ExprBinary.Op op) {
        Expr right = stack.pop();
        Expr left = stack.pop();
        stack.push(op.make(Pos.UNKNOWN, Pos.UNKNOWN, left, right));
        return (ExprBuilder<ExprBinary>) this;
    }

    ExprBuilder<ExprList> and(int n) {
        return exprlist(n, ExprList.Op.AND);
    }

    public ExprBuilder<ExprList> or(int n) {
        return exprlist(n, ExprList.Op.OR);
    }

    ExprBuilder<ExprList> exprlist(int n, ExprList.Op op) {
        List<Expr> expressions = new ArrayList<>();
        while (n > 0) {
            n--;
            expressions.add(stack.pop());
        }
        Collections.reverse(expressions);
        stack.push(ExprList.make(Pos.UNKNOWN, Pos.UNKNOWN, op, expressions));
        return (ExprBuilder<ExprList>) this;
    }


    T pop() {
        return (T) stack.pop();
    }

    public <X extends Expr> ExprBuilder<X> push(X body) {
        stack.push(body);
        return (ExprBuilder<X>) this;
    }


    public ExprBuilder<ExprUnary> prime() {
        Primer p = new Primer((ExprBuilder<Expr>) this);
        push(pop().accept(p));
        return (ExprBuilder<ExprUnary>) this;
    }

    public ExprBuilder<ExprBinary> implies() {
        Expr left = stack.pop();
        Expr cond = stack.pop();
        push(ExprBinary.Op.IMPLIES.make(Pos.UNKNOWN, Pos.UNKNOWN, cond, left));
        return (ExprBuilder<ExprBinary>) this;
    }

    public ExprBuilder<ExprBinary> impliesElse() {
        Expr right = stack.pop();
        Expr left = stack.pop();
        Expr cond = stack.pop();
        push(ExprITE.make(Pos.UNKNOWN, cond, left, right));
        return (ExprBuilder<ExprBinary>) this;
    }

    public ExprBuilder<T> dup() {
        stack.push(stack.peek());
        return this;
    }

    List<ExprVar> vars(Collection<String> keys) {
        return keys.stream().map(label -> var(label).pop()).collect(Collectors.toList());
    }

    public Sig defineEnum(String name, Collection<String> atoms) {
        module.addEnum(Pos.UNKNOWN, null, var(name).pop(), vars(atoms), Pos.UNKNOWN);
        return module.getSig(name);
    }

    public Decl decl(Expr e, String... names) {
        return new Decl(null, null, null, Pos.UNKNOWN, vars(Arrays.asList(names)), e);
    }

    public Sig defineSig(String name, String par, List<String> parents, List<Decl> fields, Expr fact, Attr... attributes) throws Err {
        return module.addSig(name, par == null ? null : var(par).pop(), parents == null ? null : vars(parents), fields, fact, attributes);
    }

    public int size() {
        return stack.size();
    }

    public ExprBuilder<T> setContext(String name, Expr value) {
        this.context.put(name, value);
        return this;
    }

    public ExprBuilder<T> eventually() {
        unary(ExprUnary.Op.EVENTUALLY);
        return this;
    }

    public ExprBuilder<T> always() {
        unary(ExprUnary.Op.ALWAYS);
        return this;
    }

    public ExprBuilder<ExprUnary> unary(Op op) {
        Expr make = op.make(Pos.UNKNOWN, pop());
        push(make);
        return (ExprBuilder<ExprUnary>) this;
    }

    public ExprBuilder<T> setInit(boolean b) {
        this.init = b;
        return this;
    }

    public boolean isInit() {
        return init;
    }

    public ExprBuilder<ExprBinary> arrow() {
        return binary(ExprBinary.Op.ARROW);
    }

    public ExprBuilder<ExprBinary> in() {
        return binary(ExprBinary.Op.IN);
    }

    public ExprBuilder<ExprBinary> plusplus() {
        return binary(ExprBinary.Op.PLUSPLUS);
    }

    public Expr fixupVariables(Expr expr) {
        class Fixup extends CopyVisitor {

            @Override
            public Expr visit(ExprVar x) throws Err {
                return makeVar(x.label);
            }
        }
        return expr.accept(new Fixup());
    }

    public Set<Expr> fixupVariables(Collection<Expr> expr) {
        return expr.stream().map(this::fixupVariables).collect(Collectors.toSet());
    }

    public Expr resolve(Expr lvar) {
        return push(lvar).resolve().pop();
    }

    public ExprBuilder<ExprBinary> minus() {
        return binary(ExprBinary.Op.MINUS);
    }

    public ExprBuilder<ExprUnary> noop() {
        return unary(ExprUnary.Op.NOOP);
    }

    public void some(List<Decl> decls) {
        push(ExprQt.Op.SOME.make(Pos.UNKNOWN, null, decls, pop()));
    }

    public void all(List<Decl> decls) {
        push(ExprQt.Op.ALL.make(Pos.UNKNOWN, null, decls, pop()));
    }

    public String print() {
        T pop = pop();
        Formatter f = new Formatter();
        Printer p = new Printer(f);
        pop.accept(p);
        return f.toString();
    }

    public ExprBuilder<T> constant(int i) {
        push(ExprConstant.makeNUMBER(i));
        return this;
    }
}
