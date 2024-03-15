package org.alloytools.alloy.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.mit.csail.sdg.alloy4.Pos;
import edu.mit.csail.sdg.ast.Decl;
import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.ExprBinary;
import edu.mit.csail.sdg.ast.ExprConstant;

abstract public class Statement {


    public interface Visitor<T> {

        T compound(Compound s);

        T assert_(Assert s);

        T assignment(Assignment s);

        T if_(If s);

        T while_(If s);

        T label(Label s);

    }

    public static class Compound extends Statement {

        final List<Statement> statements = new ArrayList<>();

        public Compound(Statement... statements) {
            if (statements != null)
                this.statements.addAll(Arrays.asList(statements));
        }

        public Compound(List<Statement> statements) {
            if (statements != null)
                this.statements.addAll(statements);
        }

        public Compound(List<Statement> output, int start, int end) {
            if (end <= 0)
                end = output.size() + end;

            for (int i = start; i < end; i++) {
                this.statements.add(output.get(i));
            }
        }

        @Override
        protected void build(Algorithm alg) {
            ExprBuilder<Expr> b = alg.b;
            int mark = b.size();
            statements.forEach(s -> s.build(alg));
            int sz = b.size() - mark;
            assert sz >= 0;
            Expr body;
            if (sz == 0)
                b.push(ExprConstant.TRUE);
            else
                b.and(sz);
        }

        @Override
        public String toString() {
            return statements.stream().map(Statement::toString).collect(Collectors.joining(", ", "{", "}"));
        }

        public boolean isEmpty() {
            return statements.isEmpty();
        }

        @Override
        protected Set<Ref> doVariables(Algorithm alg) {
            Set<Ref> vars = new HashSet<>();
            for (Statement s : statements) {
                Set<Ref> next = s.doVariables(alg);
                vars.addAll(next);
            }
            return vars;
        }

        public void add(Statement s) {
            this.statements.add(s);
        }
    }


    public static class Assert extends Statement {

        final String label;
        final Expr   expr;

        public Assert(String label, Expr expr) {
            this.label = label;
            this.expr = expr;
        }

        @Override
        protected void build(Algorithm alg) {
            ExprBuilder<Expr> b = alg.b;
            b.var("this").var(alg.name).join();
            alg.finished(alg.b);
            b.push(expr).resolve();
            b.and(2);
            b.eventually();
            b.implies();
            String tmpPredicateName = label + "_" + alg.label++;
            b.module.addFunc(Pos.UNKNOWN, null, tmpPredicateName, null, alg.prototypeEventFunctions, null, b.noop().pop());

            b.var(alg.name);
            b.var(tmpPredicateName);
            b.join();
            b.module.addAssertion(Pos.UNKNOWN, Pos.UNKNOWN, label, b.noop().pop());
        }

        @Override
        public String toString() {
            return "ASSERT " + expr;
        }

    }


    public static class Label extends Statement {

        final String label;

        public Label(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return "LABEL " + label;
        }

        @Override
        protected void split(Algorithm algorithm, String name, List<Statement> output) {
            assert false : "Labels are never split";
        }

        @Override
        protected void build(Algorithm alg) {
            ExprBuilder<Expr> b = alg.b;
            b.var("pc").resolve();
            b.var(label);
            b.equals();
        }

    }

    static class Skip extends Statement {

        @Override
        public String toString() {
            return "SKIP";
        }

        @Override
        protected void build(Algorithm alg) {
        }
    }


    static class Goto extends Statement {

        final String label;

        public Goto(String l) {
            this.label = l;
        }


        @Override
        public String toString() {
            return "GOTO " + label;
        }


        @Override
        protected void build(Algorithm alg) {
            ExprBuilder<Expr> b = alg.b;
            if (b.isInit()) {
                if (b.context.containsKey("p")) {
                    b.var("pc").resolve();

                    ExprBinary expr = (ExprBinary) b.extra.get("process");
                    b.push(expr.left);
                    b.var(label);
                    b.arrow();
                    b.equals();
                } else {
                    b.var("pc").resolve();
                    b.var(label);
                    b.equals();
                }
            } else {

                if (b.context.containsKey("p")) {
                    b.var("pc").resolve();
                    b.prime();
                    b.var("pc").resolve();
                    b.var("p").resolve();
                    b.var(label);
                    b.arrow();
                    b.plusplus();
                    b.equals();
                } else {
                    b.var("pc").resolve();
                    b.prime();
                    b.var(label);
                    b.equals();
                }
            }
        }

        @Override
        protected Set<Ref> doVariables(Algorithm alg) {
            return Collections.singleton(alg.getRef("pc"));
        }
    }

    static class While extends Statement {

        final Compound body;
        final Expr     cond;

        public While(Expr cond, Compound body) {
            this.cond = cond;
            this.body = body;
        }

        @Override
        protected boolean needsLabel() {
            return true;
        }

        @Override
        public String toString() {
            return "WHILE " + cond + " " + body;
        }

        @Override
        protected void build(Algorithm alg) {
            throw new UnsupportedOperationException("While should have been removed!");
            //            ExprBuilder<Expr> b = alg.b;
            //            b.push(cond).resolve();
            //            body.build(alg);
            //            b.implies();
        }

        @Override
        protected void split(Algorithm alg, String name, List<Statement> block) {
            ArrayList<Statement> l = new ArrayList<>(this.body.statements);
            l.add(new Goto(name));
            Compound split = alg.split(name, l);
            block.add(new While(cond, split));
        }

        /*
         * This is trick. A while is quite special. We make sure a while is always the
         * first statement of a block. This method then replaces the WHILE with an IF.
         */
        public static Compound fixup(String name, List<Statement> output) {
            if (output.size() > 0 && output.get(0) instanceof While) {

                While w = (While) output.remove(0);
                If if1 = new Statement.If(w.cond, w.body, new Compound(output));
                return new Compound(if1);
            }
            return null;
        }
    }

    static class If extends Statement {

        final Expr     cond;
        final Compound ifTrue;
        final Compound ifFalse;

        public If(Expr cond, Compound ifTrue, Compound ifFalse) {
            this.cond = cond;
            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
        }

        @Override
        public String toString() {
            return "IF " + cond + " " + ifTrue + " else " + ifFalse;
        }

        @Override
        protected void split(Algorithm algorithm, String name, List<Statement> output) {
            Compound ifTrue = algorithm.split(null, this.ifTrue.statements);
            Compound ifFalse = algorithm.split(null, this.ifFalse.statements);
            output.add(new If(this.cond, ifTrue, ifFalse));
        }

        @Override
        protected void build(Algorithm alg) {
            ExprBuilder<Expr> b = alg.b;
            b.push(this.cond).resolve();
            ifTrue.build(alg);

            if (ifFalse != null && !ifFalse.isEmpty()) {
                ifFalse.build(alg);
                b.impliesElse();
            } else {
                b.implies();
            }
        }

        @Override
        protected Set<Ref> doVariables(Algorithm alg) {
            Set<Ref> t = ifTrue.doVariables(alg);
            Set<Ref> f = ifFalse.doVariables(alg);
            Set<Ref> total = new HashSet<>(t);
            total.addAll(f);

            alg.unchanged(subtract(total, t), ifTrue);
            alg.unchanged(subtract(total, f), ifFalse);
            return total;
        }

    }

    static class Assignment extends Statement {

        final Expr lvar;
        final Expr value;

        public Assignment(Expr lvar, Expr value) {
            this.lvar = lvar;
            this.value = value;
        }



        @Override
        public String toString() {
            return "LET " + lvar + " := " + value;
        }

        @Override
        protected void build(Algorithm alg) {
            ExprBuilder<Expr> b = alg.b;
            if (lvar != null) {
                b.push(lvar).resolve();
                if (!b.isInit())
                    b.prime();

                if (value == null)
                    b.push(lvar).resolve();
                else
                    b.push(value).resolve();
                b.equals();
            } else {
                b.push(value).resolve();
            }
        }

        @Override
        protected Set<Ref> doVariables(Algorithm alg) {
            Ref ref = alg.getRef(lvar);
            return Collections.singleton(ref);
        }


    }

    static class With extends Statement {

        final Compound   compound;
        final List<Decl> decls;

        public With(List<Decl> decls, Compound compound) {
            this.decls = decls;
            this.compound = compound;
        }



        @Override
        public String toString() {
            return "WITH " + decls + " | " + compound;
        }

        @Override
        protected void build(Algorithm alg) {
            ExprBuilder<Expr> b = alg.b;

            compound.build(alg);
            b.some(decls);
        }

        @Override
        protected Set<Ref> doVariables(Algorithm alg) {
            return compound.doVariables(alg);
        }


    }


    static class Require extends Statement {

        final Expr body;

        public Require(Expr body) {
            this.body = body;
        }



        @Override
        public String toString() {
            return "REQUIRE " + body;
        }

        @Override
        protected void build(Algorithm alg) {
            alg.b.push(body);
        }

    }


    protected void split(Algorithm algorithm, String name, List<Statement> output) {
        output.add(this);
    }

    protected boolean needsLabel() {
        return false;
    }

    protected abstract void build(Algorithm alg);

    protected Set<Ref> doVariables(Algorithm alg) {
        return Collections.emptySet();
    }

    private static Set<Ref> subtract(Set<Ref> total, Set<Ref> f) {
        Set<Ref> s = new HashSet<>(total);
        s.removeAll(f);
        return s;
    }


}
