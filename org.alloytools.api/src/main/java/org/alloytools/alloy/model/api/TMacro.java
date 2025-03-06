package org.alloytools.alloy.model.api;

import java.util.List;

public interface TMacro {

    String name();

    List<String> arguments();

    TExpr body();
}
