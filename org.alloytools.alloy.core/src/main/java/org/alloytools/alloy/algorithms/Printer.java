package org.alloytools.alloy.algorithms;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.ast.Assert;
import edu.mit.csail.sdg.ast.Attr;
import edu.mit.csail.sdg.ast.Attr.AttrType;
import edu.mit.csail.sdg.ast.Decl;
import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.ExprBad;
import edu.mit.csail.sdg.ast.ExprBadCall;
import edu.mit.csail.sdg.ast.ExprBadJoin;
import edu.mit.csail.sdg.ast.ExprBinary;
import edu.mit.csail.sdg.ast.ExprCall;
import edu.mit.csail.sdg.ast.ExprConstant;
import edu.mit.csail.sdg.ast.ExprHasName;
import edu.mit.csail.sdg.ast.ExprITE;
import edu.mit.csail.sdg.ast.ExprLet;
import edu.mit.csail.sdg.ast.ExprList;
import edu.mit.csail.sdg.ast.ExprQt;
import edu.mit.csail.sdg.ast.ExprUnary;
import edu.mit.csail.sdg.ast.ExprUnary.Op;
import edu.mit.csail.sdg.ast.ExprVar;
import edu.mit.csail.sdg.ast.Func;
import edu.mit.csail.sdg.ast.Sig;
import edu.mit.csail.sdg.ast.Sig.Field;
import edu.mit.csail.sdg.ast.Sig.PrimSig;
import edu.mit.csail.sdg.ast.VisitReturn;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.Macro;

public class Printer extends VisitReturn<Void> {

    final Formatter out;
    String          indent = "";
    boolean         detailed;

    public Printer(Formatter out) {
        this.out = out;
    }

    public Printer(PrintStream out) {
        this.out = new Formatter(out);
    }

    /** Visits a ExprBad node */
    @Override
    public Void visit(ExprBad x) throws Err {
        out.format("%s BAD %s\n", indent, x.errors.pick());
        return null;
    }


    /** Visits a ExprBadCall node */
    @Override
    public Void visit(ExprBadCall x) throws Err {
        out.format("%s BAD CALL %s %s\n", indent, x.fun, x.errors.pick());
        return null;
    }

    /** Visits a ExprBadJoin node However, this is used as a join before resolved */
    @Override
    public Void visit(ExprBadJoin x) throws Err {
        if (isSimple(x)) {
            if (x.right instanceof ExprVar)
                out.format("%s %s.%s\n", indent, toString(x.left), toString(x.right));
            else
                out.format("%s %s[%s]\n", indent, toString(x.right), toString(x.left));
        } else {
            out.format("%s JOIN!\n", indent);
            indent(() -> {
                x.left.accept(this);
                x.right.accept(this);
            });
        }
        return null;
    }

    @Override
    public Void visit(ExprBinary x) throws Err {
        if (isSimple(x.left) && isSimple(x.right)) {
            out.format("%s %s %s %s\n", indent, toString(x.left), x.op, toString(x.right));
        } else {
            out.format("%s %s (\n", indent, x.op);
            indent(() -> {
                x.left.accept(this);
                x.right.accept(this);
            });
            out.format("%s )\n", indent, x.op);
        }
        return null;
    }

    private String toString(Expr expr) {
        if (expr instanceof ExprConstant)
            return expr.toString();

        if (isSimple(expr)) {
            if (expr instanceof ExprBadJoin) {
                if (((ExprBadJoin) expr).right instanceof ExprVar || ((ExprBadJoin) expr).right instanceof ExprUnary)
                    return toString(((ExprBadJoin) expr).left) + "." + toString(((ExprBadJoin) expr).right);
                else
                    return toString(((ExprBadJoin) expr).right) + "[" + toString(((ExprBadJoin) expr).left) + "]";

            }

            if (expr instanceof ExprUnary && ((ExprUnary) expr).op == ExprUnary.Op.PRIME) {
                return toString(((ExprUnary) expr).sub) + "'";
            }

            if (expr instanceof Sig) {
                return toSimple(((Sig) expr).label);
            }
        }
        return expr.toString();
    }

    private boolean isSimple(Expr expr) {
        if (detailed)
            return false;
        if (expr instanceof ExprHasName || expr instanceof ExprConstant) {
            return true;
        }
        if (expr instanceof ExprBadJoin) {
            ExprBadJoin ebj = (ExprBadJoin) expr;
            if (isSimple(ebj.left) && isSimple(ebj.right)) {
                return true;
            }
        }
        if (expr instanceof ExprUnary && ((ExprUnary) expr).op == ExprUnary.Op.PRIME && isSimple(((ExprUnary) expr).sub)) {
            return true;
        }
        if (expr instanceof ExprBinary && isSimple(((ExprBinary) expr).left) && isSimple(((ExprBinary) expr).right)) {
            return true;
        }
        if (expr instanceof Sig)
            return true;

        return false;
    }

    private void indent(Runnable run) {
        String old = indent;
        indent += "    ";
        run.run();
        this.indent = old;

    }

    @Override
    public Void visit(ExprList x) throws Err {
        if (detailed) {
            out.format("%s %s (\n", indent, x.op);
            indent(() -> {
                for (Expr e : x.args) {
                    e.accept(this);
                }
            });
            out.format("%s )\n", indent, x.op);
        } else {
            String op = (x.op == ExprList.Op.AND ? "" : x.op.toString()).toLowerCase();
            indent(() -> {
                String del = "";
                String old = indent;
                for (Expr e : x.args) {
                    indent = old + del;
                    e.accept(this);
                    del = op;
                }
                indent = old;
            });
        }
        return null;
    }

    @Override
    public Void visit(ExprCall x) throws Err {
        out.format("%s CALL %s\n", indent, x.fun);
        indent(() -> {
            for (Expr e : x.args) {
                e.accept(this);
            }
        });
        return null;
    }

    @Override
    public Void visit(ExprConstant x) throws Err {
        out.format("%s CONST %s\n", indent, x);
        return null;
    }

    @Override
    public Void visit(ExprITE x) throws Err {
        out.format("%s %s implies {\n", indent, toString(x.cond));
        indent(() -> {
            x.left.accept(this);
            if (x.right != null) {
                out.format("%s } else {\n", indent);
                x.right.accept(this);
            }
        });
        out.format("%s }\n", indent);
        return null;
    }

    @Override
    public Void visit(ExprLet x) throws Err {
        out.format("%s LET %s\n", indent, x.var);
        indent(() -> {
            x.expr.accept(this);
            x.sub.accept(this);
        });
        return null;
    }

    @Override
    public Void visit(ExprQt x) throws Err {
        out.format("%s %s ", indent, x.op);
        print(x.decls);
        out.format("%s {\n", indent);
        indent(() -> {
            x.sub.accept(this);
        });
        out.format("%s }\n", indent);
        return null;
    }

    @Override
    public Void visit(ExprUnary x) throws Err {
        if (x.op == Op.NOOP) {
            return x.sub.accept(this);
        }
        if (isSimple(x) && x.op == ExprUnary.Op.PRIME) {
            out.format("%s %s'\n", indent, x.sub);
            return null;
        }
        out.format("%s %s (\n", indent, x.op);

        indent(() -> {
            x.sub.accept(this);
        });
        out.format("%s )\n", indent, x.op);
        return null;
    }

    @Override
    public Void visit(ExprVar x) throws Err {
        if (detailed)
            out.format("%s %s\n", indent, x.label);
        else
            out.format("%s", x.label);
        return null;
    }

    @Override
    public Void visit(Sig x) throws Err {
        out.format("%s %s\n", indent, x);
        return null;
    }

    @Override
    public Void visit(Field x) throws Err {
        out.format("%s %s\n", indent, x);
        return null;
    }

    public void print(Func f) {
        if (f.isPred) {
            out.format("pred");
        } else {
            out.format("fun");
        }

        List<Decl> decls;
        if (f.decls == null)
            decls = Collections.EMPTY_LIST;
        else
            decls = new ArrayList<>(f.decls);

        boolean isThis = false;

        if (!decls.isEmpty() && decls.get(0).hasName("this") && decls.get(0).names.size() == 1) {
            Expr type = decls.get(0).expr;
            out.format(" %s.%s", type, f.label);
            decls.remove(0);
        } else {
            out.format(" %s", f.label);
        }

        if (!decls.isEmpty()) {
            out.format("[");

            print(decls);
            out.format("]");
        }
        if (!f.isPred && f.returnDecl != null) {
            out.format(" : ");
            f.returnDecl.accept(this);
        }
        out.format("{%n");
        indent(() -> f.getBody().accept(this));
        out.format("}%n");
    }

    private void print(List<Decl> decls) {
        String del = "";
        for (Decl decl : decls) {
            if (decl.isVar != null) {
                out.format(" var");
            }
            String names = decl.names.stream().map(e -> e.label).collect(Collectors.joining(", "));
            out.format(" %s : ", names);
            decl.expr.accept(this);
            out.format(del);
            del = ", ";
        }
    }

    public void print(CompModule m) {
        for (Sig s : m.getAllSigs()) {
            print(m, s);
        }
        for (Func f : m.getAllFunc()) {
            print(f);
        }
        for (Assert f : m.getAllAssertions()) {
            out.format("assert %s {", f);
            indent(() -> {
                f.accept(this);
            });
            out.format("}");
        }
    }

    private void print(CompModule m, Sig ss) {
        PrimSig s = (PrimSig) ss;

        Sig parent = m.getSig(toSimple(s.parent.label));
        if (parent != null && parent.isEnum != null)
            return;

        String del = "";
        boolean isEnum = false;
        AttrType instances = AttrType.SOME;

        for (Attr a : s.attributes) {
            if (a != null && a.pos != null) {
                switch (a.type) {
                    case ABSTRACT :
                    case LONE :
                    case SOME :
                    case ONE :
                        instances = a.type;
                        break;

                    case BUILTIN :
                        break;
                    case ENUM :
                        isEnum = true;
                        break;
                    case EXACT :
                        break;
                    case META :
                        break;
                    case PRIVATE :
                        break;
                    case SUBSET :
                        break;
                    case SUBSIG :
                        break;
                    case VARIABLE :
                        break;
                    case WHERE :
                        break;
                    default :
                        break;
                }
            }
        }
        if (isEnum) {
            out.format("enum %s {", toSimple(s.label));
            String delx = "";
            for (Sig sig : m.getAllSigs()) {
                PrimSig ps = (PrimSig) sig;
                if (ss == m.getSig(toSimple(ps.parent.label))) {
                    out.format("%s%s", delx, toSimple(ps.label));
                    delx = ", ";
                }
            }
            out.format("}%n");
        } else {
            switch (instances) {
                case ABSTRACT :
                    out.format("abstract ");
                    break;
                case LONE :
                    out.format("lone ");
                    break;
                case ONE :
                    out.format("one ");
                    break;
                case SOME :
                    break;

                default :
                    out.format("??? ");
            }
            out.format("sig %s ", toSimple(s.label));
            if (!s.parent.label.equals("univ")) {
                out.format("extends %s ", toSimple(s.parent.label));
            }

            List<Decl> fields = m.getFields(ss);
            if (fields.isEmpty())
                out.format("{}%n");
            else {
                out.format("{%n");
                for (Decl field : fields) {
                    out.format("  ");
                    print(field);
                    out.format("\n");
                }
                out.format("}%n");
            }
        }
    }

    public void print(Decl field) {
        String names = field.names.stream().map(e -> e.label).collect(Collectors.joining(", "));
        if (field.isVar != null) {
            out.format("var ");
        }
        out.format("%s : %s,", names, field.expr);
    }

    private String toSimple(String label) {
        int n = label.indexOf('/');
        return label.substring(n + 1);
    }

    @Override
    public Void visit(Func x) throws Err {
        String type = x.isPred ? "pred" : "fun";
        out.format("%s %s\n", type, x);
        return null;
    }

    @Override
    public Void visit(Assert x) throws Err {
        out.format("%s\n", x);
        return null;
    }

    @Override
    public Void visit(Macro macro) throws Err {
        out.format("%s\n", macro);
        return null;
    }


}
