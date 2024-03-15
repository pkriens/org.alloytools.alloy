package org.alloytools.alloy.algorithms;

import java.util.Objects;

import edu.mit.csail.sdg.alloy4.Pos;
import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.ExprBadJoin;
import edu.mit.csail.sdg.ast.ExprVar;

class Ref {

    Relation relation;
    Expr     path;


    Ref(Relation r, Expr path) {
        relation = r;
        this.path = path;
    }

    public Ref(Relation rel) {
        // TODO Auto-generated constructor stub
    }

    public Ref() {
        // TODO Auto-generated constructor stub
    }

    Expr getPath() {
        if (path == null)
            return ExprVar.make(Pos.UNKNOWN, relation.head.label);
        return ExprBadJoin.make(Pos.UNKNOWN, Pos.UNKNOWN, path, ExprVar.make(Pos.UNKNOWN, relation.head.label));
    }

    @Override
    public String toString() {
        return (path == null ? "" : path + ".") + relation.label;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, relation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ref other = (Ref) obj;
        return Objects.equals(path, other.path) && Objects.equals(relation, other.relation);
    }
}
