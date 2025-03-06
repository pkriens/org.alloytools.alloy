package org.alloytools.alloy.util.relations;

import org.alloytools.alloy.instance.api.IAtom;
import org.alloytools.alloy.instance.api.IRelation;
import org.alloytools.alloy.instance.api.ITuple;
import org.alloytools.alloy.instance.api.Solution;

public record Tuple(Solution solution, int arity, Atom[] atoms) implements ITuple {

    @Override
    public int compareTo(ITuple o) {
        int arity = arity();
        int result = Integer.compare(arity, o.arity());
        if (result != 0)
            return result;

        for (int i = 0; i < arity; i++) {
            result = get(i).compareTo(o.get(i));
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    @Override
    public IRelation asRelation() {
        return new Relation(solution, arity(), new Tuple[] {
                                                            this
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String del = "";
        for (int i = 0; i < arity(); i++) {
            sb.append(del);
            sb.append(get(i));
            del = "->";
        }

        return sb.toString();
    }

    @Override
    public IAtom get(int n) {
        assert n >= 0 && n < arity;
        return atoms[ n];
    }

}
