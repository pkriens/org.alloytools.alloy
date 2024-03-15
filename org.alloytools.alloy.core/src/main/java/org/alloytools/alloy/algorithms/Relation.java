package org.alloytools.alloy.algorithms;

import java.util.ArrayList;
import java.util.List;

import edu.mit.csail.sdg.ast.Expr;
import edu.mit.csail.sdg.ast.Sig;

public class Relation {

    enum Usage {
                THIS,
                ONE,
                MULTIPLE,
                FIXED,
                P
    }

    final Sig        head;
    final Expr       remainder;
    final String     label;
    final List<Expr> accesses = new ArrayList<>();
    final Usage      usage;

    Relation(String label, Sig sig, Expr expr, Usage usage) {
        this.label = label;
        this.head = sig;
        this.remainder = expr;
        this.usage = usage;
    }


    @Override
    public String toString() {
        return label;
    }

}
