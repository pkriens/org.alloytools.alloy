package org.alloytools.alloy.model.api;

import java.util.List;

public interface TPred {

    String name();

    TModule module();

    List<TDeclaration> arguments();

    TFormula body();
}
