package org.alloytools.alloy.model.api;

import org.alloytools.alloy.builder.api.Pos;

public interface TExpr extends Typed {

    Pos<TType> where();

    default TType type() {
        return where().get();
    }
}
