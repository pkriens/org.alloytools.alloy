package org.alloytools.alloy.algorithms;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formattable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.alloytools.alloy.algorithms.Statement.Compound;
import org.alloytools.alloy.algorithms.Statement.Goto;
import org.alloytools.alloy.algorithms.Statement.If;
import org.alloytools.alloy.algorithms.Statement.Label;
import org.alloytools.alloy.algorithms.Statement.Skip;
import org.alloytools.alloy.algorithms.Statement.While;

import edu.mit.csail.sdg.alloy4.Pos;
import edu.mit.csail.sdg.ast.Attr;
import edu.mit.csail.sdg.ast.Decl;
import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.ExprBadJoin;
import edu.mit.csail.sdg.ast.ExprConstant;
import edu.mit.csail.sdg.ast.ExprHasName;
import edu.mit.csail.sdg.ast.ExprVar;
import edu.mit.csail.sdg.ast.Sig;
import edu.mit.csail.sdg.parser.CompModule;

/**
 * This
 *
 * @author aqute
 *
 */
public class Algorithm {

    final CompModule           module;
    final Map<String,Step>     steps     = new HashMap<>();
    int                        label     = 100;
    final ExprBuilder<Expr>    b;
    final Map<String,Relation> relations = new HashMap<>();
    private Sig                this_;
    String                     name;
    List<Decl>                 prototypeEventFunctions;
    Expr                       process;


    static Algorithm           a;

    public static Algorithm c(CompModule module) {
        if (a == null) {
            a = new Algorithm(module);
        }
        return a;
    }


    public Statement skip() {
        return new Skip();
    }

    public Statement goto_(String name) {
        return new Goto(name);
    }

    public Statement while_(Expr cond, List<Statement> body) {
        return new While(cond, new Compound(body));
    }

    public Statement if_(Expr cond, List<Statement> ifTrue, List<Statement> ifFalse) {
        return new If(cond, new Compound(ifTrue), new Compound(ifFalse));
    }


    public Statement assignment(Expr lvar, Expr value) {
        return new Statement.Assignment(lvar, value);
    }

    public Statement ifThenElse(Expr cond, List<Statement> ifTrue, List<Statement> ifFalse) {
        return new Statement.If(cond, new Compound(ifTrue), new Compound(ifFalse));
    }

    public Statement ifThen(Expr cond, List<Statement> ifTrue) {
        return new Statement.If(cond, new Compound(ifTrue), new Compound());
    }

    public List<Statement> statements(List<Statement> l, Statement s) {
        ArrayList<Statement> nl = new ArrayList<>(l);
        nl.add(s);
        return nl;
    }

    public List<Statement> clause(Statement s) {
        return Arrays.asList(s);
    }

    public void algorithm(String name, List<Decl> declarations, List<Statement> body, Expr process) {
        this.name = name;
        this.process = process;
        a = null;

        body = new ArrayList<>(body);
        body.add(new Statement.Label("Done"));
        Compound init = split("Init", body);



        ExprVar enumName = b.makeVar(name + "Enum");
        Sig e = b.defineEnum(enumName.label, steps.keySet());

        Expr processType = enumName;
        if (process != null) {
            b.push(process);
            b.push(enumName);
            b.arrow();
            processType = b.pop();
            b.extra.put("process", processType);
        }


        List<Decl> newDecls = new ArrayList<>();
        Decl pc = b.decl(processType, "pc");
        newDecls.add(pc);

        if (process != null) {
            Decl p = b.decl(process, "p");
            newDecls.add(p);
        }

        newDecls.addAll(declarations);

        this_ = b.defineSig(name, null, null, newDecls, ExprConstant.TRUE, Attr.ONE);
        List<Decl> fields = module.getFields(this_);

        Decl prototypeEventFunction = new Decl(null, null, null, null, Arrays.asList(b.var("this").pop()), b.var(name).pop());
        prototypeEventFunctions = Arrays.asList(prototypeEventFunction);


        for (Sig s : module.getAllSigs()) {
            List<Decl> fields2 = module.getFields(s);
            if (fields2 != null) {
                for (Decl d : fields2) {
                    for (ExprHasName ehn : d.names) {
                        String relationName = ehn.label;
                        Relation.Usage usage = s == this_ ? Relation.Usage.THIS : s.isOne == null ? Relation.Usage.MULTIPLE : Relation.Usage.ONE;
                        if ((s == this_ && relationName.equals("p")) || d.isPrivate != null)
                            usage = Relation.Usage.P;

                        Relation r = new Relation(relationName, s, d.expr, d.isVar != null ? usage : Relation.Usage.FIXED);
                        if (s == this_) {
                            b.context.put(relationName, b.makeVar("this"));
                        }
                        relations.put(relationName, r);
                    }
                }
            }
        }
        System.out.println(relations);


        int markOuter = b.size();

        b.setInit(true);
        init.build(this);
        b.setInit(false);

        int markInner = b.size();

        for (Step step : steps.values()) {
            step.doVariables(this);
            Expr bdy = step.build(this);
            if (process != null) {
                b.var("this").var("p").join();
                b.var(step.label);
                b.arrow();
                b.var("this").var("pc").join();
                b.in();
            } else {
                b.var("this").var("pc").join();
                b.var(step.label);
                b.equals();
            }

            b.push(bdy);
            b.and(2);
            module.addFunc(Pos.UNKNOWN, null, step.label, null, prototypeEventFunctions, null, b.noop().pop());

            b.var("this").var(step.label).join();

            System.out.println(step);
        }

        b.or(b.size() - markInner);
        b.always();

        finished(b);

        b.eventually();

        b.and(b.size() - markOuter);
        module.addFunc(Pos.UNKNOWN, null, name, null, prototypeEventFunctions, null, b.noop().pop());

    }


    void finished(ExprBuilder<Expr> b) {
        if (process != null) {
            b.var("this").var("pc").join();
            b.push(process);
            b.var("Done");
            b.arrow();
            b.equals();

        } else {
            b.var("this").var("pc").join();
            b.var("Done");
            b.equals();
        }
    }


    public Statement label(String label) {
        return new Statement.Label(label);
    }



    public Algorithm(CompModule module) {
        this.module = module;
        b = new ExprBuilder<>(module);
    }

    public void print(Formattable out) {
    }

    Compound split(String name, List<Statement> body) {
        List<Statement> output = new ArrayList<>();

        for (int index = 0; index < body.size(); index++) {
            Statement s = body.get(index);

            if (s.needsLabel()) {
                if (index != 0 || name.equals("Init")) {
                    s = new Label("_L" + label++);
                    index--;
                }
            }

            if (s instanceof Label) {
                Label l = (Label) s;

                output.add(new Statement.Goto(l.label));

                Compound next = split(l.label, body.subList(index + 1, body.size()));
                steps.put(l.label, new Step(l.label, next));
                break;
            } else {
                s.split(this, name, output);
            }
        }

        Compound result = While.fixup(name, output);
        if (result != null)
            return result;

        return new Compound(output);
    }

    public Statement assert_(String name, Expr e) {
        return new Statement.Assert(name, e);
    }

    public Expr lvar(Expr j, String label) {
        return ExprBadJoin.make(j.pos, Pos.UNKNOWN, j, ExprVar.make(j.pos, label));
    }

    public Expr lvar(Expr j, Expr i) {
        return ExprBadJoin.make(j.pos, Pos.UNKNOWN, i, j);
    }


    public Ref getRef(Expr lvar) {
        Expr resolved = b.resolve(lvar);

        if (resolved instanceof ExprVar) {
            // must be a standalone relation or sig
            String label = ((ExprVar) resolved).label;
            Relation r = relations.get(label);
            if (r == null) {
                error("Expected a relation %s but not found", label);
            } else
                return new Ref(r, null);
        }

        if (resolved instanceof ExprBadJoin) {

            Expr left = ((ExprBadJoin) resolved).left;
            Expr right = ((ExprBadJoin) resolved).right;
            while (right instanceof ExprBadJoin) {
                right = ((ExprBadJoin) right).right;
            }

            if (right instanceof ExprVar) {
                String label = ((ExprVar) right).label;
                Relation r = relations.get(label);
                if (r == null) {
                    error("Expected a relation %s but not found", label);
                } else
                    return new Ref(r, left);
            }
        }
        error("Expected a relation %s but not found", lvar);
        return new Ref();
    }

    public Ref getRef(String label) {
        return getRef(ExprVar.make(Pos.UNKNOWN, label));
    }

    private void error(String format, Object... args) {
        System.err.printf(format, args);
        throw new RuntimeException(String.format(format, args));
    }



    public void unchanged(Set<Ref> touched, Compound compound) {
        for (Relation r : relations.values()) {
            Set<Ref> filtered = touched.stream().filter(ref -> ref.relation == r).collect(Collectors.toSet());
            if (!filtered.isEmpty()) {
                b.var(r.label);
                compound.add(new Statement.Assignment(b.pop(), null));
            }
        }

    }

    public String toSimple(String label) {
        int n = label.indexOf('/');
        return label.substring(n + 1);
    }


    public Statement with(List<Decl> decls, List<Statement> body) {
        return new Statement.With(decls, new Compound(body));
    }


    public Statement require(Expr e) {
        return new Statement.Require(e);
    }

}
